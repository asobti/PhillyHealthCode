package com.vitaminme.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.vitaminme.data.Recipe;

public class VitaminME_DB_DataSource
{
	// Database fields
	private SQLiteDatabase db;
	private VitaminME_DB dbHelper;

	public VitaminME_DB_DataSource(Context context)
	{
		dbHelper = new VitaminME_DB(context);
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

	// FIX THIS AYUSH
	private Recipe cursorToRecipe(Cursor cursor)
	{
		Recipe recipe = new Recipe();
		recipe.id = cursor.getString(0);
		return recipe;
	}

}
