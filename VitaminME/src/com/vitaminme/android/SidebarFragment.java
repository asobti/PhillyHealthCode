package com.vitaminme.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.vitaminme.home.Home;
import com.vitaminme.search.SearchRecipes;
import com.vitaminme.userprofiles.Favorites;
import com.vitaminme.userprofiles.UserProfile;

public class SidebarFragment extends Fragment
{
	ViewGroup vg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		vg = (ViewGroup) inflater.inflate(R.layout.sidebar, null);

		setListeners(vg);
		setSelectorOnLoad(vg);

		return vg;
	}

	private void setListeners(ViewGroup vg)
	{
		final Vibrator vibe = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);

		RelativeLayout home = (RelativeLayout) vg.findViewById(R.id.home);
		home.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vibe.vibrate(20);
				switchFragment("Home");
			}
		});

		// RelativeLayout searchByNutrients = (RelativeLayout) vg
		// .findViewById(R.id.searchByNutrients);
		// searchByNutrients.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// vibe.vibrate(20);
		// switchFragment("Nutrients");
		// }
		// });
		//
		// RelativeLayout searchByIngredients = (RelativeLayout) vg
		// .findViewById(R.id.searchByIngredients);
		// searchByIngredients.setOnClickListener(new OnClickListener()
		// {
		//
		// @Override
		// public void onClick(View v)
		// {
		// vibe.vibrate(20);
		// switchFragment("Ingredients");
		// }
		// });

		RelativeLayout searchByRecipeNames = (RelativeLayout) vg
				.findViewById(R.id.searchByRecipeNames);
		searchByRecipeNames.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vibe.vibrate(20);
				switchFragment("RecipeNames");
			}
		});

		RelativeLayout searchRecipes = (RelativeLayout) vg
				.findViewById(R.id.searchRecipes);
		searchRecipes.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				vibe.vibrate(20);
				switchFragment("SearchRecipes");
			}

		});

		RelativeLayout favorites = (RelativeLayout) vg
				.findViewById(R.id.favorites);
		favorites.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vibe.vibrate(20);
				switchFragment("Favorites");
			}
		});

		RelativeLayout userProfile = (RelativeLayout) vg
				.findViewById(R.id.userProfile);
		userProfile.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vibe.vibrate(20);
				switchFragment("User Profile");
			}
		});

	}

	private void setSelectorOnLoad(ViewGroup vg)
	{
		final Activity activity = getActivity();

		if (activity instanceof Home)
		{
			vg.findViewById(R.id.selectHome).setVisibility(View.VISIBLE);
		}
		else if (activity instanceof Favorites)
		{
			vg.findViewById(R.id.selectFavorites).setVisibility(View.VISIBLE);
		}
		else if (activity instanceof UserProfile)
		{
			vg.findViewById(R.id.selectUserProfile).setVisibility(View.VISIBLE);
		}

	}

	// Switch fragment or call new intents
	private void switchFragment(String fragmentItemName)
	{
		if (getActivity() == null)
			return;

		final Activity activity = getActivity();

		if (fragmentItemName.equals("Home"))
		{
			if (!(activity instanceof Home))
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, Home.class);
				activity.startActivity(intent);
			}
			else
			{
				closeSidebar(activity);
			}
		}
		else if (fragmentItemName.equals("SearchRecipes"))
		{
			if (!(activity instanceof SearchRecipes))
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, SearchRecipes.class);
				activity.startActivity(intent);
			}
			else
			{
				closeSidebar(activity);
			}
		}

		else if (fragmentItemName.equals("Favorites"))
		{
			if (!(activity instanceof Favorites))
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, Favorites.class);
				activity.startActivity(intent);
			}
			else
			{
				closeSidebar(activity);
			}
		}
		else if (fragmentItemName.equals("User Profile"))
		{
			if (!(activity instanceof UserProfile))
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, UserProfile.class);
				activity.startActivity(intent);
			}
			else
			{
				closeSidebar(activity);
			}
		}
	}

	private void closeSidebar(final Activity activity)
	{
		Handler h = new Handler();
		h.postDelayed(new Runnable()
		{
			public void run()
			{
				((SlidingFragmentActivity) activity).getSlidingMenu()
						.showContent();
			}
		}, 50);
	}

}
