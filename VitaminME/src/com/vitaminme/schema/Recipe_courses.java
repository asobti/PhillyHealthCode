package com.vitaminme.schema;

public class Recipe_courses extends Schema {
	
	public Recipe_courses() {
		this.table_name = "recipe_courses";
		
		fields.put("recipe_id", "INT NOT NULL");
		fields.put("course_id", "INT NOT NULL");		
	}	
}
