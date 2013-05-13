package com.vitaminme.schema;

public class Courses extends Schema {
	
	public Courses() {
		this.table_name = "courses";
		
		this.fields.put("_id", "INT PRIMARY KEY");
		this.fields.put("id", "TEXT NOT NULL");
		this.fields.put("shortDescription", "TEXT NOT NULL");
		this.fields.put("longDescription", "TEXT");
		this.fields.put("searchValue", "TEXT NOT NULL");
		this.fields.put("type", "TEXT NOT NULL");
	}
}
