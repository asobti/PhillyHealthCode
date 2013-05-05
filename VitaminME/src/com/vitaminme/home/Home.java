package com.vitaminme.home;

import java.util.HashMap;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.vitaminme.main.BaseActivity;
import com.vitaminme.main.R;

public class Home extends BaseActivity
{
	Fragment mContent;
	HashMap<String, Fragment> Fragments = new HashMap<String, Fragment>();

	protected Dialog mSplashDialog;
	public String currentFragment = "";
	static boolean firstDisplay = true;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		showSplashScreen();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);

		Bundle extras = getIntent().getExtras();
		String fragmentName = "";
		if (extras != null)
		{
			fragmentName = extras.getString("fragmentName");
		}

		// if (savedInstanceState != null)
		// {
		// Fragments = (HashMap<String, Fragment>) savedInstanceState
		// .getSerializable("Fragments");
		// System.out.println("not null saved instance");
		// }

		if (fragmentName.isEmpty()
				|| fragmentName.equals(getResources().getString(
						R.string.name_fragment_home)))
		{
			// if (Fragments.get(getResources().getString(
			// R.string.name_fragment_home)) != null)
			// {
			// mContent = Fragments.get(getResources().getString(
			// R.string.name_fragment_home));
			// System.out.println("opening home from saved instance");
			// }
			// else
			{
				mContent = new HomeFragment();
				currentFragment = getResources().getString(
						R.string.name_fragment_home);
				// Fragments.put(
				// getResources().getString(R.string.name_fragment_home),
				// mContent);
			}
		}
		else if (fragmentName.equals(getResources().getString(
				R.string.name_fragment_search_nutrients)))
		{
			mContent = new NutrientListFragment();
			currentFragment = getResources().getString(
					R.string.name_fragment_search_nutrients);

			// Fragments.put(
			// getResources().getString(
			// R.string.name_fragment_search_nutrients), mContent);
		}
		else if (fragmentName.equals(getResources().getString(
				R.string.name_fragment_search_ingredients)))
		{
			mContent = new NutrientListFragment(); // CHANGE THIS
			currentFragment = getResources().getString(
					R.string.name_fragment_search_ingredients);
			// Fragments.put(
			// getResources().getString(
			// R.string.name_fragment_search_ingredients),
			// mContent);
		}
		else if (fragmentName.equals(getResources().getString(
				R.string.name_fragment_search_recipe_name)))
		{
			mContent = new NutrientListFragment();
			currentFragment = getResources().getString(
					R.string.name_fragment_search_recipe_name);
			// Fragments.put(
			// getResources().getString(
			// R.string.name_fragment_search_recipe_name),
			// mContent);
		}

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

	}

	protected void showSplashScreen()
	{
		if (firstDisplay == true)
		{
			mSplashDialog = new Dialog(this, R.style.SplashScreen);
			mSplashDialog.setContentView(R.layout.splashscreen);
			mSplashDialog.setCancelable(false);
			mSplashDialog.show();
			firstDisplay = false;

			// Set Runnable to remove splash screen just in case
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					removeSplashScreen();
				}
			}, 10000);
		}

		setTheme(R.style.AppTheme);
	}

	protected void removeSplashScreen()
	{
		if (mSplashDialog != null)
		{
			mSplashDialog.dismiss();
			mSplashDialog = null;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);

		// Bundle fragments = new Bundle();
		// fragments.putSerializable("Fragments", Fragments);
		// savedInstanceState.putBundle("Fragments", fragments);
		// System.out.println("Saving instance");
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
	}
}
