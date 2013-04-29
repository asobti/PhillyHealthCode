package com.vitaminme.recipe;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vitaminme.data.Recipe;
import com.vitaminme.main.R;

public class pageLayoutCook extends Fragment
{
	ProgressBar Pbar;
	TextView loadText;
	View box;
	Recipe recipe;

	public static Fragment newInstance(Context context)
	{
		pageLayoutCook f = new pageLayoutCook();
		return f;
	}

	public void constructor(Recipe recipe)
	{
		this.recipe = recipe;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.cook_page_layout,
				null);

		loadText = (TextView) vg.findViewById(R.id.loadingText);
		Pbar = (ProgressBar) vg.findViewById(R.id.pB1);
		box = (View) vg.findViewById(R.id.view1);
		Pbar.bringToFront();
		loadText.bringToFront();

		WebView wv = (WebView) vg.findViewById(R.id.webView1);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setVisibility(View.VISIBLE);
		wv.requestFocus(View.FOCUS_DOWN);

		wv.setWebChromeClient(new WebChromeClient()
		{
			public void onProgressChanged(WebView view, int progress)
			{
				if (progress < 100 && Pbar.getVisibility() == ProgressBar.GONE)
				{
					box.setVisibility(View.VISIBLE);
					Pbar.setVisibility(ProgressBar.VISIBLE);
					loadText.setVisibility(View.VISIBLE);

				}
				Pbar.setProgress(progress);
				if (progress == 100)
				{
					box.setVisibility(View.GONE);
					Pbar.setVisibility(ProgressBar.GONE);
					loadText.setVisibility(View.GONE);

				}
			}

		});

		wv.getSettings().setBuiltInZoomControls(true);
		wv.getSettings().setDisplayZoomControls(false);
		wv.getSettings().setSupportZoom(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wv.getSettings().setAllowFileAccess(true);
		wv.getSettings().setDomStorageEnabled(true);
		wv.setWebViewClient(new WebViewClient());

		wv.loadUrl(recipe.source.sourceUrl);

		vg.setBackgroundColor(Color.BLACK);
		return vg;

	}
}