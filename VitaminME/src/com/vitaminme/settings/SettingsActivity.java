package com.vitaminme.settings;

import java.util.ArrayList;
import java.util.List;

import com.vitaminme.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		
		List<String> li = new ArrayList<String>();
		li.add("Test1");
		li.add("Test2");
		li.add("Test3");
		ExcludesAdapter adapter = new ExcludesAdapter(SettingsActivity.this, li);
		
		// bind spinner

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
//		// Create an ArrayAdapter using the string array and a default spinner

		ListView listView = (ListView) findViewById(R.id.excludes_list);
		listView.setAdapter(adapter);
		
		// set the listener
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		// common diets
		// drop down of list (i.e. vegetarian, vegan)

		// ignore foods title
		// your filter
	}

	
}
