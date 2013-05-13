package com.vitaminme.userprofiles;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class SuggestionsSimpleCursorAdapter extends SimpleCursorAdapter
{

	public SuggestionsSimpleCursorAdapter(Context context, int layout,
			Cursor c, String[] from, int[] to, int flags)
	{
		super(context, layout, c, from, to, flags);
	}

	@Override
	public CharSequence convertToString(Cursor cursor)
	{
		return cursor.getString(0);
	}

}
