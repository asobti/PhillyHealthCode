package com.vitaminme.recipelist;

import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.TabPageIndicator;
import com.vitaminme.android.BaseActivity;
import com.vitaminme.android.R;

public class RecipeListViewPager extends BaseActivity
{
	String[] courseType = { "Breakfast", "Lunch", "Dinner", "Others" };

	// FragmentPagerAdapter adapter;
	// android.support.v4.view.ViewPager pager;
	// TabPageIndicator indicator;
	// Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);

		Bundle bundle = getIntent().getExtras();

		android.support.v4.view.ViewPager pager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(courseType.length); // Cache all fragments
														// in memory for fast
														// switching

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);

		FragmentPagerAdapter adapter = new RecipeListAdapter(
				getSupportFragmentManager(), bundle);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
	}

	class RecipeListAdapter extends FragmentPagerAdapter
	{
		private Bundle bundle;

		public RecipeListAdapter(FragmentManager fm, Bundle bundle)
		{
			super(fm);
			this.bundle = bundle;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return courseType[position].toUpperCase();
		}

		@Override
		public Fragment getItem(int position)
		{
			Fragment f = new Fragment();
			f = RecipeListFragment.newInstance();
			((RecipeListFragment) f).constructor(bundle, courseType[position]);
			return f;
		}

		@Override
		public int getCount()
		{
			return courseType.length;
		}
	}

}
