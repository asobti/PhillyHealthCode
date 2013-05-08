package com.vitaminme.database;

import android.app.IntentService;
import android.content.Intent;

public class UpdateNutrientsDB extends IntentService
{

	public UpdateNutrientsDB()
	{
		super("UpdateNutrientsDB");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		System.out.println("Updating Nutrients DB");
		VitaminME_DB db = new VitaminME_DB(getBaseContext());
		db.getWritableDatabase().execSQL(
				"DELETE FROM " + VitaminME_DB.TABLE_NUTRIENTS_LIST);
		db.addNutrients();
		db.close();
	}

}
