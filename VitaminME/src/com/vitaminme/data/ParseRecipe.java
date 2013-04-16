package com.vitaminme.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitaminme.api.ApiResponse;
import com.vitaminme.api.CallBackable;
import com.vitaminme.recipe.RecipeDetails;

public class ParseRecipe implements CallBackable
{
	// change RecipeList to whatever Matt's activity is called
	// and all references of it through this file
	private RecipeDetails caller;

	public ParseRecipe(RecipeDetails recipe)
	{
		this.caller = recipe;
	}

	@Override
	public void callback(ApiResponse apiResponse)
	{
		JSONObject jsonObject = apiResponse.jsonObj;
		Recipe recipe = new Recipe();
		Pagination pag = new Pagination();
		System.out.println("Parse Recipe callback");
		
		// get basic info
		try
		{
			// get recipe info
			recipe.id = jsonObject.getString("id");
			recipe.name = jsonObject.getString("name");
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		// get courses
		recipe.courses = new ArrayList<String>();
		try
		{
			JSONObject attributes = jsonObject.getJSONObject("attributes");
			JSONArray courses = attributes.getJSONArray("course");
			
			for(int i = 0; i < courses.length(); i++) {
				recipe.courses.add((String)courses.get(i));
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		// get cooking time
		try
		{
			recipe.cookingTime = new TimeSpan(
					jsonObject.getInt("totalTimeInSeconds"));
		}
		catch (Exception e)
		{
			recipe.cookingTime = new TimeSpan();
			System.out.println(e.getMessage());
		}
		
		// get source information
		try	
		{
			recipe.source = new RecipeSource(jsonObject.getJSONObject("source"));
		}
		catch (Exception e)
		{

		}
		
		// get ingredients
		JSONArray ingredients = null;
		try
		{
			ingredients = jsonObject.getJSONArray("ingredientLines");
			
			for (int k = 0; k < ingredients.length(); k++)
			{
				try
				{
					recipe.ingredients.add((String) ingredients.get(k));
				}
				catch (Exception e)
				{

				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}		
		
		
		// get images		
		try
		{
			JSONArray images = jsonObject.getJSONArray("images");
			JSONObject hostedImages = images.getJSONObject(0);
			recipe.images.put(ImageSize.SMALL, hostedImages.getString("hostedSmallUrl"));
			recipe.images.put(ImageSize.LARGE, hostedImages.getString("hostedLargeUrl"));
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// get nutrient info
		try
		{
			JSONArray nutrients = jsonObject.getJSONArray("nutritionEstimates");
			for(int i = 0; i < nutrients.length(); i++) {
				JSONObject nutEst = nutrients.getJSONObject(i);
				
				if (nutEst.getString("description").equalsIgnoreCase("energy")) {
					recipe.energy = new RecipeNutrient(nutEst);
				} else {
					recipe.nutrients.add(new RecipeNutrient(nutEst));
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		// get serving size
		try {
			recipe.servingSize = jsonObject.getInt("numberOfServings");
		} catch (Exception e) {
			recipe.servingSize = 0;
			System.out.println(e.getMessage());
		}
		caller.callback(recipe, pag);
	}

}
