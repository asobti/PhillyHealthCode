package com.vitaminme.api;

public class ApiFilter {
	private String name;
	private ApiFilterOp op;
	private String val;
	
	public ApiFilter(String name, ApiFilterOp op, String val) {
		this.name = name;
		this.op = op;
		this.val = val;
	}
}
