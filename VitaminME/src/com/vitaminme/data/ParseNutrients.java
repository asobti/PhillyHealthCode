package com.vitaminme.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitaminme.api.*;
import com.vitaminme.main.MainActivity;

public class ParseNutrients implements CallBackable {

	private MainActivity caller;
	
	public ParseNutrients(MainActivity caller){
		this.caller = caller;
	}
	@Override
	public void callback(ApiResponse apiResponse) {
		JSONObject jsonObject = apiResponse.jsonObj;
		Pagination pag = new Pagination();
		
		JSONArray objects = null;
		try {
			objects = jsonObject.getJSONArray("objects");
		} catch (JSONException e) {

		}
		
		ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();
		
		for(int i = 0; i < objects.length(); i++) {
			Nutrient nutrient = new Nutrient();
			JSONObject nutrient_ = null;
			try {
				nutrient_ = (JSONObject) objects.get(i);
			} catch (JSONException e) {

			}
			
			try {
				nutrient.id = nutrient_.getInt("id");
			} catch (JSONException e) {

			}
			
			try {
				nutrient.name = nutrient_.getString("description");
			} catch (JSONException e) {

			}
			nutrients.add(nutrient);
		}
		
		
		this.caller.callback(nutrients, pag);
		
		
	}
}
