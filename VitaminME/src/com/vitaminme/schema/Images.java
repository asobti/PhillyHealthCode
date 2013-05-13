package com.vitaminme.schema;

public class Images extends Schema {	
	public Images() {
		this.table_name = "images";
		
		fields.put("_id", "INT PRIMARY KEY");
		fields.put("url", "TEXT NOT NULL");		
	}	
}
