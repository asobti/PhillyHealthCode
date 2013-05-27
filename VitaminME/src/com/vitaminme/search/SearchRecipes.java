package com.vitaminme.search;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.text.WordUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import com.vitaminme.android.BaseActivity;
import com.vitaminme.android.R;
import com.vitaminme.api.ApiAdapter;
import com.vitaminme.api.ApiFilter;
import com.vitaminme.api.ApiFilterOp;
import com.vitaminme.data.DietObject;
import com.vitaminme.data.Ingredient;
import com.vitaminme.data.Nutrient;
import com.vitaminme.exceptions.APICallException;
import com.vitaminme.recipelist.RecipeListViewPager;

public class SearchRecipes extends BaseActivity implements
		SearchView.OnQueryTextListener
{
	private ListView lv;
	private Vibrator vib;
	Boolean searched = false;
	CharSequence search;
	ArrayAdapter<String> adapter;
	EditText inputSearch;
	ArrayList<HashMap<String, String>> productList;
	ArrayList<Nutrient> nutrients;
	ArrayList<DietObject> allItems = new ArrayList<DietObject>();
	ArrayList<DietObject> selectedItems = new ArrayList<DietObject>();
	AtomicReference<Object> selectionRef = new AtomicReference<Object>(
			selectedItems);
	ProgressDialog progressDialog;
	ImageButton x;
	SearchView searchView;
	MenuItem searchMenu;
	boolean firstDisplay = true;
	boolean firstQuery = true;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_ingredient_list);
		setSupportProgressBarIndeterminateVisibility(false);

		if (firstDisplay) // Only show loading screen when empty
		// screen is loaded onCreate
		{
			firstDisplay = false;
		}
		else
		{
			searchView.setQueryHint("Search");
		}

		vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		lv = (ListView) findViewById(R.id.listView_IngredientsList);
		View footerView = (View) getLayoutInflater().inflate(
				R.layout.fragment_search_footer_search_more, null);
		TextView text = (TextView) footerView.findViewById(R.id.text);
		text.setText("Search for nutrients + ingredients");
		lv.addFooterView(footerView);
		adapter = new SearchRecipesAdapter(this, selectedItems, selectionRef);
		lv.setAdapter(adapter);

		footerView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				searchMenu.expandActionView();
				searchView.requestFocus();
				InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}

		});

		Button check = (Button) findViewById(R.id.nextButton);
		check.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vib.vibrate(20);

				if (selectedItems != null && selectedItems.size() > 0)
				{
					Intent intent = new Intent(SearchRecipes.this,
							RecipeListViewPager.class);
					intent.putExtra("Ingredients", selectedItems);
					startActivity(intent);
				}
				else
				{
					PopUpSelection();
				}
			}
		});

		Button reviewButton = (Button) findViewById(R.id.reviewButton);
		reviewButton.setText("MY DIET");
		reviewButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vib.vibrate(20);
				if (!selectedItems.isEmpty())
				{
					searchView.setQuery("", false);
				}
				else
				{
					PopUpSelection();
				}
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		// Search view
		// Create the search view

		searchView = new SearchView(getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Search");
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

	private void PopUpSelection()
	{
		AlertDialog.Builder box = new AlertDialog.Builder(this);
		String list = "";
		boolean empty = false;

		box.setTitle("Selected Ingredients");
		for (DietObject n : selectedItems)
		{
			if (n.value > 0)
			{
				list = list + " + " + n.term.toString() + "\n";
			}
		}
		if (list == "")
		{
			list = "Your Diet is empty";
			empty = true;
		}
		box.setMessage(list);
		box.setPositiveButton("OK", new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				// do nothing
			}
		});
		if (!empty)
		{
			box.setNegativeButton("Remove All",
					new DialogInterface.OnClickListener()
					{

						public void onClick(DialogInterface dialog, int which)
						{
							for (int i = 0; i < allItems.size(); i++)
							{
								allItems.get(i).value = 0;
								selectedItems.clear();
								adapter.notifyDataSetChanged();
							}
						}
					});
		}

		AlertDialog helpDialog = box.create();
		helpDialog.show();

	}

	private final class getItems extends
			AsyncTask<ApiFilter, Void, ArrayList<DietObject>>
	{
		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute()
		{
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected ArrayList<DietObject> doInBackground(ApiFilter... arg)
		{
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("count", "100"));
			List<ApiFilter> filters = new ArrayList<ApiFilter>();

			for (ApiFilter f : arg)
			{
				filters.add(f);
			}
			try
			{
				ArrayList<Ingredient> ing = api.getIngredients(params, filters);

				if (firstQuery)
				{
					nutrients = api.getNutrients(params);
					firstQuery = false;
				}
				ArrayList<DietObject> items = new ArrayList<DietObject>();
				if (nutrients != null)
				{
					items.addAll(nutrients);
				}
				if (ing != null)
				{
					items.addAll(ing);
				}

				return items;
			}
			catch (APICallException e)
			{
				return null;
			}
		}

		@Override
		protected void onPostExecute(final ArrayList<DietObject> items)
		{
			if (items != null && items.size() > 0)
			{
				for (DietObject i : items)
				{
					i.term = WordUtils.capitalize(i.term);
				}

				allItems = items;
				adapter = new SearchRecipesAdapter(SearchRecipes.this,
						allItems, selectionRef);
				SearchRecipes.this.adapter.getFilter().filter(
						searchView.getQuery());

				lv.setAdapter(adapter);

				lv.setTextFilterEnabled(true);
				adapter.notifyDataSetChanged();

			}
			else if (items == null)
			{
				Toast.makeText(getBaseContext(), "No network found",
						Toast.LENGTH_LONG).show();
			}
			else if (items.size() == 0)
			{
				Toast.makeText(getBaseContext(), "No ingredients found",
						Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getBaseContext(),
						"There was an error. Please try again",
						Toast.LENGTH_LONG).show();
			}

			// if (progressDialog.isShowing())
			// progressDialog.dismiss();
			setSupportProgressBarIndeterminateVisibility(false);

		}
	}

	@Override
	public boolean onQueryTextSubmit(String query)
	{
		searchMenu.collapseActionView();
		return true;
	}

	@Override
	public boolean onQueryTextChange(String query)
	{
		delayHandler.removeMessages(0);
		final Message msg = Message.obtain(delayHandler, 0, query);
		delayHandler.sendMessageDelayed(msg, 500);
		return true;
	}

	private Handler delayHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 0)
			{
				String query = (String) msg.obj;

				if (query.length() > 2 && !searched)
				{
					if(containsSpace(query)){
						int spaceIndex = query.indexOf(" ");
						String ps1 = query.substring(0, spaceIndex);
						String ps2 = query.substring(spaceIndex + 1);
						ApiFilter filter1 = new ApiFilter("term", ApiFilterOp.like,
								ps1);
						ApiFilter filter2 = new ApiFilter("term", ApiFilterOp.like,
								ps2);
						new getItems().execute(filter1, filter2);
					}
					else{
						ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
								query);
	
						new getItems().execute(filter);
					}
					searched = true;
				}
				else if (query.length() != 0)
				{

					if (SearchRecipes.this.adapter != null)
					{
						if(containsSpace(query)){
							int spaceIndex = query.indexOf(" ");
							String ps1 = query.substring(0, spaceIndex);
							String ps2 = query.substring(spaceIndex + 1);
							ApiFilter filter1 = new ApiFilter("term", ApiFilterOp.like,
									ps1);
							ApiFilter filter2 = new ApiFilter("term", ApiFilterOp.like,
									ps2);
							new getItems().execute(filter1, filter2);
						}
						else{
							ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
									query);
							new getItems().execute(filter);
						}

					}
				}
				else if (!selectedItems.isEmpty())
				{
					adapter = new SearchRecipesAdapter(SearchRecipes.this,
							selectedItems, selectionRef);
					lv.setAdapter(adapter);
				}
				if (adapter != null)
				{
					adapter.notifyDataSetChanged();
				}

			}
		}
	};
	public static boolean containsSpace(final String s)
	{
		if (s != null)
		{
			for (int i = 0; i < s.length(); i++)
			{
				if (Character.isWhitespace(s.charAt(i)))
				{
					//checks for a letter after the whitespace before returning true
					if(s.length() > i){
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
