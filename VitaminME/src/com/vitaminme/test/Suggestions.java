package com.vitaminme.test;

import android.app.Activity;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.widget.SearchView;

import com.vitaminme.android.R;

public class Suggestions extends Activity implements
		SearchView.OnQueryTextListener
{

	private SearchView searchView;
	String[] COLUMNS = { "_id", "text" };
	MatrixCursor cursor;
	String[] from = { "text" };
	int[] columnTextId = new int[] { android.R.id.text1 };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestions);

		searchView = (SearchView) findViewById(R.id.searchView1);
		searchView.setOnQueryTextListener(this);
	}

	@Override
	public boolean onQueryTextSubmit(String query)
	{
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText)
	{
		cursor = new MatrixCursor(COLUMNS);
		cursor.addRow(new String[] { "1", "test" });
		cursor.addRow(new String[] { "2", "test2" });
		if (cursor.getCount() != 0)
		{
			SuggestionsSimpleCursorAdapter simple = new SuggestionsSimpleCursorAdapter(
					getBaseContext(), android.R.layout.simple_list_item_1,
					cursor, from, columnTextId, 0);

			searchView.setSuggestionsAdapter(simple);

			return true;
		}
		else
		{
			return false;
		}
	}

}