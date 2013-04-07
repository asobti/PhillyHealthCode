package com.vitaminme.recipe;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vitaminme.data.RecipeNutrient;
import com.vitaminme.main.R;

public class RecipeNutrientAdapter extends ArrayAdapter<RecipeNutrient>
{

	private Context context;
	private ArrayList<RecipeNutrient> nutrients;

	public RecipeNutrientAdapter(Context context, List<RecipeNutrient> nutrients)
	{

		super(context, R.layout.nutrition_facts_nutrients, nutrients);
		System.out.println("constructor for adapter");

		this.context = context;
		this.nutrients = (ArrayList<RecipeNutrient>) nutrients;

	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		System.out.println("nutrients " + nutrients.size());
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.nutrition_facts_nutrients, parent,
				false);

		TextView nutrient = (TextView) v.findViewById(R.id.nutrient);
		TextView nutQuant = (TextView) v.findViewById(R.id.nutrientQuant);
		TextView percent = (TextView) v.findViewById(R.id.percent);

		RecipeNutrient nut = nutrients.get(position);		
		nutrient.setText(nut.name);
		nutQuant.setText(nut.value + nut.unit.abbr);
		percent.setText(nut.getPercentVal());	

		return v;
	}
}
