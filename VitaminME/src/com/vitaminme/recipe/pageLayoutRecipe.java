package com.vitaminme.recipe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vitaminme.data.ImageSize;
import com.vitaminme.data.Recipe;
import com.vitaminme.main.R;

public class pageLayoutRecipe extends Fragment
{
	private Context context;
	Recipe recipe;
	private RecipeSelectedAdapter adapter;

	ImageView mainImage;

	public static Fragment newInstance(Context context)
	{
		pageLayoutRecipe f = new pageLayoutRecipe();

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
		ViewGroup vg = (ViewGroup) inflater.inflate(
				R.layout.recipe_page_layout, null);

		ListView list = (ListView) vg.findViewById(R.id.list);
		TextView recipeName = (TextView) vg.findViewById(R.id.textView1);
		TextView courseType = (TextView) vg.findViewById(R.id.courseType);
		TextView cookingTime = (TextView) vg.findViewById(R.id.cookingTime);
		mainImage = (ImageView) vg.findViewById(R.id.recipeImage);
		ImageView cookingTimeImage = (ImageView) vg
				.findViewById(R.id.clock_icon);
		ImageView courseTypeImage = (ImageView) vg
				.findViewById(R.id.plate_icon);

		recipeName.setSelected(true);

		adapter = new RecipeSelectedAdapter(getActivity(), recipe.ingredients);
		list.setAdapter(adapter);

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(list.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < adapter.getCount(); i++)
		{
			View listItem = adapter.getView(i, null, list);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += (listItem.getMeasuredHeight() * 1.1);
		}
		ViewGroup.LayoutParams params = list.getLayoutParams();
		params.height = totalHeight
				+ (list.getDividerHeight() * (list.getCount() + 1));
		list.setLayoutParams(params);
		list.requestLayout();

		// String recipe = "Burger and Fries";
		recipeName.setTextColor(Color.WHITE);
		recipeName.setText(recipe.name);

		String temp = "";
		for (String s : recipe.courses)
		{
			temp += s + ", ";
		}

		if (temp.isEmpty())
		{
			System.out.println("temp: " + temp);
			courseTypeImage.setVisibility(View.INVISIBLE);
			courseTypeImage.getLayoutParams().height = 0;
		}
		else
			courseType.setText(temp.substring(0, temp.length() - 2));

		courseType.setTextColor(Color.BLACK);
		// vg.findViewById(R.id.plate_icon).setVisibility(View.INVISIBLE);

		if (recipe.cookingTime.toString().isEmpty()){
			cookingTimeImage.setVisibility(View.INVISIBLE);
			cookingTimeImage.getLayoutParams().height = 0;
		}
		else
			cookingTime.setText(recipe.cookingTime.toString());
		cookingTime.setTextColor(Color.BLACK);

		mainImage.setImageResource(R.drawable.plate_icon);
		new GetImage().execute(recipe.images.get(ImageSize.LARGE));

		mainImage.setBackgroundColor(Color.BLACK);
		return vg;

	}

	private class GetImage extends AsyncTask<String, Void, Bitmap>
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
				mainImage.setImageResource(R.drawable.plate_icon);
			}
			return null;
		}

		protected void onPostExecute(Bitmap bm)
		{
			if (bm != null)
				mainImage.setImageBitmap(bm);
		}

	}
}
