package com.vitaminme.widgets;

import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.fima.cardsui.objects.Card;
import com.vitaminme.android.R;

public class ImageCard extends Card
{
	String imageURL;
	ImageView image;

	public ImageCard(String title, String imageURL)
	{
		super(title);
		this.imageURL = imageURL;
	}

	@Override
	public View getCardContent(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.card_picture,
				null);

		((TextView) view.findViewById(R.id.title)).setText(title);
		System.out.println("Getting image");
		image = ((ImageView) view.findViewById(R.id.imageView1));
		image.setTag(imageURL);
		image.setImageResource(R.drawable.yummly);
//		new GetImage().execute(image);

		return view;
	}

	private class GetImage extends AsyncTask<ImageView, Void, Bitmap>
	{
		ImageView image = null;

		protected Bitmap doInBackground(ImageView... params)
		{
			try
			{
				this.image = params[0];
				URL url = new URL(image.getTag().toString());
				System.out.println("card background return");
				// return BitmapFactory.decodeStream(url.openConnection()
				// .getInputStream());
			}
			catch (Exception e)
			{
				System.out.println("ERROR in ImageCard AsyncTask: "
						+ e.getMessage());
			}
			return null;
		}

		protected void onPostExecute(Bitmap bm)
		{
			System.out.println("card onpost");
			if (bm == null)
				image.setImageResource(R.drawable.ic_launcher);
			else
			{
				System.out.println("card onpost image not null");
				// image.setImageBitmap(bm);
				image.setImageResource(R.drawable.yummly);
				image.setScaleType(ScaleType.CENTER_CROP);
			}
		}
	}
}
