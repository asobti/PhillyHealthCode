package com.vitaminme.userprofiles;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vitaminme.android.BaseActivity;
import com.vitaminme.android.R;
import com.vitaminme.api.ApiAdapter;
import com.vitaminme.api.ApiFilter;
import com.vitaminme.api.ApiFilterOp;
import com.vitaminme.data.Allergy;
import com.vitaminme.data.Diet;
import com.vitaminme.data.Ingredient;
import com.vitaminme.exceptions.APICallException;

public class UserProfile extends BaseActivity {
	private Vibrator vib;
	boolean firstStart = true;
	boolean firstStartDiet = true;
	boolean searched = false;
	ListView excludesListView;
	ListView myDietListView;
	ArrayAdapter<String> ignoreSearchAdapter;
	ObjectListAdapter excludesAdapter;
	ObjectListAdapter myDietAdapter;
	AllergySpinnerAdapter allergiesAdapter;
	DietSpinnerAdapter dietSpinnerAdapter;
	OnQueryTextListener searchFieldWatcher;
	List<String> myExcludesList = new ArrayList<String>();
	List<String> myDietList = new ArrayList<String>();
	SearchView searchBarIngredients;
	Spinner allergySpinner;
	Spinner dietSpinner;
	SuggestionsSimpleCursorAdapter simple;
	ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	ArrayList<Allergy> allergies = new ArrayList<Allergy>();
	ArrayList<Diet> diets = new ArrayList<Diet>();
	ProgressDialog mDialog;
	List<String> ingredientsArray = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		
		// Common diet Spinner
		allergySpinner = (Spinner) findViewById(R.id.spinner1);
		new getAllergies().execute();
		allergySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!firstStartDiet) {
					// need diet object with ingredients
					// veg Example
					myExcludesList.add(allergiesAdapter.getItem(arg2).longDescription);
					excludesAdapter.notifyDataSetChanged();

				} else {
					firstStartDiet = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		// diet spinner
		dietSpinner = (Spinner) findViewById(R.id.spinner2);
		new getDiets().execute();
		dietSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!firstStart) {
					// need diet object with ingredients
					// veg Example
					myDietList.add(dietSpinnerAdapter.getItem(arg2).shortDescription);
					myDietAdapter.notifyDataSetChanged();

				} else {
					firstStart = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		excludesAdapter = new ObjectListAdapter(UserProfile.this,
				myExcludesList) {
			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
				setListViewHeight(excludesListView);
			}

		};
		
		myDietAdapter = new ObjectListAdapter(UserProfile.this,
				myDietList) {
			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
				setListViewHeight(myDietListView);
			}

		};
		excludesListView = (ListView) findViewById(R.id.excludes_list);
		excludesListView.setAdapter(excludesAdapter);
		setListViewHeight(excludesListView);
		
		myDietListView = (ListView) findViewById(R.id.diet_list);
		myDietListView.setAdapter(myDietAdapter);
		setListViewHeight(myDietListView);

		searchBarIngredients = (SearchView) findViewById(R.id.searchBar);
		searchBarIngredients.setQueryHint("Search for Ingredients");
		searchBarIngredients.setIconified(false);
		searchFieldWatcher = new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.length() > 2 && !searched) {
					ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
							searchBarIngredients.getQuery().toString());
					new getIngredients().execute(filter);
					searched = true;
				}
				if (newText.length() == 2 && searched) {
					ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
							searchBarIngredients.getQuery().toString());
					new getIngredients().execute(filter);
					searched = false;
				}

				ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
						searchBarIngredients.getQuery().toString());
				new getIngredients().execute(filter);
				return true;
			}

		};
		searchBarIngredients.setOnQueryTextListener(searchFieldWatcher);
		searchBarIngredients
				.setOnSuggestionListener(new OnSuggestionListener() {

					@Override
					public boolean onSuggestionClick(int position) {
						searchBarIngredients.setQuery(simple.getCursor()
								.getString(0), false);

						vib.vibrate(20);
						boolean inList = false;
						for (int i = 0; i < myExcludesList.size(); i++) {
							if (myExcludesList.get(i).equals(
									searchBarIngredients.getQuery().toString())) {
								Log.v("mytag", "inside if");
								Toast.makeText(
										UserProfile.this,
										"You already have "
												+ searchBarIngredients
														.getQuery().toString()
												+ " in your list",
										Toast.LENGTH_LONG).show();
								inList = true;
							}
						}
						if (!inList) {
							myExcludesList.add(0, searchBarIngredients
									.getQuery().toString());
							excludesAdapter.notifyDataSetChanged();
						}
						searchBarIngredients.setQuery("", false);

						return true;
					}

					@Override
					public boolean onSuggestionSelect(int position) {
						// TODO Auto-generated method stub
						return false;
					}

				});

	}

	public static void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	private final class getIngredients extends
			AsyncTask<ApiFilter, Void, ArrayList<Ingredient>> {

		private ProgressDialog mDialog;
		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(UserProfile.this);
			mDialog.setMessage("Loading...");
			mDialog.setCancelable(false);
			// mDialog.show();

		}

		@Override
		protected ArrayList<Ingredient> doInBackground(ApiFilter... arg) {
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("count", "1000"));
			List<ApiFilter> filters = new ArrayList<ApiFilter>();

			for (ApiFilter f : arg) {
				filters.add(f);
			}

			try {
				return api.getIngredients(params, filters);
			} catch (APICallException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(final ArrayList<Ingredient> nut) {

			if (nut != null && nut.size() > 0) {
				ingredients = nut;

				String[] columnNames = { "term", "_id" };
				int[] columnTextId = new int[] { android.R.id.text1 };
				MatrixCursor cursor = new MatrixCursor(columnNames);
				int id = 0;
				for (Ingredient i : ingredients) {
					cursor.addRow(new String[] { i.term, Integer.toString(id++) });
				}

				simple = new SuggestionsSimpleCursorAdapter(UserProfile.this,
						android.R.layout.simple_list_item_1, cursor,
						columnNames, columnTextId, 0);

				searchBarIngredients = (SearchView) findViewById(R.id.searchBar);
				searchBarIngredients.setSuggestionsAdapter(simple);

			} else if (nut == null) {
				Toast.makeText(UserProfile.this, "No network found",
						Toast.LENGTH_LONG).show();
			} else if (nut.size() == 0) {
				Toast.makeText(UserProfile.this, "No ingredients found",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(UserProfile.this,
						"There was an error. Please try again",
						Toast.LENGTH_LONG).show();
			}

			if (mDialog.isShowing())
				mDialog.dismiss();

		}
	}

	private final class getAllergies extends
			AsyncTask<ApiFilter, Void, ArrayList<Allergy>> {

		private ProgressDialog mDialog;
		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(UserProfile.this);
			mDialog.setMessage("Loading...");
			mDialog.setCancelable(false);
			// mDialog.show();

		}

		@Override
		protected ArrayList<Allergy> doInBackground(ApiFilter... arg) {
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("count", "100"));

			try {
				return api.getAllergies(params);
			} catch (APICallException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(final ArrayList<Allergy> allergiesReturned) {

			if (allergiesReturned != null && allergiesReturned.size() > 0) {
				allergies = allergiesReturned;
				allergiesAdapter = new AllergySpinnerAdapter(allergies,
						UserProfile.this);
				allergySpinner.setAdapter(allergiesAdapter);

			} else if (allergiesReturned == null) {
				Toast.makeText(UserProfile.this, "No network found",
						Toast.LENGTH_LONG).show();
			} else if (allergiesReturned.size() == 0) {
				Toast.makeText(UserProfile.this, "No ingredients found",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(UserProfile.this,
						"There was an error. Please try again",
						Toast.LENGTH_LONG).show();
			}

			if (mDialog.isShowing())
				mDialog.dismiss();

		}

	}

	private final class getDiets extends
			AsyncTask<ApiFilter, Void, ArrayList<Diet>> {

		private ProgressDialog mDialog;
		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(UserProfile.this);
			mDialog.setMessage("Loading...");
			mDialog.setCancelable(false);
			// mDialog.show();

		}

		@Override
		protected ArrayList<Diet> doInBackground(ApiFilter... arg) {
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("count", "100"));

			try {
				return api.getDiets(params);
			} catch (APICallException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(final ArrayList<Diet> dietsReturned) {

			if (dietsReturned != null && dietsReturned.size() > 0) {
				diets = dietsReturned;
				dietSpinnerAdapter = new DietSpinnerAdapter(diets, UserProfile.this);
				dietSpinner.setAdapter(dietSpinnerAdapter);

			} else if (dietsReturned == null) {
				Toast.makeText(UserProfile.this, "No network found",
						Toast.LENGTH_LONG).show();
			} else if (dietsReturned.size() == 0) {
				Toast.makeText(UserProfile.this, "No ingredients found",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(UserProfile.this,
						"There was an error. Please try again",
						Toast.LENGTH_LONG).show();
			}

			if (mDialog.isShowing())
				mDialog.dismiss();

		}

	}

	public class AllergySpinnerAdapter extends BaseAdapter implements
			SpinnerAdapter {
		private final ArrayList<Allergy> content;
		private final Activity activity;

		public AllergySpinnerAdapter(ArrayList<Allergy> content,
				Activity activity) {
			super();
			this.content = content;
			this.activity = activity;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return content.size();
		}

		@Override
		public Allergy getItem(int arg0) {
			// TODO Auto-generated method stub
			return content.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(android.R.layout.simple_list_item_1,
					parent, false);

			TextView tv = (TextView) v.findViewById(android.R.id.text1);
			tv.setText(content.get(position).shortDescription);

			return v;
		}

	}

	public class DietSpinnerAdapter extends BaseAdapter implements
			SpinnerAdapter {
		private final ArrayList<Diet> content;
		private final Activity activity;

		public DietSpinnerAdapter(ArrayList<Diet> content,
				Activity activity) {
			super();
			this.content = content;
			this.activity = activity;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return content.size();
		}

		@Override
		public Diet getItem(int arg0) {
			// TODO Auto-generated method stub
			return content.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(android.R.layout.simple_list_item_1,
					parent, false);

			TextView tv = (TextView) v.findViewById(android.R.id.text1);
			tv.setText(content.get(position).shortDescription);

			return v;
		}

	}

}
