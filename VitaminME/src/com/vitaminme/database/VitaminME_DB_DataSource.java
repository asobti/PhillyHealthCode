package com.vitaminme.database;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.vitaminme.api.ApiAdapter;
import com.vitaminme.data.DataStore;
import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Recipe;
import com.vitaminme.exceptions.APICallException;

public class VitaminME_DB_DataSource
{
	// Database fields
	private SQLiteDatabase db;
	private VitaminME_DB dbHelper;
	private Context context;

	public VitaminME_DB_DataSource(Context context)
	{
		this.dbHelper = new VitaminME_DB(context);
		this.context = context;
	}

	public void open() throws SQLException
	{
		db = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	// FIX THIS AYUSH
	public int addStarred(Recipe recipe)
	{
		ContentValues values = new ContentValues();
		values.put(VitaminME_DB.RECIPE_ID, recipe.id);
		values.put("name", recipe.name);
		values.put("images", recipe.name); // FIX THIS AYUSH
		values.put("courses", recipe.name); // FIX THIS AYUSH
		values.put("cooking_time", recipe.cookingTime.toString());
		values.put("ingredients", recipe.name); // FIX THIS AYUSH
		values.put("source", recipe.name); // FIX THIS AYUSH
		values.put("nutrients", recipe.name); // FIX THIS AYUSH
		values.put("energy", recipe.name); // FIX THIS AYUSH
		values.put("serving_size", recipe.servingSize);

		try
		{
			db.insert(VitaminME_DB.TABLE_STARRED_RECIPES, null, values);
		}
		catch (Exception ex)
		{
			return -1;
		}

		return 0;
	}

	public int deleteStarred(Recipe recipe) // Or just send the recipe_id
											// instead?
	{
		String recipe_id = recipe.id;

		try
		{
			db.delete(VitaminME_DB.TABLE_STARRED_RECIPES,
					VitaminME_DB.RECIPE_ID + " = " + recipe_id, null);
		}
		catch (Exception ex)
		{
			return -1;
		}

		return 0;
	}

	public Recipe getStarred(String recipe_id)
	{
		Cursor cursor = db.query(VitaminME_DB.TABLE_STARRED_RECIPES, null,
				VitaminME_DB.RECIPE_ID + " = " + recipe_id, null, null, null,
				null);
		cursor.moveToFirst();

		Recipe recipe = cursorToRecipe(cursor);
		cursor.close();

		return recipe;
	}

	// FIX THIS AYUSH
	public ArrayList<Recipe> getAllStarred()
	{
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		Cursor cursor = db.query(VitaminME_DB.TABLE_STARRED_RECIPES, null,
				null, null, null, null, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			Recipe recipe = cursorToRecipe(cursor);
			recipes.add(recipe);
			cursor.moveToNext();
		}

		cursor.close();
		return recipes;
	}

	// FIX THIS AYUSH
	public int addRecent(Recipe recipe)
	{
		ContentValues values = new ContentValues();
		values.put(VitaminME_DB.RECIPE_ID, recipe.id);
		values.put("name", recipe.name);
		values.put("images", recipe.name); // FIX THIS AYUSH
		values.put("courses", recipe.name); // FIX THIS AYUSH
		values.put("cooking_time", recipe.cookingTime.toString());
		values.put("ingredients", recipe.name); // FIX THIS AYUSH
		values.put("source", recipe.name); // FIX THIS AYUSH
		values.put("nutrients", recipe.name); // FIX THIS AYUSH
		values.put("energy", recipe.name); // FIX THIS AYUSH
		values.put("serving_size", recipe.servingSize);

		try
		{
			db.insert(VitaminME_DB.TABLE_RECENTLY_VIEWED, null, values);
		}
		catch (Exception ex)
		{
			return -1;
		}

		return 0;
	}

	public int deleteRecent(Recipe recipe) // Or just send the recipe_id
	// instead?
	{
		String recipe_id = recipe.id;

		try
		{
			db.delete(VitaminME_DB.TABLE_STARRED_RECIPES,
					VitaminME_DB.RECIPE_ID + " = " + recipe_id, null);
		}
		catch (Exception ex)
		{
			return -1;
		}

		return 0;
	}

	public Recipe getRecent(String recipe_id)
	{
		Cursor cursor = db.query(VitaminME_DB.TABLE_RECENTLY_VIEWED, null,
				VitaminME_DB.RECIPE_ID + " = " + recipe_id, null, null, null,
				null);
		cursor.moveToFirst();

		Recipe recipe = cursorToRecipe(cursor);
		cursor.close();

		return recipe;
	}

	// FIX THIS AYUSH
	public ArrayList<Recipe> getAllRecent()
	{
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		Cursor cursor = db.query(VitaminME_DB.TABLE_RECENTLY_VIEWED, null,
				null, null, null, null, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			Recipe recipe = cursorToRecipe(cursor);
			recipes.add(recipe);
			cursor.moveToNext();
		}

		cursor.close();
		return recipes;
	}

	public ArrayList<Nutrient> getAllNutrients()
	{
		ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();
		Cursor cursor = db.query(VitaminME_DB.TABLE_NUTRIENTS_LIST, null, null,
				null, null, null, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			Nutrient nutrient = cursorToNutrient(cursor);
			nutrients.add(nutrient);
			cursor.moveToNext();
		}

		cursor.close();
		return nutrients;
	}
	
	public void clearNutrientsDB()
	{
		db.execSQL("DELETE FROM " + VitaminME_DB.TABLE_NUTRIENTS_LIST);
	}

	public void addNutrients()
	{
		new addNutrientsTask().execute();
	}

	private final class addNutrientsTask extends
			AsyncTask<Void, Void, ArrayList<Nutrient>>
	{

		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute()
		{

		}

		@Override
		protected ArrayList<Nutrient> doInBackground(Void... arg0)
		{
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("count", "100"));

			try
			{
				return api.getNutrients(params);
			}
			catch (APICallException e)
			{
				return null;
			}
		}

		@Override
		protected void onPostExecute(final ArrayList<Nutrient> nutrients)
		{
			if (nutrients != null && nutrients.size() > 0)
			{
				for (Nutrient n : nutrients)
				{
					ContentValues values = new ContentValues();
					values.put(VitaminME_DB.NUTRIENT_ID, n.id);
					values.put("name", n.name);
					values.put("tagname", n.tag);
					values.put("unit", n.unit);
					values.put("info", n.info);
					values.put("daily_value", n.value);

					try
					{
						db.insert(VitaminME_DB.TABLE_NUTRIENTS_LIST, null,
								values);
					}
					catch (Exception ex)
					{
						System.out
								.println("Error adding items to Nutrient List DB: "
										+ ex.getMessage());
					}
				}
			}

			DateTimeFormatter formatter = DateTimeFormat
					.forPattern("yyyy-MM-dd");
			DateTime today = new DateTime();
			new DataStore(context).setString("NutrientsDB_LastUpdate",
					formatter.print(today));
			System.out.println("Added Nutrients to DB on "
					+ formatter.print(today));
		}
	}

	public int getNutrientCount()
	{
		Cursor cursor = db.query(VitaminME_DB.TABLE_NUTRIENTS_LIST, null, null,
				null, null, null, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	// FIX THIS AYUSH
	private Recipe cursorToRecipe(Cursor cursor)
	{
		Recipe recipe = new Recipe();
		recipe.id = cursor.getString(cursor
				.getColumnIndex(VitaminME_DB.RECIPE_ID));
		return recipe;
	}

	private Nutrient cursorToNutrient(Cursor cursor)
	{
		Nutrient nutrient = new Nutrient();
		nutrient.id = cursor.getInt(cursor
				.getColumnIndex(VitaminME_DB.NUTRIENT_ID));
		nutrient.name = cursor.getString(cursor.getColumnIndex("name"));
		nutrient.tag = cursor.getString(cursor.getColumnIndex("tagname"));
		nutrient.unit = cursor.getString(cursor.getColumnIndex("unit"));
		nutrient.info = cursor.getString(cursor.getColumnIndex("info"));
		nutrient.daily_value = cursor.getFloat(cursor
				.getColumnIndex("daily_value"));
		return nutrient;
	}

}
