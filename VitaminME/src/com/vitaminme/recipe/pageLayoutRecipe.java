package com.vitaminme.recipe;

import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
	private Vibrator vib;
	ImageView mainImage;

	public static Fragment newInstance(Context context)
	{
		pageLayoutRecipe f = new pageLayoutRecipe();
		return f;
	}

	public void constructor(Recipe recipe, Context context)
	{
		this.recipe = recipe;
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		ViewGroup vg = (ViewGroup) inflater.inflate(
				R.layout.recipe_page_layout, null);
		vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		ListView list = (ListView) vg.findViewById(R.id.list);
		TextView recipeName = (TextView) vg.findViewById(R.id.textView1);
		TextView courseType = (TextView) vg.findViewById(R.id.courseType);
		TextView cookingTime = (TextView) vg.findViewById(R.id.cookingTime);
		mainImage = (ImageView) vg.findViewById(R.id.recipeImage);
		ImageView cookingTimeImage = (ImageView) vg
				.findViewById(R.id.clock_icon);
		ImageView courseTypeImage = (ImageView) vg
				.findViewById(R.id.plate_icon);
		
		// add favorite icon unimplemented
		ImageButton favorite = (ImageButton) vg.findViewById(R.id.favorite_icon);
		favorite.setVisibility(View.INVISIBLE);
//		favorite.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//				vib.vibrate(20);
//				Toast.makeText(context, "Added to Favorites (not really)",
//							Toast.LENGTH_SHORT).show();
//			}
//		});
		
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

		// vg.findViewById(R.id.plate_icon).setVisibility(View.INVISIBLE);

		if (recipe.cookingTime.toString().isEmpty()){
			cookingTimeImage.setVisibility(View.INVISIBLE);
			cookingTimeImage.getLayoutParams().height = 0;
		}
		else
			cookingTime.setText(recipe.cookingTime.toString());

//		mainImage.setImageResource(R.drawable.plate_icon);
		new GetImage().execute(recipe.images.get(ImageSize.LARGE));

//		mainImage.setBackgroundColor(Color.BLACK);
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
			{
				mainImage.setImageBitmap(bm);
				mainImage.setScaleType(ScaleType.CENTER_CROP);
			}
		}

	}
}
