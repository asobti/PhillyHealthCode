package com.vitaminme.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vitaminme.main.R;

public class ScrollingListView extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scrolling_list_view);

		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutTest);

		int[] drawables = { R.drawable.search, R.drawable.send_now,
				R.drawable.collections_view_as_list };
		String[] labels = { "Search", "Send", "List" };

		ScrollingListViewListAdapter adapter = new ScrollingListViewListAdapter(
				this, drawables, labels);

		for (int i = 0; i < adapter.getCount(); i++)
		{
			View rowView = adapter.getView(i, null, layout);
			layout.addView(rowView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scrolling_list_view, menu);
		return true;
	}

}
