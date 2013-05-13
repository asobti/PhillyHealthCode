package com.vitaminme.recipelist;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vitaminme.android.BaseActivity;
import com.vitaminme.api.ApiAdapter;
import com.vitaminme.data.Ingredient;
import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Pagination;
import com.vitaminme.data.RecipeSummary;
<<<<<<< Updated upstream
=======
import com.vitaminme.main.BaseActivity;
>>>>>>> Stashed changes
import com.vitaminme.android.R;
import com.vitaminme.recipe.RecipeDetails;

public class RecipeList extends BaseActivity
{
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	ListView listView;
	View footerView;
	ItemAdapter itemAdapter;

	int startIndex = 0;
	int count = 20;
	int firstVisibleItem = 0;
	int totalNumResults = 1;
	boolean runningBg = false;

	List<RecipeSummary> recipeList;
	List<String> images = new ArrayList<String>();
	List<String> recipeNames = new ArrayList<String>();
	List<String> notes = new ArrayList<String>();
	List<String> ids = new ArrayList<String>();
	static List<Nutrient> nutrients = new ArrayList<Nutrient>();
	static List<Ingredient> ingredients = new ArrayList<Ingredient>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_list);

		getActionBar().setDisplayHomeAsUpEnabled(true);		
		
		Serializable nutrientsExtra = getIntent().getSerializableExtra("Nutrients");
		if (nutrientsExtra != null) 
			nutrients = (List<Nutrient>)nutrientsExtra;
		
		Serializable ingExtra = getIntent().getSerializableExtra("Ingredients");
		if (ingExtra != null) 
			ingredients = (List<Ingredient>)ingExtra;

		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error)
				// .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.build();
		ImageLoader.getInstance().init(config);

		itemAdapter = new ItemAdapter();
		listView = (ListView) findViewById(android.R.id.list);

		footerView = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.activity_recipe_list_footer, null, false);
		listView.addFooterView(footerView, null, false);

		setListeners();		
	}

	public void fillListView()
	{
		for (RecipeSummary r : recipeList)
		{
			if (r.images.keySet().size() > 0)
				images.add(r.images.get(r.images.keySet().toArray()[0]));
			recipeNames.add(r.name);
			notes.add(r.sourceName);
			ids.add(r.id);
		}

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
						&& startIndex < totalNumResults) {
					firstVisibleItem = firstVisibleItemInScreen;
					
					if (!runningBg) {
						runningBg = true;
						new GetRecipes().execute();					
					}
				}
			}
		});
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
			// holder.text1.setSelected(true);

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
	
	private final class GetRecipes extends AsyncTask<Void, Void, ArrayList<RecipeSummary>> {
		private final ApiAdapter api = ApiAdapter.getInstance();
		ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			if (startIndex == 0) // Only show loading screen when empty
			// screen is loaded onCreate
			{
				progressDialog = new ProgressDialog(RecipeList.this);
				progressDialog.setMessage(getResources().getText(
						R.string.loading_message));
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
		}
		
		@Override
		protected ArrayList<RecipeSummary> doInBackground(Void... arg0) {
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("start", Integer.toString(startIndex)));
			params.add(new SimpleEntry<String, String>("count", Integer.toString(count)));
			
			/*for(Nutrient n : nutrients) {
				String k = String.format("nutrient[%s]",n.tag);
				String v = (n.value == 1) ? "HIGH" : "LOW";
				params.add(new SimpleEntry<String, String>(k, v));
			}*/
			
			for(Ingredient i : ingredients) {
				String k = (i.value == 1) ? "allowedIngredient[]" : "excludedIngredient[]";
				String v = i.searchValue;
				params.add(new SimpleEntry<String, String>(k, v));
			}
			
			try {
				return api.getRecipes(params);
			} catch (Exception e) {
				return null;
			}
			
		}
		
		@Override
		protected void onPostExecute(ArrayList<RecipeSummary> rec) {			
			// hide dialog
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.hide();
			}
			
			runningBg = false;
			
			if (rec != null && rec.size() > 0) {
				Pagination pagination = api.getPaginationObject();
				
				totalNumResults = pagination.num_results;
				setTitle("Recipe List: " + totalNumResults + " items found");
				
				if (startIndex < totalNumResults) {
					recipeList = rec;
					startIndex += pagination.page_results;
					setTitle("Recipe List: " + totalNumResults + " items found");
					fillListView();
				}
				
				if (startIndex >= totalNumResults) {
					listView.removeFooterView(footerView);
				}
			} else if (rec == null) {
				// toast: Internet connection issue
			} else if (rec.size() == 0) {
				// toast: No results
			}
		}		
	}

}
