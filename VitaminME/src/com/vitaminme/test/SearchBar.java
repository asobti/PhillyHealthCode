package com.vitaminme.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.widget.SearchView;
import com.vitaminme.api.ApiAdapter;
import com.vitaminme.data.Nutrient;
import com.vitaminme.exceptions.APICallException;
import com.vitaminme.main.BaseActivity;
import com.vitaminme.main.R;

public class SearchBar extends BaseActivity implements
		SearchView.OnQueryTextListener, SearchView.OnSuggestionListener
{
	private SuggestionsAdapter mSuggestionsAdapter;
	private static final String[] COLUMNS = { BaseColumns._ID,
			SearchManager.SUGGEST_COLUMN_TEXT_1, };
	SearchView searchView;
	Activity activity;
	ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();
	ArrayAdapter<String> adapter;
	String query;
	MatrixCursor cursor;
	boolean performingSearch = false;
	ArrayList<String> suggestions = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_bar);
		setSupportProgressBarIndeterminateVisibility(false);

		activity = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Search view
		// Create the search view
		searchView = new SearchView(getSupportActionBar().getThemedContext());
		searchView.setQueryHint("Search by nutrients");

		searchView.setOnQueryTextListener(this);
		searchView.setOnSuggestionListener(this);

		searchView.setIconified(false);
		searchView.requestFocus();

		final MenuItem searchMenu = menu.add("Search");
		searchMenu.setIcon(R.drawable.ic_search_inverse)
				.setActionView(searchView)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
		searchMenu.expandActionView();
//		searchMenu.setOnActionExpandListener(new OnActionExpandListener()
//		{
//
//			@Override
//			public boolean onMenuItemActionExpand(MenuItem item)
//			{
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public boolean onMenuItemActionCollapse(MenuItem item)
//			{
//				searchMenu
//						.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
//				return true;
//			}
//
//		});

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onQueryTextSubmit(String query)
	{
		Toast.makeText(activity, "You searched for: " + query,
				Toast.LENGTH_LONG).show();
		return true;

	}

	@Override
	public boolean onQueryTextChange(String newText)
	{
		delayHandler.removeMessages(0);
		final Message msg = Message.obtain(delayHandler, 0, newText);
		delayHandler.sendMessageDelayed(msg, 500);
		return true;
	}

	@Override
	public boolean onSuggestionSelect(int position)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSuggestionClick(int position)
	{
		Cursor c = (Cursor) mSuggestionsAdapter.getItem(position);
		String query = c.getString(c
				.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
		Toast.makeText(activity, "Suggestion clicked: " + query,
				Toast.LENGTH_LONG).show();
		return true;

	}

	private class SuggestionsAdapter extends CursorAdapter
	{

		public SuggestionsAdapter(Context context, Cursor c)
		{
			super(context, c, 0);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(android.R.layout.simple_list_item_1,
					parent, false);
			return v;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{
			TextView tv = (TextView) view;
			final int textIndex = cursor
					.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
			tv.setText(cursor.getString(textIndex));
		}
	}

	private Handler delayHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == 0)
			{
				String newText = (String) msg.obj;
				refreshList(newText);
			}
		}
	};

	private boolean arrayListContains(ArrayList<String> list, String query)
	{
		for (int i = 0; i < list.size(); i++)

		{
			String item = list.get(i);
			// System.out.println("item: " + item + "query: " + query);
			if (item.toLowerCase(Locale.getDefault()).contains(
					query.toLowerCase(Locale.getDefault())))
			{

				// System.out.println("item true: " + item);
				return true;
			}
		}
		return false;
	}

	private void refreshList(String newText)
	{
		if (newText.length() >= 3)
		{
			query = newText;
			if (!arrayListContains(suggestions, query) && !performingSearch)
				new getNutrients().execute();
			else
			{
				cursor = new MatrixCursor(COLUMNS);
				String item = "";
				for (int i = 0; i < suggestions.size(); i++)
				{
					item = suggestions.get(i);
					if (item.toLowerCase(Locale.getDefault()).contains(
							query.toLowerCase(Locale.getDefault())))
					{
						cursor.addRow(new String[] { Integer.toString(i), item });
					}
				}

				mSuggestionsAdapter = new SuggestionsAdapter(
						getSupportActionBar().getThemedContext(), cursor);

				searchView.setSuggestionsAdapter(mSuggestionsAdapter);
			}
		}
	}

	private final class getNutrients extends
			AsyncTask<Void, Void, ArrayList<Nutrient>>
	{

		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute()
		{
			performingSearch = true;
			setSupportProgressBarIndeterminateVisibility(true);
			System.out.println("Searching...");
		}

		@Override
		protected ArrayList<Nutrient> doInBackground(Void... arg0)
		{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("count", "100");
			params.put(
					"filter",
					"%5B%7B%22name%22%3A%22description%22%2C%22op%22%3A%22like%22%2C%22val%22%3A%22"
							+ query + "%22%7D%5D");

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
				String item = "";
				cursor = new MatrixCursor(COLUMNS);

				for (int i = 0; i < nut.size(); i++)
				{
					item = nut.get(i).name;
					suggestions.add(item);
					cursor.addRow(new String[] { Integer.toString(i), item });
				}

				mSuggestionsAdapter = new SuggestionsAdapter(
						getSupportActionBar().getThemedContext(), cursor);

				searchView.setSuggestionsAdapter(mSuggestionsAdapter);

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

			setSupportProgressBarIndeterminateVisibility(false);
			performingSearch = false;

		}
	}

}
