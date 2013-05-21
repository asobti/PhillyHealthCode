package com.vitaminme.home;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.text.WordUtils;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.vitaminme.android.R;
import com.vitaminme.api.ApiAdapter;
import com.vitaminme.api.ApiFilter;
import com.vitaminme.api.ApiFilterOp;
import com.vitaminme.data.DietObject;
import com.vitaminme.data.Ingredient;
import com.vitaminme.data.Nutrient;
import com.vitaminme.exceptions.APICallException;
import com.vitaminme.recipelist.RecipeListViewPager;

public class DietBuilderListFragment extends SherlockFragment implements
		SearchView.OnQueryTextListener
{
	private ListView lv;
	private Vibrator vib;
	Boolean searched = false;
	CharSequence search;
	ArrayAdapter<String> adapter;
	EditText inputSearch;
	ArrayList<HashMap<String, String>> productList;
	ArrayList<DietObject> allItems = new ArrayList<DietObject>();
	ArrayList<DietObject> selectedItems = new ArrayList<DietObject>();
	AtomicReference<Object> selectionRef = new AtomicReference<Object>(
			selectedItems);
	ProgressDialog progressDialog;
	ImageButton x;
	Activity activity;
	SearchView searchView;
	MenuItem searchMenu;
	boolean firstDisplay = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		activity.setTitle(R.string.title_fragment_search_ingredients);

		setHasOptionsMenu(true);

		if (firstDisplay) // Only show loading screen when empty
		// screen is loaded onCreate
		{
			progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage(getResources().getText(
					R.string.loading_message));
			progressDialog.setCancelable(false);
			progressDialog.show();
			firstDisplay = false;
		}

		ViewGroup vg = (ViewGroup) inflater.inflate(
				R.layout.fragment_ingredient_list, null);

		new getItems().execute();

		vib = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

		lv = (ListView) vg.findViewById(R.id.listView_IngredientsList);
		View footerView = (View) inflater.inflate(
				R.layout.fragment_search_footer_search_more, null);
		TextView text = (TextView) footerView.findViewById(R.id.text);
		text.setText("Click to search for ingredients");
		lv.addFooterView(footerView);

		footerView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				searchMenu.expandActionView();
				searchView.requestFocus();
				InputMethodManager imgr = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}

		});

		Button check = (Button) vg.findViewById(R.id.nextButton);
		check.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vib.vibrate(20);

				if (selectedItems != null
						&& selectedItems.size() > 0)
				{
					Intent intent = new Intent(activity, RecipeListViewPager.class);
					intent.putExtra("Ingredients", selectedItems);
					startActivity(intent);
				}
				else
				{
					PopUpSelection();					
				}
			}
		});

		Button reviewButton = (Button) vg.findViewById(R.id.reviewButton);
		reviewButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vib.vibrate(20);
				PopUpSelection();
			}
		});

		return vg;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		// Search view
		// Create the search view
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		searchView = new SearchView(actionBar.getThemedContext());
		searchView.setQueryHint("Search Ingredients");

		searchView.setOnQueryTextListener(this);

		searchMenu = menu.add("Search");
		searchMenu
				.setIcon(R.drawable.search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
								| MenuItem.SHOW_AS_ACTION_IF_ROOM);
		// searchMenu.expandActionView();
	}

	public void onStart()
	{
		super.onStart();
	}

	private void PopUpSelection()
	{
		AlertDialog.Builder box = new AlertDialog.Builder(activity);
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
			list = "No ingredients selected";
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	private final class getItems extends
			AsyncTask<ApiFilter, Void, ArrayList<DietObject>>
	{
		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute()
		{
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
					true);
		}

		@Override
		protected ArrayList<DietObject> doInBackground(ApiFilter... arg)
		{
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("count", "20"));
			List<ApiFilter> filters = new ArrayList<ApiFilter>();

			for (ApiFilter f : arg)
			{
				filters.add(f);
			}
			try
			{
				ArrayList<Ingredient> ing = api.getIngredients(params, filters);
				ArrayList<Nutrient> nuts = api.getNutrients(params, filters);
				ArrayList<DietObject> items= new ArrayList<DietObject>();
				
				for(int i=0; i < nuts.size(); i++){
					Log.v("mytag", "nut = " +  nuts.get(i).term);
					items.add(nuts.get(i));
				}
				for(int i=0; i < ing.size(); i++){
					Log.v("mytag", "ing = " + ing.get(i).term);
					items.add(ing.get(i));
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
				adapter = new DietObjectListAdapter(activity, allItems,
						selectionRef);

				lv.setAdapter(adapter);
				lv.setTextFilterEnabled(true);
				adapter.notifyDataSetChanged();

			}
			else if (items == null)
			{
				Toast.makeText(activity, "No network found", Toast.LENGTH_LONG)
						.show();
			}
			else if (items.size() == 0)
			{
				Toast.makeText(activity, "No ingredients found",
						Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(activity,
						"There was an error. Please try again",
						Toast.LENGTH_LONG).show();
			}

			if (progressDialog.isShowing())
				progressDialog.dismiss();
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
					false);

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
					ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
							query);
					new getItems().execute(filter);
					searched = true;
				}
				else if (query.length() == 2 && searched)
				{
					ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
							query);
					new getItems().execute(filter);
					searched = false;
				}
				else if (query.length() != 0)
				{

					if (DietBuilderListFragment.this.adapter != null)
					{
						ApiFilter filter = new ApiFilter("term",
								ApiFilterOp.like, query);
						DietBuilderListFragment.this.adapter.getFilter().filter(
								query);
						new getItems().execute(filter);
					}
				}
				else if (!selectedItems.isEmpty())
				{
					adapter = new DietObjectListAdapter(activity, selectedItems,
							selectionRef);
					lv.setAdapter(adapter);
				}

			}
		}
	};
}
