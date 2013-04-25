package com.vitaminme.main;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Favorites extends BaseActivity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_activity_favorites);
		setContentView(R.layout.activity_favorites);
	}

}
