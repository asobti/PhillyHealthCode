package com.vitaminme.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Nutrient implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3145633552956087855L;
	
	public int id; // id of nutrient
	public String name; // name of nutrient
	public String tag;
	public int value; // -1, 0, 1
	public String unit;
	public int decimals;

	public Nutrient()
	{

	}

	public Nutrient(String name)
	{
		this.name = name;
	}
	
	/*
	 * Construct instance from a JSONObject
	 */
	public Nutrient(JSONObject jsonObject) throws JSONException {
		this.id = jsonObject.getInt("id");
		this.name = jsonObject.getString("description");
		this.tag = jsonObject.getString("tagname");
		this.unit = jsonObject.getString("unit");
		this.decimals = jsonObject.getInt("decimals");
	}
}
