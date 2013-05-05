package com.vitaminme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VitaminME_DB extends SQLiteOpenHelper
{
	public static final String COLUMN_ID = "_id";
	public static final String TABLE_STARRED_RECIPES = "starred_recipes";
	public static final String TABLE_RECENTLY_VIEWED = "recently_viewed";
	public static final String RECIPE_ID = "recipe_id";

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "VitaminME_DB";

	// @formatter:off
	// FIX THIS AYUSH
	private static final String CREATE_TABLE_STARRED_RECIPES = 
			"CREATE TABLE "	+ TABLE_STARRED_RECIPES + " (" + 
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			RECIPE_ID + " TEXT, " +
			"name" + " TEXT" + 
			"images" + " TEXT" + 
			"courses" + " TEXT" +
			"cooking_time" + " TEXT" + 
			"source" + " TEXT" + 
			"nutrients"	+ " TEXT" + 
			"energy" + " TEXT" + 
			"serving_size" + " INTEGER" +
			");";
	private static final String CREATE_TABLE_RECENTLY_VIEWED = 
			"CREATE TABLE "	+ TABLE_RECENTLY_VIEWED + " (" + 
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
			RECIPE_ID + " TEXT, " +
			"name" + " TEXT" + 
			"images" + " TEXT" + 
			"courses" + " TEXT" +
			"cooking_time" + " TEXT" + 
			"source" + " TEXT" + 
			"nutrients"	+ " TEXT" + 
			"energy" + " TEXT" + 
			"serving_size" + " INTEGER" +
			");";
	// @formatter:on

	public VitaminME_DB(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_TABLE_STARRED_RECIPES);
		db.execSQL(CREATE_TABLE_RECENTLY_VIEWED);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

}
