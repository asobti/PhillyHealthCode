package com.vitaminme.data;

import com.vitaminme.api.ApiCallParams;
import com.vitaminme.api.ApiCallTask;


public class TestApi {

	
	public void test1() {
		String url = "http://www.reddit.com/.json";
		ApiCallParams params = new ApiCallParams();
		params.url = url;
//		params.callBackObject = new ParseNutrients();
		ApiCallTask task = new ApiCallTask();
		task.execute(params);
		
	}
	
	public void test2() {
		@SuppressWarnings("unused")
		RecipeFilter filter = new RecipeFilter();
		// "?filter=" + filter.toString();
	}
}
