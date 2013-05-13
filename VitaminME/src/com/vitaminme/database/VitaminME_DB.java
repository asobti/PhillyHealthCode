package com.vitaminme.database;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vitaminme.schema.Favorite_recipes;
import com.vitaminme.schema.Images;
import com.vitaminme.schema.Recent_recipes;
import com.vitaminme.schema.Recipe_courses;
import com.vitaminme.schema.Recipe_images;
import com.vitaminme.schema.Recipe_nutrients;
import com.vitaminme.schema.Recipes;
import com.vitaminme.schema.Schema;
import com.vitaminme.schema.Sources;

public class VitaminME_DB extends SQLiteOpenHelper
{
	public static final String COLUMN_ID = "_id";
	public static final String TABLE_STARRED_RECIPES = "starred_recipes";
	public static final String TABLE_RECENTLY_VIEWED = "recently_viewed";
	public static final String TABLE_NUTRIENTS_LIST = "nutrients";
	public static final String RECIPE_ID = "recipe_id";
	public static final String NUTRIENT_ID = "nutrient_id";

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

	private static String DATABASE_PATH = "/data/data/com.vitaminme.android/databases/";

	// private static String DATABASE_PATH = "/data/data/"
	// + context.getApplicationContext().getPackageName() + "/databases/";
	// private static String DATABASE_PATH = context.getApplicationContext()
	// .getFilesDir().getParentFile().getPath()
	// + "/databases/";

	private static VitaminME_DB mInstance = null;


	public VitaminME_DB(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	// Open the database, so we can query it
	public boolean openDataBase() throws SQLException
	{
		String mPath = DATABASE_PATH + DATABASE_NAME;
		db = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		return db != null;
	}

	public void createDataBase() throws IOException
	{
		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist)
		{
			this.getReadableDatabase();
			this.close();
			try
			{
				copyDataBase();
			}
			catch (IOException mIOException)
			{
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		this.db = db;
		
		for(Schema s : tables) {
			String stmt = s.create_statement();
			db.execSQL(stmt);
		}		
	}
	
	private void copyDataBase() throws IOException
	{
		System.out.println("Copying/unzipping DB");

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(
				"db/" + DATABASE_NAME + ".zip");

		System.out.println("db open " + myInput);

		// Path to the just created empty db
		String outFileName = DATABASE_PATH + DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		ZipInputStream zis = new ZipInputStream(
				new BufferedInputStream(myInput));
		try
		{
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null)
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int count;
				while ((count = zis.read(buffer)) != -1)
				{
					baos.write(buffer, 0, count);
					// Log.d("", buffer.toString());
				}
				baos.writeTo(myOutput);
			}
		}
		catch (Exception ex)
		{
			System.out.println("Error copying/unzipping DB");
		}
		finally
		{
			zis.close();
			myOutput.flush();
			myOutput.close();
			myInput.close();
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	@Override
	public synchronized void close()
	{
		if (db != null)
			db.close();

		super.close();
	}

	private boolean checkDataBase()
	{
		String myPath = DATABASE_PATH + DATABASE_NAME;

		File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
		System.out.println("DB exists? " + dbFile);
		return dbFile.exists();
	}	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
	}				
	
}
