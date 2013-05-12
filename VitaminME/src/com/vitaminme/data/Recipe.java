package com.vitaminme.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recipe
{
	public String id;
	public String name;

	public HashMap<ImageSize, String> images = new HashMap<ImageSize, String>();

	public List<String> courses = new ArrayList<String>();
	public TimeSpan cookingTime;
	public List<String> ingredients = new ArrayList<String>();
	public RecipeSource source;
	public List<RecipeNutrient> nutrients = new ArrayList<RecipeNutrient>();
	public RecipeNutrient energy = null;
	public int servingSize;
	
	public Recipe(JSONObject obj) throws JSONException
	{
		// name and id
		this.id = obj.getString("id");
		this.name = obj.getString("name");
		
		// images
		JSONArray images = obj.getJSONArray("images");
		
		if (images.length() > 0) {
			JSONObject image = images.getJSONObject(0);
			
			try {
				this.images.put(ImageSize.SMALL, image.getString("hostedSmallUrl"));
			} catch (Exception e) {}
			
			try {
				this.images.put(ImageSize.MEDIUM, image.getString("hostedMediumUrl"));
			} catch (Exception e) {}
			
			try {
				this.images.put(ImageSize.LARGE, image.getString("hostedLargeUrl"));
			} catch (Exception e) {}
		}
		
		// courses
		try {
			JSONArray courses = obj.getJSONObject("attributes").getJSONArray("course");
			for (int i = 0; i < courses.length(); i++) {
				this.courses.add(courses.getString(i));
			}
		} catch (Exception e) {}
		
		// cooking time
		try {
			this.cookingTime = new TimeSpan(obj.getInt("totalTimeInSeconds"));
		} catch (Exception e) {
			this.cookingTime = new TimeSpan();
		}
		
		// ingredients
		try {
			JSONArray iLines = obj.getJSONArray("ingredientLines");
			for (int i = 0; i < iLines.length(); i++) {
				this.ingredients.add(iLines.getString(i));
			}			
		} catch (Exception e) {}
		
		// source
		try {
			this.source = new RecipeSource(obj.getJSONObject("source"));
		} catch (Exception e) {}
		
		// nutrition
		JSONArray nutEstimates = obj.getJSONArray("nutritionEstimates");
		for (int i = 0; i < nutEstimates.length(); i++) {
			RecipeNutrient n = new RecipeNutrient(nutEstimates.getJSONObject(i));
			if (n.attribute.equalsIgnoreCase("ENERC_KCAL")) {
				this.energy = n;
			} else {
				this.nutrients.add(n);
			}
		}
		
		// serving size
		try {
			this.servingSize = obj.getInt("numberOfServings");
		} catch (Exception e) {}
		
	}

	public String getNotes() {
		String notes = "";

		if (cookingTime != null)
			notes += cookingTime.toString();

		if (courses.size() > 0)	{
			for (String s : courses)
				notes += s + ' ';
		}

		return notes;
	}

	// return the RecipeNutrient object for FAT for the current
	// recipe. Returns null if not found
	public RecipeNutrient getFatNutrient() {
		for (RecipeNutrient nutrient : this.nutrients) {
			if (nutrient.attribute.equalsIgnoreCase("FAT"))	{
				return nutrient;
			}
		}
		return null;
	}
	
	public RecipeNutrient getEnergy() {
		return this.energy;
	}
}
