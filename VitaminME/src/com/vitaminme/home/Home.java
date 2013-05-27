package com.vitaminme.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Window;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vitaminme.android.BaseActivity;
import com.vitaminme.android.R;
import com.vitaminme.services.UpdateNutrientsDB;
import com.vitaminme.userprofiles.Favorites;
import com.vitaminme.widgets.ExpandableHeightGridView;

public class Home extends BaseActivity
{
	protected Dialog mSplashDialog;
	static boolean firstDisplay = true;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	static int imageCounter;
	static int num_images;

	ArrayList<String> images = new ArrayList<String>();

	static Home activity;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		showSplashScreen();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home);

		performChecks();

		activity = this;
		this.helpMessage = "Home help";

		imageCounter = 0;
		num_images = 0;

		ExpandableHeightGridView gv1 = (ExpandableHeightGridView) findViewById(R.id.gridView1);
		gv1.setExpanded(true);
		gv1.setEmptyView(findViewById(R.id.emptyFavorites));
		// gv1.setAdapter(new ImageAdapter());

		for (int i = 0; i < 6; i++)
		{
			images.add("http://vafoodbanks.org/wp-content/uploads/2012/06/fresh_food.jpg");
		}
		num_images += images.size();

		final ExpandableHeightGridView gv2 = (ExpandableHeightGridView) findViewById(R.id.gridView2);
		gv2.setExpanded(true);
		gv2.setEmptyView(findViewById(R.id.emptyRecent));
		gv2.setAdapter(new ImageAdapter());

		final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		gv1.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				vibe.vibrate(20);
				Toast.makeText(getBaseContext(), "Clicked " + position,
						Toast.LENGTH_LONG).show();
				if (position == (images.size() - 1))
				{
					Intent intent = new Intent(getBaseContext(),
							Favorites.class);
					startActivity(intent);
				}
			}
		});
		gv2.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				vibe.vibrate(20);
				Toast.makeText(getBaseContext(), "Clicked " + position,
						Toast.LENGTH_LONG).show();
				if (position == (images.size() - 1))
				{
					Intent intent = new Intent(getBaseContext(),
							Favorites.class);
					startActivity(intent);
				}
			}
		});

	}

	private void performChecks()
	{
		// Initialize global objects
		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(options)
				.build();
		ImageLoader.getInstance().init(config);

		// Update NutrientsDB
		Intent intent = new Intent(this, UpdateNutrientsDB.class);
		startService(intent);
	}

	protected void showSplashScreen()
	{
		if (firstDisplay == true)
		{
			mSplashDialog = new Dialog(this, R.style.SplashScreen);
			mSplashDialog.setContentView(R.layout.splashscreen);
			Typeface tf = Typeface.createFromAsset(getAssets(),
					"fonts/Lato-Bold.ttf");
			TextView tv1 = (TextView) mSplashDialog
					.findViewById(R.id.vitaminText);
			tv1.setTypeface(tf);
			TextView tv2 = (TextView) mSplashDialog
					.findViewById(R.id.yummlyText);
			tv2.setTypeface(tf);
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

	public class ImageAdapter extends BaseAdapter
	{
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		private class ViewHolder
		{
			public TextView text1;
			public ImageView image;
		}

		@Override
		public int getCount()
		{
			return images.size();
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null)
			{
				view = getLayoutInflater().inflate(
						R.layout.fragment_home_gridview_items, parent, false);
				holder = new ViewHolder();
				holder.text1 = (TextView) view.findViewById(R.id.textHome);
				holder.image = (ImageView) view.findViewById(R.id.imageHome);
				view.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) view.getTag();
			}

			if (position == (images.size() - 1))
			{
				imageLoader.displayImage("drawable://" + R.drawable.play,
						holder.image, options, animateFirstListener);
				holder.text1.setText("See all...");
			}
			else
			{
				imageLoader.displayImage(images.get(position), holder.image,
						options, animateFirstListener);
			}

			return view;
		}
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener
	{
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage)
		{
			if (loadedImage != null)
			{
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay)
				{
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);

					// Remove splash screen when done loading all images
					imageCounter++;
					if (imageCounter >= num_images - 1)
					{
						activity.removeSplashScreen();
					}
				}
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
	}
}
