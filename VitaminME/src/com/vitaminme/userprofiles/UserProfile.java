package com.vitaminme.userprofiles;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.vitaminme.main.BaseActivity;
import com.vitaminme.main.R;

public class UserProfile extends BaseActivity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		List<String> li = new ArrayList<String>();
		li.add("Test1");
		li.add("Test2");
		li.add("Test3");
		ExcludesAdapter adapter = new ExcludesAdapter(UserProfile.this, li);

		// bind spinner

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		// // Create an ArrayAdapter using the string array and a default
		// spinner

		ListView listView = (ListView) findViewById(R.id.excludes_list);
		listView.setAdapter(adapter);

		// set the listener
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub

			}

		});

		// common diets
		// drop down of list (i.e. vegetarian, vegan)

		// ignore foods title
		// your filter
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		// case android.R.id.home:
		// {
		// onBackPressed();
		// finish();
		// return true;
		// }
		case R.id.save:
			// Save user profile
			Toast.makeText(UserProfile.this, "Saved!", Toast.LENGTH_LONG)
					.show();
		default:
		{
			return super.onOptionsItemSelected(item);
		}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}

}
