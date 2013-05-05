package com.vitaminme.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recipe
{
	public String id;
	public String name;

	public HashMap<ImageSize, String> images = new HashMap<ImageSize, String>();

	public List<String> courses;
	public TimeSpan cookingTime;
	public List<String> ingredients = new ArrayList<String>();
	public RecipeSource source;
	public List<RecipeNutrient> nutrients = new ArrayList<RecipeNutrient>();
	public RecipeNutrient energy;
	public int servingSize;

	public String getNotes()
	{
		String notes = "";

		if (cookingTime != null)
			notes += cookingTime.toString();

		if (courses.size() > 0)
		{
			for (String s : courses)
				notes += s + ' ';
		}

		return notes;
	}

	public Recipe()
	{
		this.courses = new ArrayList<String>();
	}

	// return the RecipeNutrient object for FAT for the current
	// recipe. Returns null if not found
	public RecipeNutrient getFatNutrient()
	{
		for (RecipeNutrient nutrient : this.nutrients)
		{
			if (nutrient.attribute.equalsIgnoreCase("FAT"))
			{
				return nutrient;
			}
		}

		return null;
	}
}
