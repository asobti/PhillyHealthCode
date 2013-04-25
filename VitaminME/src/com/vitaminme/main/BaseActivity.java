package com.vitaminme.main;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.view.MenuItem;

@SuppressWarnings("unused")
public class BaseActivity extends SlidingFragmentActivity
{
	private int mTitleRes;
	protected ListFragment mFrag;

//	public BaseActivity(int titleRes)
//	{
//		mTitleRes = titleRes;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setTitle(mTitleRes);

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
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
