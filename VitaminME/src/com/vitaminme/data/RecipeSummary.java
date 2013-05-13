package com.vitaminme.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitaminme.exceptions.RecipeParseException;

public class RecipeSummary {
	public String name;
	public String id;
	public HashMap<ImageSize, String> images = new HashMap<ImageSize, String>();
	public List<String> courses = new ArrayList<String>();
	public String sourceName;
	public List<String> ingredients = new ArrayList<String>();
	
	public RecipeSummary(JSONObject obj) throws JSONException, RecipeParseException {
		// name and id
		this.name = obj.getString("recipeName");
		this.id = obj.getString("id");
		
		// image url
		JSONArray smallImageUrls = obj.getJSONArray("smallImageUrls");
		
		if (smallImageUrls.length() > 0) {
			this.images.put(ImageSize.SMALL, smallImageUrls.getString(0));
		} else {
			throw new RecipeParseException("No image url available");
		}
		
		// courses
		try {
			JSONObject attributes = obj.getJSONObject("attributes");
			JSONArray courses = attributes.getJSONArray("course");
			
			for(int i = 0; i < courses.length(); i++) {
				this.courses.add(courses.getString(i));
			}
		} catch (JSONException e) {}
		
		// source display name
		try {
			this.sourceName = obj.getString("sourceDisplayName");
		} catch (JSONException e) {
			this.sourceName = "";
		}
		
		// ingredients
		try {
			JSONArray ing = obj.getJSONArray("ingredients");
			
			for(int i = 0; i < ing.length(); i++) {
				this.ingredients.add(ing.getString(i));
			}
		} catch (JSONException e) {}
	}
}