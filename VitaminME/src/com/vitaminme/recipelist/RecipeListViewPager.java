package com.vitaminme.recipelist;

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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);

		Bundle bundle = getIntent().getExtras();

		FragmentPagerAdapter adapter = new RecipeListAdapter(
				getSupportFragmentManager(), bundle, this);

		android.support.v4.view.ViewPager pager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	class RecipeListAdapter extends FragmentPagerAdapter
	{
		private Context context;
		private Bundle bundle;

		public RecipeListAdapter(FragmentManager fm, Bundle bundle,
				Context context)
		{
			super(fm);
			this.context = context;
			this.bundle = bundle;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			switch (position)
			{
			case 0:
				return "Breakfast";
			case 1:
				return "Lunch";
			case 2:
				return "Dinner";
			}
			return "Recipe List";
		}

		@Override
		public Fragment getItem(int position)
		{
			Fragment f = new Fragment();
			switch (position)
			{
			case 0:
				f = RecipeListFragment.newInstance(context);
				((RecipeListFragment) f).constructor(context, bundle,
						"Breakfast");
				break;
			case 1:
				f = RecipeListFragment.newInstance(context);
				((RecipeListFragment) f).constructor(context, bundle, "Lunch");
				break;
			case 2:
				f = RecipeListFragment.newInstance(context);
				((RecipeListFragment) f).constructor(context, bundle, "Dinner");
				break;
			}
			return f;
		}

		@Override
		public int getCount()
		{
			return 3;
		}
	}

}
