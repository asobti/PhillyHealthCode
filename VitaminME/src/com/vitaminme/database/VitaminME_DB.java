package com.vitaminme.database;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
	public static final String TABLE_NUTRIENTS_LIST = "nutrients";
	public static final String RECIPE_ID = "recipe_id";
	public static final String NUTRIENT_ID = "nutrient_id";

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "VitaminME_DB";

	private Context context;
	private SQLiteDatabase db;

	private static String DATABASE_PATH = "/data/data/com.vitaminme.android/databases/";

	// private static String DATABASE_PATH = "/data/data/"
	// + context.getApplicationContext().getPackageName() + "/databases/";
	// private static String DATABASE_PATH = context.getApplicationContext()
	// .getFilesDir().getParentFile().getPath()
	// + "/databases/";

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
		System.out.println("Creating DB tables...");

		this.db = db;

		 db.execSQL(CREATE_TABLE_STARRED_RECIPES);

		 db.execSQL(CREATE_TABLE_RECENTLY_VIEWED);

		// db.execSQL(CREATE_TABLE_NUTRIENTS_LIST);
		// addNutrients();
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

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

}
