package com.vitaminme.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.vitaminme.api.*;
import com.vitaminme.recipelist.RecipeList;

public class ParseRecipe implements CallBackable {
	// change RecipeList to whatever Matt's activity is called
	// and all references of it through this file
	private RecipeList caller;
	
	public ParseRecipe(RecipeList caller) {
		this.caller = caller;
	}
	
	@Override
	public void callback(ApiResponse apiResponse) {
		JSONObject jsonObject = apiResponse.jsonObj;
		Recipe recipe = new Recipe();
		Pagination pag = new Pagination();
		
		try {
			// get pagination data
			pag.num_results = jsonObject.getInt("num_results");
			pag.total_pages = jsonObject.getInt("total_pages");
			pag.page_results = jsonObject.getInt("page_results");
			
			// get recipe info
			recipe.id = jsonObject.getString("id");
			recipe.name = jsonObject.getString("recipeName");
		} catch (Exception e) {
			
		}
		
		recipe.courses = new ArrayList<String>();
		JSONArray courses = null;
		
		try {
			courses = jsonObject.getJSONArray("course");
			
			for(int j = 0; j < courses.length(); j++) {
				try {
					recipe.courses.add((String)courses.get(j));
				} catch (Exception e) {
					
				}
			}
		} catch (Exception e) {	
		}
		
		try {
			recipe.cookingTime = new TimeSpan(jsonObject.getInt("totalTimeInSeconds"));
		} catch (Exception e) {
			
		}
		
		try {
			recipe.sourceDisplayName =  jsonObject.getString("sourceDisplayName");
		} catch (Exception e) {
			
		}
		
		JSONArray ingredients = null;
		
		try {
			ingredients = jsonObject.getJSONArray("ingredients");
		} catch (Exception e) {
			
		}
		
		recipe.ingredients = new ArrayList<String>();
		
		for(int k = 0; k < ingredients.length(); k++) {
			try {
				recipe.ingredients.add((String)ingredients.get(k));
			} catch (Exception e) {
				
			}
		}			
		
		// caller.callback(recipe, pag);
	}

}
