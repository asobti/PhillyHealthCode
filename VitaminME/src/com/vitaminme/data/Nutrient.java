package com.vitaminme.data;

import java.io.Serializable;

public class Nutrient implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3145633552956087855L;
	public String name; // name of nutrient
	public int id; // id of nutrient
	public int value; // -1, 0, 1

	public Nutrient()
	{

	}

	public Nutrient(String name)
	{
		this.name = name;
	}
}
