package com.vitaminme.recipelist;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vitaminme.android.BaseActivity;
import com.vitaminme.android.R;
import com.vitaminme.api.ApiAdapter;
import com.vitaminme.data.Ingredient;
import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Pagination;
import com.vitaminme.data.Recipe;
import com.vitaminme.data.RecipeSummary;
import com.vitaminme.recipe.RecipeDetails;
import com.vitaminme.recipe.pageLayoutRecipe;

public class RecipeListFragment extends Fragment
{
	Context context;
	LayoutInflater inflater;
	boolean firstDisplay;

	ImageLoader imageLoader;// = ImageLoader.getInstance();
	DisplayImageOptions options;
	ListView listView;
	View footerView;
	ItemAdapter itemAdapter;
	String courseType;

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
	List<Nutrient> nutrients = new ArrayList<Nutrient>();
	List<Ingredient> ingredients = new ArrayList<Ingredient>();

	public static Fragment newInstance()
	{
		RecipeListFragment f = new RecipeListFragment();
		return f;
	}

	public void constructor(Bundle bundle, String courseType)
	{
		this.courseType = courseType;

		Serializable nutrientsExtra = bundle.getSerializable("Nutrients");
		if (nutrientsExtra != null)
			nutrients = (List<Nutrient>) nutrientsExtra;

		Serializable ingExtra = bundle.getSerializable("Ingredients");
		if (ingExtra != null)
			ingredients = (List<Ingredient>) ingExtra;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.inflater = inflater;
		ViewGroup vg = (ViewGroup) inflater.inflate(
				R.layout.activity_recipe_list, null);

		itemAdapter = new ItemAdapter();
		listView = (ListView) vg.findViewById(android.R.id.list);

		footerView = (View) inflater.inflate(
				R.layout.activity_recipe_list_footer, null, false);
		listView.addFooterView(footerView, null, false);

		setListeners();

		return vg;
	}

	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		context = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		imageLoader = ImageLoader.getInstance();
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
		final Vibrator vibe = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				vibe.vibrate(20);
				System.out.println("Clicked: " + ids.get(position));
				Intent intent = new Intent(getActivity(), RecipeDetails.class);
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

					if (!runningBg)
					{
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
				view = inflater.inflate(R.layout.activity_recipe_list_items,
						parent, false);
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

	private final class GetRecipes extends
			AsyncTask<Void, Void, ArrayList<RecipeSummary>>
	{
		private final ApiAdapter api = ApiAdapter.getInstance();
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute()
		{
			if (startIndex == 0) // Only show loading screen when empty
			// screen is loaded onCreate
			{
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage(getResources().getText(
						R.string.loading_message));
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
		}

		String convertCourseType(String courseType)
		{
			if (courseType.equals("Breakfast"))
				return "course^course-Breakfast and Brunch";
			if (courseType.equals("Lunch"))
				return "course^course-Lunch and Snacks";
			if (courseType.equals("Dinner"))
				return "course^course-Main Dishes";
			if (courseType.equals("Others"))
				return "";
			return "";
		}

		@Override
		protected ArrayList<RecipeSummary> doInBackground(Void... arg0)
		{
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("start", Integer
					.toString(startIndex)));
			params.add(new SimpleEntry<String, String>("count", Integer
					.toString(count)));
			params.add(new SimpleEntry<String, String>("allowedCourse[]",
					convertCourseType(courseType)));

			/*
			 * for(Nutrient n : nutrients) { String k =
			 * String.format("nutrient[%s]",n.tag); String v = (n.value == 1) ?
			 * "HIGH" : "LOW"; params.add(new SimpleEntry<String, String>(k,
			 * v)); }
			 */

			for (Ingredient i : ingredients)
			{
				String k = (i.value == 1) ? "allowedIngredient[]"
						: "excludedIngredient[]";
				String v = i.searchValue;
				params.add(new SimpleEntry<String, String>(k, v));
			}

			try
			{
				return api.getRecipes(params);
			}
			catch (Exception e)
			{
				return null;
			}

		}

		@Override
		protected void onPostExecute(ArrayList<RecipeSummary> rec)
		{
			// hide dialog
			if (progressDialog != null && progressDialog.isShowing())
			{
				progressDialog.hide();
			}

			runningBg = false;

			if (rec != null && rec.size() > 0)
			{
				Pagination pagination = api.getPaginationObject();

				totalNumResults = pagination.num_results;

				getActivity().setTitle(
						"Recipe List: " + totalNumResults + " items found");

				if (startIndex < totalNumResults)
				{
					recipeList = rec;
					startIndex += pagination.page_results;
					getActivity().setTitle(
							"Recipe List: " + totalNumResults + " items found");
					fillListView();
				}

				if (startIndex >= totalNumResults)
				{
					listView.removeFooterView(footerView);
				}
			}
			else if (rec == null)
			{
				// toast: Internet connection issue
			}
			else if (rec.size() == 0)
			{
				// toast: No results
			}
		}
	}

}
