package com.vitaminme.userprofiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vitaminme.main.BaseActivity;
import com.vitaminme.main.R;
import com.vitaminme.widgets.ExpandableHeightGridView;

public class Favorites extends BaseActivity
{
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	ArrayList<String> images = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle(R.string.title_activity_favorites);
		setContentView(R.layout.activity_favorites);

		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_stub)
				.showImageOnFail(R.drawable.ic_error).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this.getApplicationContext()).defaultDisplayImageOptions(
				options).build();
		ImageLoader.getInstance().init(config);

		for (int i = 0; i < 6; i++)
		{
			images.add("http://images6.fanpop.com/image/photos/33400000/YUMMY-FAST-FOOD-fast-food-33414496-1280-720.jpg");
		}

		ExpandableHeightGridView gv1 = (ExpandableHeightGridView) findViewById(R.id.gridView1);
		gv1.setExpanded(true);
		gv1.setEmptyView(findViewById(R.id.emptyFavorites));
		gv1.setAdapter(new ImageAdapter());
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

			imageLoader.displayImage(images.get(position), holder.image,
					options, animateFirstListener);

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

				}
			}
		}
	}

}
