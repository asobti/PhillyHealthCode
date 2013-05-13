package com.vitaminme.schema;

public class Recent_recipes extends Schema {

	public Recent_recipes() {
		this.table_name = "recent_recipes";
		
		this.fields.put("recipe_id", "INT NOT NULL");
		this.fields.put("added_on", "INT NOT NULL");
	}
}
