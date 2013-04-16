package com.vitaminme.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Pagination;
import com.vitaminme.exceptions.*;

/*
 * This class is responsible for all communications with the API
 * Exists as a singleton
 */
public class ApiAdapter {
	private static ApiAdapter _instance = null;
	
	private final String endpoint = "http://vitaminme.notimplementedexception.me/";
	private Pagination pag;
	
	private ApiAdapter() {}
	
	public static ApiAdapter getInstance() {
		if (ApiAdapter._instance == null) {
			ApiAdapter._instance = new ApiAdapter();
		}
		
		return ApiAdapter._instance;
	}
	
	public ArrayList<Nutrient> getNutrients(HashMap<String, String> params) throws APICallException {
		JSONObject response;
		String url = this.endpoint + "nutrients" + this.buildQueryString(params);		
		ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();
		
		try {
			response = this.get(url);
			JSONArray arr = response.getJSONArray("objects");
			
			for (int i = 0; i < arr.length(); i++) {
				try {
					Nutrient nut = new Nutrient(arr.getJSONObject(i));
					nutrients.add(nut);
				} catch (JSONException e) {
					// unable to parse this particular object
					// ignore and continue
					continue;
				}
			}
			
			this.pag = parsePaginationInfo(response);
			
		} catch (APICallException e) {
			throw e;
		} catch (Exception e) {
			// some other exception occurred. Wrap it as an APICallException and re-throw
			throw new APICallException(e.getMessage(), e.getCause());
		}
		
		return nutrients;
	}
	
	/*
	 * Returns the pagination object for the last query
	 */
	public Pagination getPaginationObject() {
		return this.pag;		
	}
	
	private Pagination parsePaginationInfo(JSONObject resp) {
		Pagination pag = new Pagination();
		
		try {
			pag.total_pages = resp.getInt("total_pages");
			pag.num_results = resp.getInt("num_results");
			pag.page_results = resp.getInt("page_results");
		} catch (JSONException e) {
			// is there a better way to handle this?
			// How critical is pagination info? If extremely critical
			// this should throw an APICallException, and client
			// should not proceed in that case
			pag = null;
		}
		
		return pag;
	}
	/*
	 * Combines a string, string hashmap into a valid query string 
	 * that can be concatenated to a url 
	 */
	private String buildQueryString(HashMap<String, String> params) {
		if (params.size() > 0) {
			String queryString = "?";
			for(Map.Entry<String, String> entry : params.entrySet()) {
				queryString += String.format("%s=%s&", entry.getKey(), entry.getValue());
			}
			
			// trim the trailing ampersand
			queryString = queryString.substring(0, queryString.length() - 1);
			return queryString;
		} else {
			return "";
		}		
	}
	
	/*
	 * Makes a get request to the url
	 */
	private JSONObject get(String url) throws APICallException, ClientProtocolException, IOException {
		JSONObject response;
		String jsonString;
		
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request = new HttpGet(url);
		
		// accept gzip'ed data
		request.addHeader("Accept-Encoding", "gzip");
		
		// nullify the old pagination object
		this.pag = null;
		
		// execute the request		
		HttpResponse resp = client.execute(request);
		StatusLine status = resp.getStatusLine();
		
		if (status.getStatusCode() == HttpStatus.SC_OK) {
			Header contentEncoding = resp.getFirstHeader("Content-Encoding");
			InputStream instream = resp.getEntity().getContent();
			
			// was the response gzip'ed?
			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				instream = new GZIPInputStream(instream);
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			StringBuilder responseString = new StringBuilder();
			String line;
			
			while ((line = reader.readLine()) != null) {
				responseString.append(line);
			}
			
			jsonString = responseString.toString();
			
			try {
				response = new JSONObject(jsonString);				
			} catch (JSONException e) {
				throw new APICallException("Unable to parse API response as valid JSON object");
			}
		} else if (status.getStatusCode() == HttpStatus.SC_CONFLICT) {
			throw new APILimitExceededException("Yummly API Limit Exceeded");
		} else {
			throw new APICallException("API responded with status code " + status.getStatusCode());
		}
		
		return response;
	}
}
