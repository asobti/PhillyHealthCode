package com.vitaminme.home;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
import com.vitaminme.data.Nutrient;
import com.vitaminme.database.VitaminME_DB_DataSource;
import com.vitaminme.exceptions.APICallException;
import com.vitaminme.recipelist.RecipeListViewPager;

public class NutrientListFragment extends SherlockFragment implements
		SearchView.OnQueryTextListener
{
	private ListView lv;
	private Vibrator vib;

	ArrayAdapter<String> adapter;
	EditText inputSearch;
	ArrayList<HashMap<String, String>> productList;
	ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();
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
		activity.setTitle(R.string.title_fragment_search_nutrients);

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
				R.layout.fragment_nutrient_list, null);

		new getNutrients().execute();

		vib = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

		lv = (ListView) vg.findViewById(R.id.listView_NutrientList);
		View footerView = (View) inflater.inflate(
				R.layout.fragment_search_footer_search_more, null);
		TextView text = (TextView) footerView.findViewById(R.id.text);
		text.setText("Click to search for nutrients");
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

		Button nextButton = (Button) vg.findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				boolean next = false;
				vib.vibrate(20);

				for (Nutrient n : nutrients)
				{
					if (n.value == 1 || n.value == -1)
						next = true;
				}

				if (next)
				{
					Intent intent = new Intent(activity, RecipeListViewPager.class);
					intent.putExtra("Nutrients", nutrients);
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
		searchView.setQueryHint("Search Nutrients");

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
		ArrayList<Nutrient> selectedMinus = new ArrayList<Nutrient>();
		ArrayList<Nutrient> selectedPlus = new ArrayList<Nutrient>();
		String list = "";
		boolean empty = false;
		for (int i = 0; i < nutrients.size(); i++)
		{
			if (nutrients.get(i).value == -1)
			{
				selectedMinus.add(nutrients.get(i));
			}
			else if (nutrients.get(i).value == 1)
			{
				selectedPlus.add(nutrients.get(i));
			}
		}
		box.setTitle("Selected Nutrients");
		for (Nutrient n : selectedPlus)
		{
			list = list + " + " + n.term.toString() + "\n";
		}
		for (Nutrient n : selectedMinus)
		{
			list = list + " - " + n.term.toString() + "\n";
		}
		if (list == "")
		{
			list = "No nutrients selected";
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
							for (int i = 0; i < nutrients.size(); i++)
							{
								nutrients.get(i).value = 0;
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

	private final class getNutrients extends
			AsyncTask<Void, Void, ArrayList<Nutrient>>
	{

		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute()
		{
			getSherlockActivity().setSupportProgressBarIndeterminateVisibility(
					true);
		}

		@Override
		protected ArrayList<Nutrient> doInBackground(Void... arg0)
		{
			// Move this into ApiAdapter
			VitaminME_DB_DataSource db = new VitaminME_DB_DataSource(activity);
			db.open();
			ArrayList<Nutrient> nut = db.getAllNutrients();
			if (nut.size() > 0)
			{
				System.out.println("Returning Nutrients from DB");
				db.close();
				return nut;
			}
			// End of move to ApiAdapter

			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("count", "100"));

			try
			{
				return api.getNutrients(params);
			}
			catch (APICallException e)
			{
				return null;
			}
		}

		@Override
		protected void onPostExecute(final ArrayList<Nutrient> nut)
		{

			if (nut != null && nut.size() > 0)
			{
				nutrients = nut;
				adapter = new NutrientListAdapter(activity, nutrients);

				lv.setAdapter(adapter);
				lv.setTextFilterEnabled(true);

			}
			else if (nut == null)
			{
				Toast.makeText(activity, "No network found", Toast.LENGTH_LONG)
						.show();
			}
			else if (nut.size() == 0)
			{
				Toast.makeText(activity, "No nutrients found",
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
		delayHandler.sendMessageDelayed(msg, 0);
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
				if (NutrientListFragment.this.adapter != null)
					NutrientListFragment.this.adapter.getFilter().filter(query);

			}
		}
	};

}
