package com.vitaminme.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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

import com.google.gson.Gson;
import com.vitaminme.data.Allergy;
import com.vitaminme.data.Diet;
import com.vitaminme.data.Ingredient;
import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Pagination;
import com.vitaminme.data.Recipe;
import com.vitaminme.data.RecipeSummary;
import com.vitaminme.exceptions.APICallException;
import com.vitaminme.exceptions.APILimitExceededException;
import com.vitaminme.exceptions.RecipeParseException;

/*
 * This class is responsible for all communications with the API
 * Exists as a singleton
 */
public class ApiAdapter {
	private static ApiAdapter _instance = null;

	private final String endpoint = "http://vitaminme.notimplementedexception.me/";
	private Pagination pag;

	private ApiAdapter() {
	}

	public static ApiAdapter getInstance() {
		if (ApiAdapter._instance == null) {
			ApiAdapter._instance = new ApiAdapter();
		}

		return ApiAdapter._instance;
	}

	public ArrayList<Nutrient> getNutrients(
			ArrayList<Entry<String, String>> params) throws APICallException {
		return this.getNutrients(params, new ArrayList<ApiFilter>());
	}

	public ArrayList<Nutrient> getNutrients(
			ArrayList<Entry<String, String>> params, List<ApiFilter> filters)
			throws APICallException {
		JSONObject response;
		ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();

		try {
			String url = this.endpoint + "nutrients"
					+ this.buildQueryString(params, filters);
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
			// some other exception occurred. Wrap it as an APICallException and
			// re-throw
			throw new APICallException(e.getMessage(), e.getCause());
		}

		return nutrients;
	}

	public ArrayList<Diet> getDiets(ArrayList<Entry<String, String>> params)
			throws APICallException {
		return this.getDiets(params, new ArrayList<ApiFilter>());
	}

