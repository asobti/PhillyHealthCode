package com.vitaminme.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Nutrient extends DietObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3145633552956087855L;

	public String tag;
	public String unit;
	public String info;
	public float daily_value;

	public Nutrient()
	{
	}

	public Nutrient(String name)
	{
		super.term = name;
	}

	/*
	 * Construct instance from a JSONObject
	 */
	public Nutrient(JSONObject jsonObject) throws JSONException
	{
		super.id = jsonObject.getInt("id");
		super.term = jsonObject.getString("description");
		super.value = 0;
		this.tag = jsonObject.getString("tagname");
		this.unit = jsonObject.getString("unit");
		this.info = jsonObject.getString("info");
		this.daily_value = (float) jsonObject.getDouble("daily_value");
	}
}
