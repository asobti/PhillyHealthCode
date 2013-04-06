package com.vitaminme;

import com.vitaminme.api.*;


public class TestApi {

	
	public void test1() {
		String url = "http://www.reddit.com/.json";
		ApiCallParams params = new ApiCallParams();
		params.url = url;
		params.callBackObject = new ParseNutrients();
		ApiCallTask task = new ApiCallTask();
		task.execute(params);
		
	}
}
