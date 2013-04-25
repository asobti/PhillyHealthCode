package com.vitaminme.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends BaseActivity
{
	private Fragment mContent;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_activity_home);
		setContentView(R.layout.content_frame);

		Bundle extras = getIntent().getExtras();
		String fragmentName = "";
		if (extras != null)
		{
			fragmentName = extras.getString("fragmentName");
		}

		if (fragmentName.equalsIgnoreCase("home") || fragmentName.isEmpty())
			mContent = new HomeFragment();
		else if (fragmentName.equalsIgnoreCase("nutrients"))
			mContent = new NutrientListFragment();

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

	}
}
