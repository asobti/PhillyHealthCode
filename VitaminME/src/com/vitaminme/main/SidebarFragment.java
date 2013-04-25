package com.vitaminme.main;

import com.slidingmenu.lib.app.SlidingFragmentActivity;

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
				Toast.makeText(getActivity(), itemName, Toast.LENGTH_LONG)
						.show();

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
				System.out.println("Clicked: " + position);
				String itemName = parent.getItemAtPosition(position).toString();
				Toast.makeText(getActivity(), itemName, Toast.LENGTH_LONG)
						.show();

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
				System.out.println("Clicked: " + position);
				Toast.makeText(getActivity(),
						parent.getItemAtPosition(position).toString(),
						Toast.LENGTH_LONG).show();
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
	private void switchFragment(String fragmentName)
	{
		if (getActivity() == null)
			return;

		final Activity activity = getActivity();

		if (fragmentName.equalsIgnoreCase("home"))
		{
			if (activity instanceof Home)
			{
				activity.setTitle("Home");
				((FragmentActivity) activity).getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.content_frame, new HomeFragment())
						.commit();

				closeSidebar(activity);
			}
			else
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, Home.class);
				intent.putExtra("fragmentName", "home");
				activity.startActivity(intent);
			}
		}
		else if (fragmentName.equalsIgnoreCase("nutrients"))
		{
			if (activity instanceof Home)
			{
				activity.setTitle("Nutrient List");
				((FragmentActivity) activity)
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.content_frame, new NutrientListFragment())
						.commit();
				closeSidebar(activity);
			}
			else
			{
				closeSidebar(activity);

				Intent intent = new Intent(activity, Home.class);
				intent.putExtra("fragmentName", "nutrients");
				activity.startActivity(intent);
			}
		}
		else if (fragmentName.equalsIgnoreCase("favorites"))
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
