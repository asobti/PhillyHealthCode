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
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.vitaminme.api.ApiCallParams;
import com.vitaminme.api.ApiCallTask;
import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Pagination;
import com.vitaminme.data.ParseRecipes;
import com.vitaminme.data.Recipe;
import com.vitaminme.main.R;
import com.vitaminme.recipe.RecipeDetails;

public class RecipeList extends Activity
{
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	ListView listView;
	DisplayImageOptions options;
	List<Recipe> recipeList;
	ProgressDialog mDialog;

	ArrayList<String> urls = new ArrayList<String>();
	List<String> images = new ArrayList<String>();
	List<String> recipeNames = new ArrayList<String>();
	List<String> notes = new ArrayList<String>();
	List<String> ids = new ArrayList<String>();

	int counter = 0;
	View footerView;
	ItemAdapter itemAdapter;
	boolean runningBG = false;
	int FirstVisibleItem = 0;
	int TotalNumResults = 1;
	static List<Nutrient> nutrients = new ArrayList<Nutrient>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_list);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		nutrients = (List<Nutrient>) getIntent().getSerializableExtra(
				"Nutrients");

		// for (Nutrient n : nutrients)
		// {
		// System.out.println("name: " + n.name);
		// System.out.println("id: " + n.id);
		// System.out.println("value: " + n.value);
		// }
		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().delayBeforeLoading(1000)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.build();
		ImageLoader.getInstance().init(config);

		// footerView = ((LayoutInflater) this
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
		// R.layout.listfooter, null, false);

		itemAdapter = new ItemAdapter();
		listView = (ListView) findViewById(android.R.id.list);

		setListeners();

	}

	public void fillListView()
	{
		for (Recipe r : recipeList)
		{
			if (r.images.keySet().size() > 0)
				images.add(r.images.get(r.images.keySet().toArray()[0]));
			recipeNames.add(r.name);
			// System.out.println("name: " + r.name);
			notes.add(r.source.sourceName);
			ids.add(r.id);
		}

		((ListView) listView).setAdapter(itemAdapter);

		itemAdapter.notifyDataSetChanged();

		if (counter != 0)
		{
			listView.setSelection(FirstVisibleItem);
			listView.post(new Runnable()
			{
				@Override
				public void run()
				{
					if (counter - 10 != 0)
					{
						listView.smoothScrollToPosition(counter
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
				vibe.vibrate(30);
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
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{

				int lastInScreen = firstVisibleItem + visibleItemCount;

				if (lastInScreen == totalItemCount && counter < TotalNumResults)
				{
					FirstVisibleItem = firstVisibleItem;
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
						apiParams.url += "&start=" + counter;
						apiParams.callBackObject = new ParseRecipes(
								RecipeList.this);

						mDialog = new ProgressDialog(RecipeList.this);
						mDialog.setMessage("Loading...");
						mDialog.setCancelable(false);
						mDialog.show();

						// System.out.println("url: " + apiParams.url);

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
		TotalNumResults = pagination.num_results;
		if (mDialog.isShowing())
			mDialog.dismiss();

		System.out.println("counter: " + counter);

		setTitle("Recipe List: " + TotalNumResults + " items found");

		if (pagination.num_results == 0)
		{
			Toast.makeText(RecipeList.this, "No results found",
					Toast.LENGTH_LONG).show();
		}

		if (counter < pagination.num_results)
		{
			System.out.println("num_recipe: " + recipes.size());
			System.out.println("num_results: " + pagination.num_results);
			recipeList = recipes;
			fillListView();
			counter += pagination.page_results;
		}

	}

	class ItemAdapter extends BaseAdapter
	{
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder
		{
			public TextView text1;
			public TextView text2;
			public ImageView image;
		}

		@Override
		public int getCount()
		{
			// return imageUrls.length;
			return recipeNames.size();
		}

		@Override
		public Object getItem(int position)
		{
			return position;
			// counter += 10;
			// return recipeNames.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
			// return 0;
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

			System.out.println("name " + recipeNames.get(position));
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
			// This is called when the Home (Up) button is pressed
			// in the Action Bar.
			// Intent parentActivityIntent = new Intent(this,
			// MainActivity.class);
			// parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			// | Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(parentActivityIntent);
			onBackPressed();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
