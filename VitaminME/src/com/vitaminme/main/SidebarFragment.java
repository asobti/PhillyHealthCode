package com.vitaminme.main;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.vitaminme.database.TestDB;
import com.vitaminme.home.Home;
import com.vitaminme.home.HomeFragment;
import com.vitaminme.home.NutrientListFragment;
import com.vitaminme.test.SearchBar;
import com.vitaminme.userprofiles.Favorites;
import com.vitaminme.userprofiles.UserProfile;

public class SidebarFragment extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.sidebar, null);

		final Vibrator vibe = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);

		ListView listView0 = (ListView) vg.findViewById(R.id.listView0);
		listView0.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				vibe.vibrate(20);
				// String itemName =
				// parent.getItemAtPosition(position).toString();
				switchFragment(parent.getItemAtPosition(position).toString());
			}
		});

		ListView listView1 = (ListView) vg.findViewById(R.id.listView1);
		listView1.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				vibe.vibrate(20);
				// String itemName =
				// parent.getItemAtPosition(position).toString();
				switchFragment(parent.getItemAtPosition(position).toString());
			}
		});

		ListView listView2 = (ListView) vg.findViewById(R.id.listView2);
		listView2.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				vibe.vibrate(20);
				String itemName = parent.getItemAtPosition(position).toString();
				switchFragment(parent.getItemAtPosition(position).toString());
			}
		});

		return vg;
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
		else if (fragmentItemName.equals("Temp:SearchBar"))
		{
			if (!(activity instanceof SearchBar))
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, SearchBar.class);
				activity.startActivity(intent);
			}
			else
			{
				closeSidebar(activity);
			}
		}
		else if (fragmentItemName.equals("Temp:TestDB"))
		{
			if (!(activity instanceof TestDB))
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, TestDB.class);
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
