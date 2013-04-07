package com.vitaminme.recipe;

import java.util.ArrayList;
import java.util.List;

import com.vitaminme.data.Recipe;
import com.vitaminme.data.RecipeNutrient;
import com.vitaminme.main.NutrientListAdapter;
import com.vitaminme.main.R;
import com.vitaminme.recipe.pageLayoutNutrition;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class pageLayoutNutrition extends Fragment
{

	Recipe recipe;
	Context context;

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

		View border = (View) vg.findViewById(R.id.border);
		TextView calories = (TextView) vg.findViewById(R.id.calories);
		calories.setSelected(true);
		// TextView caloriesFromFat = (TextView)
		// vg.findViewById((R.id.calories_from_fat);
		TextView servingSize = (TextView) vg.findViewById(R.id.serving_size);
		TextView servingPer = (TextView) vg.findViewById(R.id.servings_per);

		if (recipe.energy != null)
			calories.setText("Calories " + recipe.energy.value);
		else
			calories.setText("Calories NA");
		servingSize.setText("Serving Size : 1 Portion");
		servingPer.setText("Servings per Recipe : " + recipe.servingSize);

		List<RecipeNutrient> nutrients = recipe.nutrients;

		ListView listView = (ListView) vg.findViewById(R.id.nutrient_list);
		RecipeNutrientAdapter adapter = new RecipeNutrientAdapter(context,
				nutrients);
		listView.setAdapter(adapter);
		
		int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0; i < adapter.getCount(); i++)
        {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += (listItem.getMeasuredHeight());
        }
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
		
		
		System.out.println("after setadapter");

		// TextView title = (TextView) vg.findViewById(R.id.textView1);
		// TextView data = (TextView) vg.findViewById(R.id.textView2);
		//
		// title.setText("Nutritional Facts");
		// data.setText("data");
		//
		// title.setTextColor(Color.LTGRAY);
		// data.setTextColor(Color.WHITE);
		// vg.setBackgroundColor(Color.BLACK);
		return vg;

	}

}
