package com.vitaminme;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitaminme.api.*;

public class ParseRecipes implements CallBackable {

	@Override
	public void callback(ApiResponse apiResponse) {
		JSONObject jsonObject = apiResponse.jsonObj;
		JSONArray matches = null;
		try {
			matches = jsonObject.getJSONArray("objects");
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
			
			recipe.course = new ArrayList<String>();
			JSONArray courses = null;
			
			try {
				courses = match.getJSONArray("course");
			} catch (Exception e) {
				
			}
			
			for(int j = 0; j < courses.length(); j++) {
				try {
					recipe.course.add((String)courses.get(j));
				} catch (Exception e) {
					
				}
			}
			
			try {
				recipe.cookingTime = new TimeSpan(match.getInt("totalTimeInSeconds"));
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
		
	}

}
