package com.vitaminme.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient {
	
	public int id;
	public String searchValue;
	public String description;
	public String term;
	
	public Ingredient() {}
	
	public Ingredient(JSONObject json) throws JSONException {
		this.id = json.getInt("id");
		this.searchValue = json.getString("searchValue");
		this.description = json.getString("description");
		this.term = json.getString("term");
	}
}
