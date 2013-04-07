package com.vitaminme.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recipe
{
	public String id;
	public HashMap<ImageSize, String> images;
	public String url;
	public String name;
	public List<String> courses;
	public TimeSpan cookingTime;
	public List<String> ingredients;
	public String sourceDisplayName;
	
	public String getNotes() {
		String notes = "";
		
		if (cookingTime != null)
			notes += cookingTime.toString();		
		
		if (courses.size() > 0) {
			for(String s:courses)
				notes += s + ' ';
		}
	
		return notes;
	}
	public Recipe()
	{
		this.courses = new ArrayList<String>();
	}
}
