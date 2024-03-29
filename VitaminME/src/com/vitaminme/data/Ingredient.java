package com.vitaminme.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5155966358076883057L;
	public int id;
	public int value;
	public String searchValue;
	public String description;
	public String term;

	public Ingredient()
	{
	}

	public Ingredient(JSONObject json) throws JSONException
	{
		this.id = json.getInt("id");
		this.value = 0;
		this.searchValue = json.getString("searchValue");
		this.description = json.getString("description");
		this.term = json.getString("term");
	}
}
