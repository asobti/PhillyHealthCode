package com.vitaminme.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.vitaminme.android.R;
import com.vitaminme.home.Home;
import com.vitaminme.android.R;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_nutrient_list);

		Intent intent = new Intent(MainActivity.this, Home.class);
		startActivity(intent);
	}

	public void onResume()
	{
		super.onResume();
		finish();
	}

}
