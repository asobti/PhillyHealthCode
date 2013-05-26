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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Recipe;
import com.vitaminme.data.RecipeNutrient;
import com.vitaminme.android.R;

public class pageLayoutNutrition extends Fragment
{

	Recipe recipe;
	Context context;
//	private RecipeNutrientAdapter adapter;

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
		
		if(recipe.getEnergy() == null){
			
			ViewGroup vg = (ViewGroup) inflater.inflate(
					R.layout.nutrition_page_empty_layout, null);
			return vg;
		}
		else{
			ViewGroup vg = (ViewGroup) inflater.inflate(
					R.layout.nutrition_page_layout, null);

			TextView title = (TextView) vg.findViewById(R.id.nut_facts_header);
			TextView calories = (TextView) vg.findViewById(R.id.calories);
			calories.setSelected(true);
			TextView servingPer = (TextView) vg.findViewById(R.id.servings_per);
			TextView caloriesFromFat = (TextView) vg
					.findViewById(R.id.calories_from_fat);
			
			calories.setText("Calories " + recipe.getEnergy().value);
			
		if (recipe.getFatNutrient() != null)
		{
			caloriesFromFat.setText("Calories from Fat "
					+ (int) recipe.getFatNutrient().value * 9);
		}
		else
			caloriesFromFat.setText("Calories from Fat NA");

		servingPer.setText("Servings per Recipe : " + recipe.servingSize);
		List<RecipeNutrient> nutrients = recipe.nutrients;
		nutrients.add(new RecipeNutrient(""));
		
		
		LinearLayout listView = (LinearLayout) vg.findViewById(R.id.nutrient_list);
		ListAdapter adapter = new RecipeNutrientAdapter(context, nutrients);
		for (int i = 0; i < adapter.getCount(); i++) {
			  View item = adapter.getView(i, null, null);
			  listView.addView(item);
			}
		

		listView.requestLayout();
		title.setFocusableInTouchMode(true);
		title.requestFocus();
		
		return vg;
		}
		
		

	
	}

}
