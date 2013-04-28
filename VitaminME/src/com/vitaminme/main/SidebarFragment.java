package com.vitaminme.main;

import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.vitaminme.userprofiles.UserProfile;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SidebarFragment extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.sidebar, null);

		final Vibrator vibe = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);

		final TextView homeText = (TextView) vg.findViewById(R.id.home);
		homeText.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				vibe.vibrate(20);
				String itemName = homeText.getText().toString();
//				Toast.makeText(getActivity(), itemName, Toast.LENGTH_LONG)
//						.show();

				switchFragment(itemName);
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
				// System.out.println("Clicked: " + position);
				String itemName = parent.getItemAtPosition(position).toString();
				// Toast.makeText(getActivity(), itemName, Toast.LENGTH_LONG)
				// .show();

				switchFragment(itemName);
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
				// System.out.println("Clicked: " + position);
				// Toast.makeText(getActivity(),
				// parent.getItemAtPosition(position).toString(),
				// Toast.LENGTH_LONG).show();
				String itemName = parent.getItemAtPosition(position).toString();
				switchFragment(itemName);
			}
		});

		return vg;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

	// Switch fragment or call new intents
	private void switchFragment(String fragmentItemName)
	{
		if (getActivity() == null)
			return;

		final Activity activity = getActivity();

		if (fragmentItemName.equals("Home"))
		{
			if (activity instanceof Home)
			{
				if (activity
						.getTitle()
						.toString()
						.equals(getResources().getString(
								R.string.title_fragment_home)))
				{

					closeSidebar(activity);
				}
				else
				{
					// activity.setTitle("Home");
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
				if (activity
						.getTitle()
						.toString()
						.equals(getResources().getString(
								R.string.name_fragment_search_nutrients)))
				{

					closeSidebar(activity);
				}
				else
				{
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
