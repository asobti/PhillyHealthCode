package com.vitaminme.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.vitaminme.home.Home;
import com.vitaminme.home.HomeFragment;
import com.vitaminme.home.IngredientListFragment;
import com.vitaminme.home.NutrientListFragment;
import com.vitaminme.android.R;
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

		RelativeLayout searchByNutrients = (RelativeLayout) vg
				.findViewById(R.id.searchByNutrients);
		searchByNutrients.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vibe.vibrate(20);
				switchFragment("Nutrients");
			}
		});

		RelativeLayout searchByIngredients = (RelativeLayout) vg
				.findViewById(R.id.searchByIngredients);
		searchByIngredients.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vibe.vibrate(20);
				switchFragment("Ingredients");
			}
		});

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

		if (fragmentItemName.equalsIgnoreCase("Home"))
		{
			if (activity instanceof Home)
			{
				if (((Home) activity).currentFragment.equals(getResources()
						.getString(R.string.name_fragment_home)))
				{
					closeSidebar(activity);
				}
				else
				{
					((Home) activity).currentFragment = getResources()
							.getString(R.string.name_fragment_home);
					((FragmentActivity) activity).getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.content_frame, new HomeFragment())
							.commit();

					closeSidebar(activity);
				}
			}
			else
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, Home.class);
				intent.putExtra("fragmentName",
						getResources().getString(R.string.name_fragment_home));
				activity.startActivity(intent);
			}

			setSelector(R.id.selectHome);
		}
		else if (fragmentItemName.equals("Nutrients"))
		{
			if (activity instanceof Home)
			{
				if (((Home) activity).currentFragment.equals(getResources()
						.getString(R.string.name_fragment_search_nutrients)))

				{

					closeSidebar(activity);
				}
				else
				{
					((Home) activity).currentFragment = getResources()
							.getString(R.string.name_fragment_search_nutrients);
					((FragmentActivity) activity)
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.content_frame,
									new NutrientListFragment()).commit();
					closeSidebar(activity);
				}
			}
			else
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, Home.class);
				intent.putExtra(
						"fragmentName",
						getResources().getString(
								R.string.name_fragment_search_nutrients));
				activity.startActivity(intent);
			}

			setSelector(R.id.selectNutrients);
		}
		else if (fragmentItemName.equals("Ingredients"))
		{
			if (activity instanceof Home)
			{
				if (activity
						.getTitle()
						.toString()
						.equals(getResources().getString(
								R.string.name_fragment_search_ingredients)))
				{

					closeSidebar(activity);
				}
				else
				{
					((Home) activity).currentFragment = getResources()
							.getString(
									R.string.name_fragment_search_ingredients);
					((FragmentActivity) activity)
							.getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.content_frame,
									new IngredientListFragment()).commit();
					closeSidebar(activity);
				}
			}
			else
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, Home.class);
				intent.putExtra(
						"fragmentName",
						getResources().getString(
								R.string.name_fragment_search_ingredients));
				activity.startActivity(intent);
			}

			setSelector(R.id.selectIngredients);
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

	private void setSelector(int selector)
	{
		vg.findViewById(R.id.selectHome).setVisibility(View.INVISIBLE);
		vg.findViewById(R.id.selectNutrients).setVisibility(View.INVISIBLE);
		vg.findViewById(R.id.selectIngredients).setVisibility(View.INVISIBLE);
		vg.findViewById(R.id.selectRecipeNames).setVisibility(View.INVISIBLE);
		vg.findViewById(R.id.selectFavorites).setVisibility(View.INVISIBLE);
		vg.findViewById(R.id.selectUserProfile).setVisibility(View.INVISIBLE);
		vg.findViewById(selector).setVisibility(View.VISIBLE);
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
