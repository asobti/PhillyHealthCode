package com.vitaminme.schema;

public class Sources extends Schema {	
	public Sources() {
		this.table_name = "sources";
		
		fields.put("_id", "INT PRIMARY KEY");
		fields.put("source_url", "TEXT NOT NULL");
		fields.put("source_name", "TEXT NOT NULL");
		fields.put("site_url", "TEXT NOT NULL");
	}	
}
