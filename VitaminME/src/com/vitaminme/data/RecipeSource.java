package com.vitaminme.data;

import org.json.JSONObject;

public class RecipeSource
{
	public String sourceUrl;
	public String siteUrl;
	public String sourceName;
	
	public RecipeSource(JSONObject obj) {
		try {
			this.sourceUrl = obj.getString("sourceRecipeUrl");
			this.siteUrl = obj.getString("sourceSiteUrl");
			this.sourceName = obj.getString("sourceDisplayName");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public RecipeSource(String name) {
		this.sourceName = name;
	}
}
