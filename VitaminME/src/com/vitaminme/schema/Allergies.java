package com.vitaminme.schema;

public class Allergies extends Schema {
	
	public Allergies() {
		this.table_name = "allergies";
		
		this.fields.put("_id", "INT PRIMARY KEY");
		this.fields.put("shortDescription", "TEXT NOT NULL");
		this.fields.put("longDescription", "TEXT");
		this.fields.put("searchValue", "TEXT NOT NULL");
		this.fields.put("type", "TEXT NOT NULL");
	}
}
