package com.vitaminme.schema;

public class Recipe_nutrients extends Schema {
	
	public Recipe_nutrients() {
		this.table_name = "recipe_nutrients";
		
		fields.put("recipe_id", "INT NOT NULL");
		fields.put("nutrient_id", "INT NOT NULL");		
	}	
}
