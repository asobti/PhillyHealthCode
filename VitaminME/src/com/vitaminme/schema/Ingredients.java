package com.vitaminme.schema;

public class Ingredients extends Schema {

	public Ingredients() {
		this.table_name = "ingredients";
		
		this.fields.put("_id", "INT PRIMARY KEY");
		this.fields.put("searchValue", "TEXT NOT NULL");
		this.fields.put("term", "TEXT NOT NULL");
		this.fields.put("description", "TEXT NOT NULL");
	}
}
