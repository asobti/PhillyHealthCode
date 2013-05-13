package com.vitaminme.schema;

public class Favorite_recipes extends Schema {

	public Favorite_recipes() {
		this.table_name = "favorite_recipes";
		
		this.fields.put("recipe_id", "INT NOT NULL");
		this.fields.put("added_on", "INT NOT NULL");
	}
}
