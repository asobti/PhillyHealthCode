package com.vitaminme.userprofiles;

import android.os.Bundle;

import com.vitaminme.main.BaseActivity;
import com.vitaminme.main.R;

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
