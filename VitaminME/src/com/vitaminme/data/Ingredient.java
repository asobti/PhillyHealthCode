package com.vitaminme.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient extends DietObject implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5155966358076883057L;
	
	public String searchValue;
	public String description;

	public Ingredient()
	{
	}

	public Ingredient(JSONObject json) throws JSONException
	{
		super.id = json.getInt("id");
		super.value = 0;
		this.searchValue = json.getString("searchValue");
		this.description = json.getString("description");
		super.term = json.getString("term");
	}
}
