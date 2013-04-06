package com.vitaminme;

import android.app.Activity;
import android.content.SharedPreferences;

public class DataStore {
	
	final static String PREF_NAME = "Preferences";
	
	Activity context;
	SharedPreferences preferences;
	
	public DataStore(Activity context) {
		this.context = context;
		preferences = this.context.getSharedPreferences(PREF_NAME, 0);
	}
	
	public int getInt(String key, int def) {
		return this.preferences.getInt(key, def);
	}
	
	public String getString(String key, String def) {
		return this.preferences.getString(key, def);
	}
	
	public boolean getBoolean(String key, boolean def) {
		return this.preferences.getBoolean(key, def);
	}
	
	public void setInt(String key, int value) {
		SharedPreferences.Editor editor = this.preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public void setString(String key, String value) {
		SharedPreferences.Editor editor = this.preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public void setBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = this.preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	
}
