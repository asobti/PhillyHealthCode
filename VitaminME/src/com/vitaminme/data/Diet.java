package com.vitaminme.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Diet {
	public int id;
	public String shortDescription;
	public String longDescription;
	public String searchValue;
	
	public Diet() {}
	
	public Diet(JSONObject obj) throws JSONException {
		this.id = obj.getInt("id");
		this.shortDescription = obj.getString("shortDescription");
		this.longDescription = obj.getString("longDescription");
		this.searchValue = obj.getString("searchValue");
	}
}
