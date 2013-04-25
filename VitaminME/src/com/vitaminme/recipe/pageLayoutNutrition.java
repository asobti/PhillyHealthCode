package com.vitaminme.recipe;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vitaminme.data.Recipe;
import com.vitaminme.data.RecipeNutrient;
import com.vitaminme.main.R;

public class pageLayoutNutrition extends Fragment
{

	Recipe recipe;
	Context context;
	private RecipeNutrientAdapter adapter;

	public static Fragment newInstance(Context context)
	{

		pageLayoutNutrition f = new pageLayoutNutrition();
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
				R.layout.nutrition_page_layout, null);

		TextView title = (TextView) vg.findViewById(R.id.nut_facts_header);
		TextView calories = (TextView) vg.findViewById(R.id.calories);
		calories.setSelected(true);
		TextView servingPer = (TextView) vg.findViewById(R.id.servings_per);
		TextView caloriesFromFat = (TextView) vg
				.findViewById(R.id.calories_from_fat);

		if (recipe.energy != null)
			calories.setText("Calories " + recipe.energy.value);
		else
			calories.setText("Calories NA");
		if (recipe.getFatNutrient() != null)
		{
			caloriesFromFat.setText("Calories from Fat "
					+ (int) recipe.getFatNutrient().value * 9);
		}
		else
			caloriesFromFat.setText("Calories from Fat NA");

		servingPer.setText("Servings per Recipe : " + recipe.servingSize);

		List<RecipeNutrient> nutrients = recipe.nutrients;
		ListView listView = (ListView) vg.findViewById(R.id.nutrient_list);
		adapter = new RecipeNutrientAdapter(context, nutrients);
		listView.setAdapter(adapter);

		int totalHeight = 0;
		MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		Log.v("mytag", "adapter view count : " + adapter.getCount());
		for (int i = 0; i < adapter.getCount(); i++)
		{
			// View listItem = adapter.getView(i, null, listView);
			// listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			// Log.v("mytag", "measured height " +
			// listItem.getMeasuredHeight());
			totalHeight += 30;
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight;
		listView.setLayoutParams(params);
		listView.requestLayout();
		title.setFocusableInTouchMode(true);
		title.requestFocus();

		return vg;

	}

}
