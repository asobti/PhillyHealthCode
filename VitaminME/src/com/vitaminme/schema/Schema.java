package com.vitaminme.schema;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class Schema {
	public String table_name;
	public HashMap<String, String> fields = new HashMap<String, String>();
	
	public String create_statement() {
		String query = "CREATE " + this.table_name;
		query += " (";
		
		for (Entry<String, String> kvp : this.fields.entrySet()) {
			query += String.format("%s %s,", kvp.getKey(), kvp.getValue());
		}
		
		// trim trailing comma
		query = query.substring(0, query.length()-1);
				
		query += ");";
		return query;
	}
}
