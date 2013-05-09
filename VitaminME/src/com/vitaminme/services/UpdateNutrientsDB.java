package com.vitaminme.services;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.vitaminme.data.DataStore;
import com.vitaminme.database.VitaminME_DB_DataSource;

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
		System.out.println("Checking Nutrients DB for updates...");
		DateTime today = new DateTime();
		String nutrientsDB_LastUpdate = new DataStore(this).getString(
				"NutrientsDB_LastUpdate", "never");
		if (!nutrientsDB_LastUpdate.equalsIgnoreCase("never"))
		{
			System.out.println("Nutrients DB last updated "
					+ nutrientsDB_LastUpdate);

			DateTimeFormatter formatter = DateTimeFormat
					.forPattern("yyyy-MM-dd");
			DateTime lastUpdate = formatter
					.parseDateTime(nutrientsDB_LastUpdate);

			if (Days.daysBetween(today, lastUpdate).getDays() > 30)
			{
				System.out.println("Updating Nutrients DB");
				try
				{
					VitaminME_DB_DataSource ds = new VitaminME_DB_DataSource(
							this);
					ds.open();
					ds.updateNutrientsDB();
					ds.close();
				}
				catch (Exception ex)
				{
					System.out.println("Error updating Nutrients DB: "
							+ ex.getMessage());
				}
			}
		}
	}

}
