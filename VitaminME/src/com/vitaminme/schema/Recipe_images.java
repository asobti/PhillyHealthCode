package com.vitaminme.schema;

public class Recipe_images extends Schema {
	
	public Recipe_images() {
		this.table_name = "recipe_images";
		
		fields.put("recipe_id", "INT NOT NULL");
		fields.put("image_id", "INT NOT NULL");		
	}	
}
