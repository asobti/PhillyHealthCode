package com.vitaminme.database;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.vitaminme.api.ApiAdapter;
import com.vitaminme.data.DataStore;
import com.vitaminme.data.Nutrient;
import com.vitaminme.exceptions.APICallException;

public class VitaminME_DB extends SQLiteOpenHelper
{
	public static final String COLUMN_ID = "_id";
	public static final String TABLE_STARRED_RECIPES = "starred_recipes";
	public static final String TABLE_RECENTLY_VIEWED = "recently_viewed";
	public static final String TABLE_NUTRIENTS_LIST = "nutrients_list";
	public static final String RECIPE_ID = "recipe_id";
	public static final String NUTRIENT_ID = "nutrient_id";

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "VitaminME_DB";

	private Context context;
	private SQLiteDatabase db;

	// @formatter:off
	// FIX THIS AYUSH
	private static final String CREATE_TABLE_STARRED_RECIPES = 
			"CREATE TABLE "	+ TABLE_STARRED_RECIPES + " (" + 
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			RECIPE_ID + " TEXT, " +
			"name" + " TEXT, " + 
			"images" + " TEXT, " + 
			"courses" + " TEXT, " +
			"cooking_time" + " TEXT, " + 
			"source" + " TEXT, " + 
			"nutrients"	+ " TEXT, " + 
			"energy" + " TEXT, " + 
			"serving_size" + " INTEGER" +
			");";
	private static final String CREATE_TABLE_RECENTLY_VIEWED = 
			"CREATE TABLE "	+ TABLE_RECENTLY_VIEWED + " (" + 
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			RECIPE_ID + " TEXT, " +
			"name" + " TEXT, " + 
			"images" + " TEXT, " + 
			"courses" + " TEXT, " +
			"cooking_time" + " TEXT, " + 
			"source" + " TEXT, " + 
			"nutrients"	+ " TEXT, " + 
			"energy" + " TEXT, " + 
			"serving_size" + " INTEGER" +
			");";
	// Leave this alone
	private static final String CREATE_TABLE_NUTRIENTS_LIST = 
			"CREATE TABLE "	+ TABLE_NUTRIENTS_LIST + " (" + 
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			NUTRIENT_ID + " INTEGER, " +
			"name" + " TEXT, " + 
			"tagname" + " TEXT, " + 
			"unit" + " TEXT, " +
			"info" + " TEXT, " + 
			"daily_value" + " REAL" + 
			");";
	// @formatter:on

	private static VitaminME_DB mInstance = null;

	private VitaminME_DB(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	public static VitaminME_DB getInstance(Context context)
	{
		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		if (mInstance == null)
		{
			mInstance = new VitaminME_DB(context.getApplicationContext());
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		this.db = db;

		db.execSQL(CREATE_TABLE_STARRED_RECIPES);

		db.execSQL(CREATE_TABLE_RECENTLY_VIEWED);

		db.execSQL(CREATE_TABLE_NUTRIENTS_LIST);
		addNutrients();
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
					values.put("name", n.term);
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

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

}
