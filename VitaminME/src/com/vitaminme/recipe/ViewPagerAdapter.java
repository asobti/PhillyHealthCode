package com.vitaminme.recipe;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vitaminme.data.Recipe;

public class ViewPagerAdapter extends FragmentPagerAdapter
{
	private Context _context;
	private Recipe recipe;
	String[] pages = {"Nutrition", "Recipe", "Directions"};
	

	public ViewPagerAdapter(Context context, FragmentManager fm, Recipe recipe)
	{
		super(fm);
		_context = context;
		this.recipe = recipe;

	}

	@Override
	public Fragment getItem(int position)
	{
		Fragment f = new Fragment();
		switch (position)
		{
		case 0:
			f = pageLayoutNutrition.newInstance(_context);
			((pageLayoutNutrition) f).constructor(recipe, _context);
			break;
		case 1:
			f = pageLayoutRecipe.newInstance(_context);
			((pageLayoutRecipe) f).constructor(recipe, _context);
			break;
		case 2:
			f = pageLayoutCook.newInstance(_context);
			((pageLayoutCook) f).constructor(recipe);
			break;
		}
		return f;
	}

	@Override
	public int getCount()
	{
		return 3;
	}
	@Override
	public CharSequence getPageTitle(int position)
	{
		return pages[position].toUpperCase();
	}

}