package com.vitaminme.data;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeNutrient
{
	public String attribute;
	public String name;
	public double value;
	public RecipeNutrientUnit unit;
	public double fraction;
	
	public RecipeNutrient(JSONObject obj) throws JSONException {		
		this.attribute = obj.getString("attribute");
		this.name = obj.getString("description");
		this.value = obj.getDouble("value");
		this.unit = new RecipeNutrientUnit(obj.getJSONObject("unit"));
		
		// calculate fraction value
		if (RecipeNutrientBaseline.baselines.containsKey(this.attribute)) {
			this.fraction = this.value / RecipeNutrientBaseline.baselines.get(this.attribute);
		} else {
			this.fraction = -1d;
		}
	}
	
	public String getPercentVal() {
		if (this.fraction < 0) {
			return "NA";
		} else {
			return String.format("%1$, .2f",this.fraction * 100) + "%";
		}
	}
}
