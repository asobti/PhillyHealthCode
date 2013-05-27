package com.vitaminme.widgets;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.fima.cardsui.R;
import com.fima.cardsui.Utils;

public abstract class Unused_ImageCard
{
	protected String title;
	protected int image;
	protected String desc;
	protected String imageURL;

	public interface OnCardSwiped
	{
		public void onCardSwiped(Unused_ImageCard unused_ImageCard, View layout);
	}

	private OnCardSwiped onCardSwipedListener;
	private OnClickListener mListener;
	protected View mCardLayout;

	public Unused_ImageCard()
	{

	}

	public Unused_ImageCard(String title)
	{
		this.title = title;
	}

	public Unused_ImageCard(String title, int image)
	{
		this.title = title;
		this.image = image;
	}

	public Unused_ImageCard(String title, String imageURL)
	{
		this.title = title;
		this.imageURL = imageURL;
	}

	public Unused_ImageCard(String title, String desc, int image)
	{
		this.title = title;
		this.desc = desc;
		this.image = image;
	}

	public View getView(Context context, boolean swipable)
	{
		return getView(context, false);
	}

	public View getView(Context context)
	{

		View view = LayoutInflater.from(context).inflate(getCardLayout(), null);

		mCardLayout = view;

		try
		{
			((FrameLayout) view.findViewById(R.id.cardContent))
					.addView(getCardContent(context));
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}

		// ((TextView) view.findViewById(R.id.title)).setText(this.title);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		int bottom = Utils.convertDpToPixelInt(context, 12);
		lp.setMargins(0, 0, 0, bottom);

		view.setLayoutParams(lp);

		return view;
	}

	public View getViewLast(Context context)
	{

		View view = LayoutInflater.from(context).inflate(getLastCardLayout(),
				null);

		mCardLayout = view;

		try
		{
			((FrameLayout) view.findViewById(R.id.cardContent))
					.addView(getCardContent(context));
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}

		// ((TextView) view.findViewById(R.id.title)).setText(this.title);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		int bottom = Utils.convertDpToPixelInt(context, 12);
		lp.setMargins(0, 0, 0, bottom);

		view.setLayoutParams(lp);

		return view;
	}

	public View getViewFirst(Context context)
	{

		View view = LayoutInflater.from(context).inflate(getFirstCardLayout(),
				null);

		mCardLayout = view;

		try
		{
			((FrameLayout) view.findViewById(R.id.cardContent))
					.addView(getCardContent(context));
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
		}

		// ((TextView) view.findViewById(R.id.title)).setText(this.title);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		int bottom = Utils.convertDpToPixelInt(context, 12);
		lp.setMargins(0, 0, 0, bottom);

		view.setLayoutParams(lp);

		return view;
	}

	public abstract View getCardContent(Context context);

	public OnClickListener getClickListener()
	{
		return mListener;
	}

	public void setOnClickListener(OnClickListener listener)
	{
		mListener = listener;
	}

	public void OnSwipeCard()
	{
		if (onCardSwipedListener != null)
			onCardSwipedListener.onCardSwiped(this, mCardLayout);
		// TODO: find better implementation to get card-object's used content
		// layout (=> implementing getCardContent());
	}

	public OnCardSwiped getOnCardSwipedListener()
	{
		return onCardSwipedListener;
	}

	public void setOnCardSwipedListener(OnCardSwiped onEpisodeSwipedListener)
	{
		this.onCardSwipedListener = onEpisodeSwipedListener;
	}

	protected int getCardLayout()
	{
		return R.layout.item_card_empty;
	}

	protected int getLastCardLayout()
	{
		return R.layout.item_card_empty_last;
	}

	protected int getFirstCardLayout()
	{
		return R.layout.item_card_empty_first;
	}

	public String getTitle()
	{
		return title;
	}

	public String getDesc()
	{
		return desc;
	}

	public int getImage()
	{
		return image;
	}

	public Bitmap getImageBitmap(String imageURL)
	{
		try
		{
			URL url = new URL(
					"http://vafoodbanks.org/wp-content/uploads/2012/06/fresh_food.jpg");
			return BitmapFactory.decodeStream(url.openConnection()
					.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private class GetImageAsync extends AsyncTask<String, Void, Bitmap>
	{
		protected Bitmap doInBackground(String... params)
		{
			try
			{
				URL url = new URL(params[0]);
				return BitmapFactory.decodeStream(url.openConnection()
						.getInputStream());
			}
			catch (Exception e)
			{
				Log.e("vitaminme", "ERROR in AsyncTask: " + e.toString());
				e.printStackTrace();
				// image.setImageResource(R.drawable.ic_launcher);
			}
			return null;
		}

		protected void onPostExecute(Bitmap bm)
		{
			if (bm != null)
			{
//				image.setImageBitmap(bm);
//				image.setScaleType(ScaleType.CENTER_CROP);
			}
		}

	}
}
