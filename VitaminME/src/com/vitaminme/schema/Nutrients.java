package com.vitaminme.schema;

public class Nutrients extends Schema {

	public Nutrients() {
		this.table_name = "nutrients";
		
		this.fields.put("_id", "INT PRIMARY KEY");
		this.fields.put("description", "TEXT NOT NULL");
		this.fields.put("tagname", "TEXT NOT NULL");
		this.fields.put("unit", "TEXT NOT NULL");
		this.fields.put("info", "TEXT NOT NULL");
		this.fields.put("daily_value", "REAL NOT NULL");
	}
}
