package com.vitaminme;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitaminme.api.*;

public class ParseNutrients implements CallBackable {

	private MainActivity mainActivity;

	@Override
	public void callback(ApiResponse apiResponse) {
		JSONObject jsonObject = apiResponse.jsonObj;
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
		
		
		this.mainActivity.callback(nutrients);
		
		
	}

	public void setCallback(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

}
