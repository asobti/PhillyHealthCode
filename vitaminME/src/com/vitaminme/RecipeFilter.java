package com.vitaminme;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;


public class RecipeFilter {
	List<String> ingredients;
	List<Nutrient> nutrients;
	
	@Override
	public String toString() {
		JSONObject jo = new JSONObject();
		try {
			jo.put("ingredients", this.ingredients);
		} catch (JSONException e) {

		}
		try {
			jo.put("ingredients", this.nutrients);
		} catch (JSONException e) {
			
		}
		return "";
		
	}
}
