package com.vitaminme.database;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vitaminme.schema.Favorite_recipes;
import com.vitaminme.schema.Images;
import com.vitaminme.schema.Recent_recipes;
import com.vitaminme.schema.Recipes;
import com.vitaminme.schema.Recipe_courses;
import com.vitaminme.schema.Recipe_images;
import com.vitaminme.schema.Recipe_nutrients;
import com.vitaminme.schema.Schema;
import com.vitaminme.schema.Sources;

public class VitaminME_DB extends SQLiteOpenHelper
{
	public static final String COLUMN_ID = "_id";	
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "VitaminME_DB";
	
	private ArrayList<Schema> tables = new ArrayList<Schema>() {{
		add(new Favorite_recipes());
		add(new Recent_recipes());
		add(new Images());
		add(new Recipes());
		add(new Sources());
		add(new Recipe_courses());
		add(new Recipe_images());
		add(new Recipe_nutrients());
	}};
	
	private Context context;
	private SQLiteDatabase db;	

	public VitaminME_DB(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		this.db = db;
		
		for(Schema s : tables) {
			db.execSQL(s.create_statement());
		}		
	}	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
	}				
	
}
