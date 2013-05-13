package com.vitaminme.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.vitaminme.main.R;

public class BaseActivity extends SlidingFragmentActivity
{
	public String helpMessage = "";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Set sidebar view
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new SidebarFragment()).commit();

		// Customize Sidebar
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeEnabled(true);
		sm.setFadeDegree(0.35f);
		sm.setBehindScrollScale(0.25f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setSlidingEnabled(true);
		setSlidingActionBarEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Set ActionBar
		final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		menu.add("Help").setIcon(R.drawable.info)
				.setOnMenuItemClickListener(new OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item)
					{
						dialog.setTitle("Help")
								.setMessage(helpMessage)
								.setIcon(R.drawable.info)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener()
										{
											public void onClick(
													DialogInterface dialog,
													int id)
											{

											}
										}).show();
						return false;
					}
				}).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		menu.add("About Yummly").setIcon(R.drawable.info)
				.setOnMenuItemClickListener(new OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item)
					{
						LayoutInflater inflater = getLayoutInflater();
						ViewGroup vg = new RelativeLayout(getBaseContext());
						View view = inflater.inflate(R.layout.about_yummly, vg);

						Typeface tf = Typeface.createFromAsset(getAssets(),
								"fonts/Lato-Bold.ttf");
						Typeface tf2 = Typeface.createFromAsset(getAssets(),
								"fonts/Lato-Regular.ttf");

						TextView tv1 = (TextView) view
								.findViewById(R.id.aboutVitaminText);
						tv1.setTypeface(tf);
						TextView tv2 = (TextView) view
								.findViewById(R.id.aboutYummlyText);
						tv2.setTypeface(tf2);
						TextView tv3 = (TextView) view
								.findViewById(R.id.aboutYummlyLink);
						tv3.setTypeface(tf2);

						dialog.setView(view)
								.setIcon(R.drawable.info)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener()
										{
											public void onClick(
													DialogInterface dialog,
													int id)
											{

											}
										}).show();
						return false;
					}
				}).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
