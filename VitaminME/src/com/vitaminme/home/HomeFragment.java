package com.vitaminme.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vitaminme.main.R;

public class HomeFragment extends Fragment
{
	Home activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = (Home) getActivity();
		activity.setTitle(R.string.app_name);
		activity.helpMessage = "Home help";
		
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.fragment_home,
				null);

		return vg;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

}
