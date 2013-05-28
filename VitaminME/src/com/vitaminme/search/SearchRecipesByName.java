package com.vitaminme.search;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.text.WordUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vitaminme.android.BaseActivity;
import com.vitaminme.android.R;
import com.vitaminme.api.ApiAdapter;
import com.vitaminme.api.ApiFilter;
import com.vitaminme.api.ApiFilterOp;
import com.vitaminme.data.DietObject;
import com.vitaminme.data.Ingredient;
import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Pagination;
import com.vitaminme.data.RecipeSummary;
import com.vitaminme.exceptions.APICallException;
import com.vitaminme.recipe.RecipeDetails;
import com.vitaminme.recipelist.RecipeListViewPager;


public class SearchRecipesByName extends BaseActivity implements
		SearchView.OnQueryTextListener {
	Context context;
	LayoutInflater inflater;

	private ListView lv;
	private Vibrator vib;
	Boolean searched = false;
	CharSequence search;
	ItemAdapter adapter;
	EditText inputSearch;
	View footerView;
	ArrayList<HashMap<String, String>> productList;
	ArrayList<Nutrient> nutrients;
	ArrayList<DietObject> allItems = new ArrayList<DietObject>();

	ProgressDialog progressDialog;
	ImageButton x;
	SearchView searchView;
	MenuItem searchMenu;
	boolean firstDisplay = true;
	boolean firstQuery = true;
	String courseType;

	ImageLoader imageLoader;// = ImageLoader.getInstance();
	DisplayImageOptions options;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_ingredient_list);
		setSupportProgressBarIndeterminateVisibility(false);

		this.context = getBaseContext();
		if (firstDisplay) // Only show loading screen when empty
		// screen is loaded onCreate
		{
			firstDisplay = false;
		} else {
			searchView.setQueryHint("Search");
		}

		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		lv = (ListView) findViewById(R.id.listView_IngredientsList);
		footerView = (View) getLayoutInflater().inflate(
				R.layout.fragment_search_footer_search_more, null);
		TextView text = (TextView) footerView.findViewById(R.id.text);
		text.setText("Search for recipies");
		lv.addFooterView(footerView);
		adapter = new ItemAdapter();
		lv.setAdapter(adapter);

		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchMenu.expandActionView();
				searchView.requestFocus();
				InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}

		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		searchView = new SearchView(getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Search for Recipies");
		searchView.setOnQueryTextListener(this);

		searchMenu = menu.add("Search");
		searchMenu
				.setIcon(R.drawable.search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
								| MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return true;
	}

	public void fillListView() {
		images.clear();
		recipeNames.clear();
		notes.clear();
		ids.clear();
		
		
		for (RecipeSummary r : recipeList) {
			if (r.images.keySet().size() > 0)
				images.add(r.images.get(r.images.keySet().toArray()[0]));
			recipeNames.add(r.name);
			notes.add(r.sourceName);
			ids.add(r.id);
		}

		((ListView) lv).setAdapter(adapter);

		adapter.notifyDataSetChanged();

//		if (startIndex != 0) {
//			lv.setSelection(firstVisibleItem);
//			lv.post(new Runnable() {
//				@Override
//				public void run() {
//					if (startIndex - 10 != 0) {
//						lv.smoothScrollToPosition(startIndex
//								- recipeList.size());
//					}
//				}
//			});
//		}
	}

	public void setListeners() {
		final Vibrator vibe = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				vibe.vibrate(20);
				System.out.println("Clicked: " + ids.get(position));
				Intent intent = new Intent(context, RecipeDetails.class);
				intent.putExtra("recipe_id", ids.get(position));
				startActivity(intent);
			}
		});

		lv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view,
					int firstVisibleItemInScreen, int visibleItemCount,
					int totalItemCount) {

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

	class ItemAdapter extends BaseAdapter {
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder {
			public TextView text1;
			public ImageView image;
		}

		@Override
		public int getCount() {
			return recipeNames.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				
				view = getLayoutInflater().inflate(R.layout.activity_recipe_list_items,
						parent, false);
				holder = new ViewHolder();
				holder.text1 = (TextView) view.findViewById(R.id.text1);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.text1.setText(recipeNames.get(position));
			imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(images.get(position), holder.image,
					options, animateFirstListener);

			return view;
		}
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	private final class GetRecipes extends
			AsyncTask<Void, Void, ArrayList<RecipeSummary>> {
		private final ApiAdapter api = ApiAdapter.getInstance();
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			if (startIndex == 0) // Only show loading screen when empty
			// screen is loaded onCreate
			{
				progressDialog = new ProgressDialog(SearchRecipesByName.this);
				progressDialog.setMessage(getResources().getText(
						R.string.loading_message));
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
		}

//		String convertCourseType(String courseType) {
//			if (courseType.equals("Breakfast"))
//				return "course^course-Breakfast and Brunch";
//			if (courseType.equals("Lunch"))
//				return "course^course-Lunch and Snacks";
//			if (courseType.equals("Dinner"))
//				return "course^course-Main Dishes";
//			if (courseType.equals("Others"))
//				return "";
//			return "";
//		}

		@Override
		protected ArrayList<RecipeSummary> doInBackground(Void... arg0) {
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("start", Integer
					.toString(startIndex)));
			params.add(new SimpleEntry<String, String>("count", Integer
					.toString(count)));
			// params.add(new SimpleEntry<String, String>("allowedCourse[]",
			// convertCourseType(courseType)));

			params.add(new SimpleEntry<String, String>("q", searchView
					.getQuery().toString()));

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

				// getActivity().setTitle(
				// "Recipe List: " + totalNumResults + " items found");
				
				
				if (startIndex < totalNumResults) {
					recipeList = rec;
					startIndex += pagination.page_results;
					// getActivity().setTitle(
					// "Recipe List: " + totalNumResults + " items found");
					fillListView();
				}

				if (startIndex >= totalNumResults) {
					lv.removeFooterView(footerView);
				}
			} else if (rec == null) {
				// toast: Internet connection issue
			} else if (rec.size() == 0) {
				// toast: No results
			}
		}
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		searchMenu.collapseActionView();
		return true;
	}

	@Override
	public boolean onQueryTextChange(String query) {
		delayHandler.removeMessages(0);
		final Message msg = Message.obtain(delayHandler, 0, query);
		delayHandler.sendMessageDelayed(msg, 500);
		return true;
	}

	private Handler delayHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String query = (String) msg.obj;

				if (query.length() > 2) {

					new GetRecipes().execute();

				}
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}

			}
		}
	};

}
