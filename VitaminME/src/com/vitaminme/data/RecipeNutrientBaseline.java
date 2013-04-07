package com.vitaminme.data;

import java.util.HashMap;
import java.util.Map;

public final class RecipeNutrientBaseline
{
	public static final Map<String, Double> baselines;
	
	static {
		baselines = new HashMap<String, Double>();
		baselines.put("FAT", 65.0);			// Total fat in grams
		baselines.put("FASAT", 20.0);		// Saturated fats in grams
		baselines.put("CHOLE", 0.3);		// Cholesterol in grams
		baselines.put("NA", 2.4);			// Sodium in grams
		baselines.put("K", 3.5);			// Potassium in grams
		baselines.put("CHOCDF", 300.0);		// carbohydrates in grams
		baselines.put("FIBTG", 25.0);		// fiber in grams
		baselines.put("PROCNT", 50.0);		// proteins in grams
		baselines.put("VITA_IU", 5000.0);	// Vitamin A in IU (International Unit)
		baselines.put("VITC", 0.06);		// Vitamin C in grams
		baselines.put("CA", 1.0);			// Calcium in grams
		baselines.put("FE", 0.018);			// Iron in grams
		baselines.put("VITD", 400.0);		// Vitamin D in IU
		
	}
}
