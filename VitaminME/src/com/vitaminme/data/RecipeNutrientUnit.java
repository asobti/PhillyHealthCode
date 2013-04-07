package com.vitaminme.data;

import org.json.JSONObject;

public class RecipeNutrientUnit
{
	public String id;
	public String name;
	public String abbr;
	public String plural;
	public String pluralAbbr;

	public RecipeNutrientUnit(JSONObject obj)
	{
		try
		{
			this.id = obj.getString("id");
			this.name = obj.getString("name");
			this.abbr = obj.getString("abbreviation");
			this.plural = obj.getString("plural");
			this.pluralAbbr = obj.getString("pluralAbbreviation");
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
