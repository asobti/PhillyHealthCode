package com.vitaminme.recipelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vitaminme.api.ApiCallParams;
import com.vitaminme.api.ApiCallTask;
import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Pagination;
import com.vitaminme.data.ParseRecipes;
import com.vitaminme.data.Recipe;
import com.vitaminme.main.R;
import com.vitaminme.recipe.RecipeDetails;
import com.vitaminme.userprofiles.UserProfile;

public class RecipeList extends Activity
{
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	ListView listView;
	View footerView;
	ItemAdapter itemAdapter;

	ProgressDialog progressDialog;
	int startIndex = 0;
	int count = 20;
	boolean runningBG = false;
	int firstVisibleItem = 0;
	int totalNumResults = 1;

	List<Recipe> recipeList;
	List<String> images = new ArrayList<String>();
	List<String> recipeNames = new ArrayList<String>();
	List<String> notes = new ArrayList<String>();
	List<String> ids = new ArrayList<String>();
	static List<Nutrient> nutrients = new ArrayList<Nutrient>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_list);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (startIndex == 0) // Only show loading screen when empty
		// screen is loaded onCreate
		{
			progressDialog = new ProgressDialog(RecipeList.this);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		nutrients = (List<Nutrient>) getIntent().getSerializableExtra(
				"Nutrients");

		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).memoryCacheExtraOptions(60, 60)
				.defaultDisplayImageOptions(options).build();
		ImageLoader.getInstance().init(config);

		itemAdapter = new ItemAdapter();
		listView = (ListView) findViewById(android.R.id.list);

		footerView = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.activity_recipe_list_footer, null, false);
		listView.addFooterView(footerView);

		setListeners();
	}

	public void fillListView()
	{
		for (Recipe r : recipeList)
		{
			if (r.images.keySet().size() > 0)
				images.add(r.images.get(r.images.keySet().toArray()[0]));
			recipeNames.add(r.name);
			notes.add(r.source.sourceName);
			ids.add(r.id);
		}

		// listView.removeFooterView(footerView);
		// listView.addFooterView(footerView);
		((ListView) listView).setAdapter(itemAdapter);

		itemAdapter.notifyDataSetChanged();

		if (startIndex != 0)
		{
			listView.setSelection(firstVisibleItem);
			listView.post(new Runnable()
			{
				@Override
				public void run()
				{
					if (startIndex - 10 != 0)
					{
						listView.smoothScrollToPosition(startIndex
								- recipeList.size());
					}
				}
			});
		}
	}

	public void setListeners()
	{
		final Vibrator vibe = (Vibrator) RecipeList.this
				.getSystemService(Context.VIBRATOR_SERVICE);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				vibe.vibrate(20);
				System.out.println("Clicked: " + ids.get(position));
				Intent intent = new Intent(RecipeList.this, RecipeDetails.class);
				intent.putExtra("recipe_id", ids.get(position));
				startActivity(intent);
			}
		});

		listView.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
			}

			@Override
			public void onScroll(AbsListView view,
					int firstVisibleItemInScreen, int visibleItemCount,
					int totalItemCount)
			{

				int lastInScreen = firstVisibleItemInScreen + visibleItemCount;

				if (lastInScreen == totalItemCount
						&& startIndex < totalNumResults)
				{
					firstVisibleItem = firstVisibleItemInScreen;
					if (!runningBG)
					{
						// new GetRecipes(RecipeList.this).execute();
						ApiCallParams apiParams = new ApiCallParams();
						apiParams.url = "http://vitaminme.notimplementedexception.me/recipes";
						apiParams.url += "?filter="
								+ "%7B%22nutrients%22%3A%5B";

						for (Nutrient n : nutrients)
						{
							// %7B%22id%22%3A203%7D
							if (n.value == -1 || n.value == 1)
								apiParams.url += "%7B%22id%22%3A" + n.id
										+ "%7D%2C";
						}
						apiParams.url = apiParams.url.substring(0,
								apiParams.url.length() - 3);

						apiParams.url += "%5D%7D";
						apiParams.url += "&start=" + startIndex;
						apiParams.url += "&count=" + count;
						apiParams.callBackObject = new ParseRecipes(
								RecipeList.this);

						ApiCallTask task = new ApiCallTask();
						runningBG = true;
						task.execute(apiParams);
					}
				}
			}

		});
	}

	public void callback(List<Recipe> recipes, Pagination pagination)
	{
		runningBG = false;
		totalNumResults = pagination.num_results;
		if (progressDialog.isShowing())
			progressDialog.dismiss();

		System.out.println("start index: " + startIndex);

		setTitle("Recipe List: " + totalNumResults + " items found");

		if (totalNumResults == 0)
		{
			Toast.makeText(RecipeList.this, "No results found",
					Toast.LENGTH_LONG).show();
		}

		if (startIndex < totalNumResults)
		{
			System.out.println("num_recipe: " + recipes.size());
			System.out.println("page_results: " + pagination.page_results);
			System.out.println("num_results: " + totalNumResults);
			recipeList = recipes;
			startIndex += pagination.page_results;
			fillListView();
		}
		if (startIndex >= totalNumResults) // If startIndex larger after
											// increment
		{
			listView.removeFooterView(footerView);
		}

	}

	class ItemAdapter extends BaseAdapter
	{
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder
		{
			public TextView text1;
			public ImageView image;
		}

		@Override
		public int getCount()
		{
			return recipeNames.size();
		}

		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent)
		{
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null)
			{
				view = getLayoutInflater().inflate(
						R.layout.activity_recipe_list_items, parent, false);
				holder = new ViewHolder();
				holder.text1 = (TextView) view.findViewById(R.id.text1);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) view.getTag();
			}

			holder.text1.setText(recipeNames.get(position));
			holder.text1.setSelected(true);

			imageLoader.displayImage(images.get(position), holder.image,
					options, animateFirstListener);

			return view;
		}
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener
	{

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage)
		{
			if (loadedImage != null)
			{
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay)
				{
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		AnimateFirstDisplayListener.displayedImages.clear();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
		{
			onBackPressed();
			finish();
			return true;
		}
		case R.id.user_profile:
			// open user profile
			Intent intent = new Intent(this, UserProfile.class);
			startActivity(intent);
		default:
		{
			return super.onOptionsItemSelected(item);
		}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
