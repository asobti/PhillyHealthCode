package com.vitaminme.database;

import android.os.Bundle;
import android.widget.TextView;

import com.vitaminme.android.BaseActivity;
import com.vitaminme.main.R;

public class TestDB extends BaseActivity
{
	VitaminME_DB_DataSource datasource;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_db);

		datasource = new VitaminME_DB_DataSource(this);
		datasource.open();

		// datasource.createObject("text1");
		// datasource.createObject("text2");

		System.out.println("Printing now...");
		// System.out.println(datasource.getAllObjects());

		TextView tv = (TextView) findViewById(R.id.testDBtext);
		// tv.setText(datasource.getAllStarred().toString()); // Doesn't work of
		// course, just a placeholder

	}

	@Override
	protected void onResume()
	{
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		datasource.close();
		super.onPause();
	}

}
