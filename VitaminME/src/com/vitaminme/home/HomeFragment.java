package com.vitaminme.home;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vitaminme.main.ExpandableHeightGridView;
import com.vitaminme.main.R;

public class HomeFragment extends Fragment
{
	Home activity;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	String[] imageUrls = {
			"http://tabletpcssource.com/wp-content/uploads/2011/05/android-logo.png",
			"http://simpozia.com/pages/images/stories/windows-icon.png",
			"https://si0.twimg.com/profile_images/1135218951/gmail_profile_icon3_normal.png",
			"http://www.krify.net/wp-content/uploads/2011/09/Macromedia_Flash_dock_icon.png",
			"http://radiotray.sourceforge.net/radio.png",
			"http://www.bandwidthblog.com/wp-content/uploads/2011/11/twitter-logo.png",
			"http://weloveicons.s3.amazonaws.com/icons/100907_itunes1.png",
			"http://weloveicons.s3.amazonaws.com/icons/100929_applications.png",
			"http://www.idyllicmusic.com/index_files/get_apple-iphone.png",
			"http://www.frenchrevolutionfood.com/wp-content/uploads/2009/04/Twitter-Bird.png",
			"http://3.bp.blogspot.com/-ka5MiRGJ_S4/TdD9OoF6bmI/AAAAAAAAE8k/7ydKtptUtSg/s1600/Google_Sky%2BMaps_Android.png",
			"http://www.desiredsoft.com/images/icon_webhosting.png",
			"http://goodereader.com/apps/wp-content/uploads/downloads/thumbnails/2012/01/hi-256-0-99dda8c730196ab93c67f0659d5b8489abdeb977.png",
			"http://1.bp.blogspot.com/-mlaJ4p_3rBU/TdD9OWxN8II/AAAAAAAAE8U/xyynWwr3_4Q/s1600/antivitus_free.png",
			"http://cdn3.iconfinder.com/data/icons/transformers/computer.png",
			"http://cdn.geekwire.com/wp-content/uploads/2011/04/firefox.png?7794fe",
			"https://ssl.gstatic.com/android/market/com.rovio.angrybirdsseasons/hi-256-9-347dae230614238a639d21508ae492302340b2ba",
			"http://androidblaze.com/wp-content/uploads/2011/12/tablet-pc-256x256.jpg",
			"http://www.theblaze.com/wp-content/uploads/2011/08/Apple.png",
			"http://1.bp.blogspot.com/-y-HQwQ4Kuu0/TdD9_iKIY7I/AAAAAAAAE88/3G4xiclDZD0/s1600/Twitter_Android.png",
			"http://3.bp.blogspot.com/-nAf4IMJGpc8/TdD9OGNUHHI/AAAAAAAAE8E/VM9yU_lIgZ4/s1600/Adobe%2BReader_Android.png",
			"http://cdn.geekwire.com/wp-content/uploads/2011/05/oovoo-android.png?7794fe",
			"http://icons.iconarchive.com/icons/kocco/ndroid/128/android-market-2-icon.png",
			"http://thecustomizewindows.com/wp-content/uploads/2011/11/Nicest-Android-Live-Wallpapers.png",
			"http://c.wrzuta.pl/wm16596/a32f1a47002ab3a949afeb4f",
			"http://macprovid.vo.llnwd.net/o43/hub/media/1090/6882/01_headline_Muse.jpg",
			"http://tabletpcssource.com/wp-content/uploads/2011/05/android-logo.png",
			"http://simpozia.com/pages/images/stories/windows-icon.png",
			"https://si0.twimg.com/profile_images/1135218951/gmail_profile_icon3_normal.png",
			"http://www.krify.net/wp-content/uploads/2011/09/Macromedia_Flash_dock_icon.png",
			"http://radiotray.sourceforge.net/radio.png",
			"http://www.bandwidthblog.com/wp-content/uploads/2011/11/twitter-logo.png",
			"http://weloveicons.s3.amazonaws.com/icons/100907_itunes1.png",
			"http://weloveicons.s3.amazonaws.com/icons/100929_applications.png",
			"http://www.idyllicmusic.com/index_files/get_apple-iphone.png",
			"http://www.frenchrevolutionfood.com/wp-content/uploads/2009/04/Twitter-Bird.png",
			"http://3.bp.blogspot.com/-ka5MiRGJ_S4/TdD9OoF6bmI/AAAAAAAAE8k/7ydKtptUtSg/s1600/Google_Sky%2BMaps_Android.png",
			"http://www.desiredsoft.com/images/icon_webhosting.png",
			"http://goodereader.com/apps/wp-content/uploads/downloads/thumbnails/2012/01/hi-256-0-99dda8c730196ab93c67f0659d5b8489abdeb977.png",
			"http://1.bp.blogspot.com/-mlaJ4p_3rBU/TdD9OWxN8II/AAAAAAAAE8U/xyynWwr3_4Q/s1600/antivitus_free.png" };

	ArrayList<String> urls = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = (Home) getActivity();
		activity.setTitle(R.string.app_name);
		activity.helpMessage = "Home help";

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
		gv1.setAdapter(new ImageAdapter());

		Random rand = new Random();
		for (int i = 0; i < 9; i++)
		{
			urls.add(imageUrls[rand.nextInt(imageUrls.length)]);
		}

		ExpandableHeightGridView gv2 = (ExpandableHeightGridView) vg
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
			}
		});

		// activity.removeSplashScreen();
		new SplashScreen().execute();
		return vg;
	}

	private class SplashScreen extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				System.out.println("Start");
				Thread.sleep(5000);
				System.out.println("Finish");
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
			return "";
		}

		protected void onPreExecute()
		{

		}

		protected void onPostExecute(String str)
		{
			activity.removeSplashScreen();
		}
	}

	public class ImageAdapter extends BaseAdapter
	{
		private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

		@Override
		public int getCount()
		{
			return urls.size();
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
			final ImageView imageView;
			if (convertView == null)
			{
				imageView = (ImageView) activity.getLayoutInflater().inflate(
						R.layout.fragment_home_gridview_items, parent, false);
			}
			else
			{
				imageView = (ImageView) convertView;
			}

			imageLoader.displayImage(urls.get(position), imageView, options,
					animateFirstListener);

			return imageView;
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
}
