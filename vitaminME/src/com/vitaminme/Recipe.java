package com.vitaminme;

import java.util.HashMap;
import java.util.List;

public class Recipe {
	
	String id;
	HashMap<ImageSize, String> images;
	String url;
	String name;
	List<String> course;
	TimeSpan cookingTime;
	List<String> ingredients;
	
	
	public Recipe() {
		
	}
}

