package com.vitaminme.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vitaminme.main.R;
import com.vitaminme.userprofiles.Favorites;
import com.vitaminme.widgets.ExpandableHeightGridView;

public class HomeFragment extends Fragment
{
	static Home activity;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	static int imageCounter;
	static int num_images;

	ArrayList<String> images = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = (Home) getActivity();
		activity.setTitle(R.string.app_name);
		activity.helpMessage = "Home help";
		imageCounter = 0;
		num_images = 0;

		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.fragment_home,
				null);

		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showStubImage(R.drawable.ic_launcher_vm_2)
				.showImageForEmptyUri(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				activity.getApplicationContext()).defaultDisplayImageOptions(
				options).build();
		ImageLoader.getInstance().init(config);

		ExpandableHeightGridView gv1 = (ExpandableHeightGridView) vg
				.findViewById(R.id.gridView1);
		gv1.setExpanded(true);
		gv1.setEmptyView(vg.findViewById(R.id.emptyFavorites));
		// gv1.setAdapter(new ImageAdapter());

		for (int i = 0; i < 6; i++)
		{
			images.add("http://vafoodbanks.org/wp-content/uploads/2012/06/fresh_food.jpg");
		}
		num_images += images.size();

		final ExpandableHeightGridView gv2 = (ExpandableHeightGridView) vg
				.findViewById(R.id.gridView2);
		gv2.setExpanded(true);
		gv2.setEmptyView(vg.findViewById(R.id.emptyRecent));
		gv2.setAdapter(new ImageAdapter());

		final Vibrator vibe = (Vibrator) getActivity().getSystemService(
				Context.VIBRATOR_SERVICE);
		gv1.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				vibe.vibrate(20);
				Toast.makeText(getActivity(), "Clicked " + position,
						Toast.LENGTH_LONG).show();
				if (position == (images.size() - 1))
				{
					Intent intent = new Intent(activity, Favorites.class);
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
				Toast.makeText(getActivity(), "Clicked " + position,
						Toast.LENGTH_LONG).show();
				if (position == (images.size() - 1))
				{
					Intent intent = new Intent(activity, Favorites.class);
					startActivity(intent);
				}
			}
		});

		return vg;
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
				view = activity.getLayoutInflater().inflate(
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
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
}
