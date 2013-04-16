package com.vitaminme.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitaminme.api.ApiResponse;
import com.vitaminme.api.CallBackable;
import com.vitaminme.recipelist.RecipeList;

public class ParseRecipes implements CallBackable {
	
	private RecipeList caller;
	
	public ParseRecipes(RecipeList caller) {
		this.caller = caller;
	}
	
	@Override
	public void callback(ApiResponse apiResponse) {
		JSONObject jsonObject = apiResponse.jsonObj;
		JSONArray matches = null;
		
		Pagination pag = new Pagination();
		
		try {
			matches = jsonObject.getJSONArray("objects");
			pag.num_results = jsonObject.getInt("num_results");
			pag.total_pages = jsonObject.getInt("total_pages");
			pag.page_results = jsonObject.getInt("pages_results");
		} catch (JSONException e) {
			
		}
		
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		
		for(int i = 0; i < matches.length(); i++) {
			Recipe recipe = new Recipe();
			JSONObject match = null;
			
			try {
				match = (JSONObject) matches.get(i);
			} catch (Exception e) {
				
			}
			
			try {
				recipe.id = match.getString("id");
			} catch (Exception e) {
				
			}
			
			recipe.images = new HashMap<ImageSize, String>();
			
			try {
				recipe.images.put(ImageSize.SMALL, (String)match.getJSONArray("smallImageUrls").get(0));
			} catch (Exception e) {
				
			}
			
			try {
				recipe.name = match.getString("recipeName");
			} catch (Exception e) {
				
			}
			
			recipe.courses = new ArrayList<String>();
			JSONArray courses = null;
			
			try {
				courses = match.getJSONArray("course");
				
				for(int j = 0; j < courses.length(); j++) {
					try {
						recipe.courses.add((String)courses.get(j));
					} catch (Exception e) {
						
					}
				}
			} catch (Exception e) {	
			}
			
			try {
				recipe.cookingTime = new TimeSpan(match.getInt("totalTimeInSeconds"));
			} catch (Exception e) {
				recipe.cookingTime = new TimeSpan();
			}
			
			try {
				recipe.source =  new RecipeSource(match.getString("sourceDisplayName"));
			} catch (Exception e) {
				
			}
			
			JSONArray ingredients = null;
			
			try {
				ingredients = match.getJSONArray("ingredients");
			} catch (Exception e) {
				
			}
			
			recipe.ingredients = new ArrayList<String>();
			
			for(int k = 0; k < ingredients.length(); k++) {
				try {
					recipe.ingredients.add((String)ingredients.get(k));
				} catch (Exception e) {
					
				}
			}
			recipes.add(recipe);			
		}
		
		caller.callback(recipes, pag);
	}

}
