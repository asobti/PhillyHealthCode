package com.vitaminme.schema;

public class Recipes extends Schema {	
	public Recipes() {
		this.table_name = "recipes";
		
		fields.put("_id", "INT PRIMARY KEY");
		fields.put("name", "TEXT NOT NULL");
		fields.put("cooking_time", "INT");
		fields.put("serving_size", "INT");
	}	
}