	public ArrayList<Diet> getDiets(ArrayList<Entry<String, String>> params,
			List<ApiFilter> filters) throws APICallException {
		JSONObject response;
		ArrayList<Diet> diets = new ArrayList<Diet>();

		try {
			String url = this.endpoint + "diets"
					+ this.buildQueryString(params, filters);
			response = this.get(url);
			JSONArray arr = response.getJSONArray("objects");

			for (int i = 0; i < arr.length(); i++) {
				try {
					Diet dt = new Diet(arr.getJSONObject(i));
					diets.add(dt);
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
			// some other exception occurred. Wrap it as an APICallException and
			// re-throw
			throw new APICallException(e.getMessage(), e.getCause());
		}

		return diets;
	}

	public ArrayList<Allergy> getAllergies(
			ArrayList<Entry<String, String>> params) throws APICallException {
		return this.getAllergies(params, new ArrayList<ApiFilter>());
	}

	public ArrayList<Allergy> getAllergies(
			ArrayList<Entry<String, String>> params, List<ApiFilter> filters)
			throws APICallException {
		JSONObject response;
		ArrayList<Allergy> allergies = new ArrayList<Allergy>();

		try {
			String url = this.endpoint + "allergies"
					+ this.buildQueryString(params, filters);
			response = this.get(url);
			JSONArray arr = response.getJSONArray("objects");

			for (int i = 0; i < arr.length(); i++) {
				try {
					Allergy all = new Allergy(arr.getJSONObject(i));
					allergies.add(all);
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
			// some other exception occurred. Wrap it as an APICallException and
			// re-throw
			throw new APICallException(e.getMessage(), e.getCause());
		}

		return allergies;
	}

	public ArrayList<Ingredient> getIngredients(
			ArrayList<Entry<String, String>> params) throws APICallException {
		return this.getIngredients(params, new ArrayList<ApiFilter>());
	}

	public ArrayList<Ingredient> getIngredients(
			ArrayList<Entry<String, String>> params, List<ApiFilter> filters)
			throws APICallException {
		JSONObject response;
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

		try {
			String url = this.endpoint + "ingredients"
					+ this.buildQueryString(params, filters);

			response = this.get(url);
			JSONArray arr = response.getJSONArray("objects");

			for (int i = 0; i < arr.length(); i++) {
				try {
					Ingredient ing = new Ingredient(arr.getJSONObject(i));
					ingredients.add(ing);
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
			// some other exception occurred. Wrap it as an APICallException and
			// re-throw
			throw new APICallException(e.getMessage(), e.getCause());
		}

		return ingredients;
	}

	public ArrayList<RecipeSummary> getRecipes(
			ArrayList<Entry<String, String>> params) throws APICallException {
		return this.getRecipes(params, new ArrayList<ApiFilter>());
	}

	public ArrayList<RecipeSummary> getRecipes(
			ArrayList<Entry<String, String>> params, List<ApiFilter> filters)
			throws APICallException {
		JSONObject response;
		ArrayList<RecipeSummary> recipes = new ArrayList<RecipeSummary>();

		try {
			String url = this.endpoint + "recipes"
					+ this.buildQueryString(params, filters);

			response = this.get(url);
			JSONArray arr = response.getJSONArray("objects");

			for (int i = 0; i < arr.length(); i++) {
				try {
					RecipeSummary rec = new RecipeSummary(arr.getJSONObject(i));
					recipes.add(rec);
				} catch (JSONException e) {
					continue;
				} catch (RecipeParseException e) {
					continue;
				}
			}

			this.pag = parsePaginationInfo(response);
			
			if (this.pag == null){
				System.out.println("foo");
			}

		} catch (APICallException e) {
			throw e;
		} catch (Exception e) {
			// some other exception occurred. Wrap it as an APICallException and
			// re-throw
			throw new APICallException(e.getMessage(), e.getCause());
		}

		return recipes;
	}
	
	public Recipe getRecipe(String id) throws APICallException {
		Recipe recipe = null;
		JSONObject response;
		
		try {
			String url = this.endpoint + "recipes/" + id;
			response = this.get(url);
			
			recipe = new Recipe(response);
		} catch (APICallException e) {
			throw e;
		} catch (Exception e) {
			throw new APICallException(e.getMessage(), e.getCause());
		}
		
		return recipe;
	}

	/*
	 * Returns the pagination object for the last query
	 */
	public Pagination getPaginationObject() {
		return this.pag;
	}

	private Pagination parsePaginationInfo(JSONObject resp) throws JSONException {
		Pagination pag = new Pagination();

		pag.total_pages = resp.getInt("total_pages");
		pag.num_results = resp.getInt("num_results");
		pag.page_results = resp.getInt("page_results");

		return pag;
	}

	/*
	 * Combines a string, string hashmap into a valid query string that can be
	 * concatenated to a url
	 */
	private String buildQueryString(ArrayList<Entry<String, String>> params,
			List<ApiFilter> filters) throws UnsupportedEncodingException {
		if (params.size() > 0) {
			String queryString = "?";
			for (Entry<String, String> entry : params) {
				queryString += String.format("%s=%s&", entry.getKey(),
						URLEncoder.encode(entry.getValue(), "UTF-8"));
			}

			if (filters.size() > 0) {
				Gson gson = new Gson();
				String filter_json = gson.toJson(filters);
				queryString += "filter="
						+ URLEncoder.encode(filter_json, "UTF-8") + '&';
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
	private JSONObject get(String url) throws APICallException,
			ClientProtocolException, IOException {
		JSONObject response;
		String jsonString;

		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request = new HttpGet(url);

		// accept gzip'ed data
		request.addHeader("Accept-Encoding", "gzip");
		// execute the request
		HttpResponse resp = client.execute(request);
		StatusLine status = resp.getStatusLine();

		if (status.getStatusCode() == HttpStatus.SC_OK) {
			Header contentEncoding = resp.getFirstHeader("Content-Encoding");
			InputStream instream = resp.getEntity().getContent();

			// was the response gzip'ed?
			if (contentEncoding != null
					&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				instream = new GZIPInputStream(instream);
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					instream));
			StringBuilder responseString = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				responseString.append(line);
			}

			jsonString = responseString.toString();

			try {
				response = new JSONObject(jsonString);
			} catch (JSONException e) {
				throw new APICallException(
						"Unable to parse API response as valid JSON object");
			}
		} else if (status.getStatusCode() == HttpStatus.SC_CONFLICT) {
			throw new APILimitExceededException("Yummly API Limit Exceeded");
		} else {
			throw new APICallException("API responded with status code "
					+ status.getStatusCode());
		}

		return response;
	}
}
