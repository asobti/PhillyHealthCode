package com.vitaminme.home;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vitaminme.data.Nutrient;
import com.vitaminme.main.R;

public class NutrientListAdapter extends ArrayAdapter<String> implements
		Filterable
{
	private Context context;
	private ArrayList<Nutrient> nutrients;
	private ArrayList<Nutrient> nutrientsall;
	private Filter filter;
	private Vibrator vib;
	private final Object mLock = new Object();

	public NutrientListAdapter(Context context, ArrayList<Nutrient> nutrients)
	{
		super(context, R.layout.fragment_search_list_item);
		this.context = context;
		this.nutrients = nutrients;
		nutrientsall = nutrients;

	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.fragment_search_list_item, parent,
				false);
		vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		TextView nutrientText = (TextView) v.findViewById(R.id.itemName);
		nutrientText.setText(nutrients.get(position).name);

		// nutrientText.setSelected(true);
		nutrientText.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				AlertDialog.Builder box = new AlertDialog.Builder(context);
				box.setTitle(nutrients.get(position).name);
				box.setMessage(nutrients.get(position).info);
				box.setPositiveButton("OK",
						new DialogInterface.OnClickListener()
						{

							public void onClick(DialogInterface dialog,
									int which)
							{
								// do nothing
							}
						});
				AlertDialog helpDialog = box.create();
				helpDialog.show();

			}

		});

		ImageButton plus = (ImageButton) v.findViewById((R.id.plus_icon));
		ImageButton minus = (ImageButton) v.findViewById(R.id.minus_icon);

		if (nutrients.get(position).value == 0)
		{
			plus.setImageResource(R.drawable.plus_gray);
			minus.setImageResource(R.drawable.minus_gray);
		}
		else if (nutrients.get(position).value == -1)
		{
			plus.setImageResource(R.drawable.plus_gray);
			minus.setImageResource(R.drawable.minus_red);
		}
		else
		{
			plus.setImageResource(R.drawable.plus_green);
			minus.setImageResource(R.drawable.minus_gray);
		}

		plus.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				vib.vibrate(20);

				if (nutrients.get(position).value == 0)
				{
					nutrients.get(position).value = 1;
				}
				else if (nutrients.get(position).value == -1)
				{
					nutrients.get(position).value = 1;

				}
				else if (nutrients.get(position).value == 1)
				{
					nutrients.get(position).value = 0;
				}

				notifyDataSetChanged();
			}
		});

		minus.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vib.vibrate(20);

				if (nutrients.get(position).value == 0)
				{
					nutrients.get(position).value = -1;
				}
				else if (nutrients.get(position).value == 1)
				{
					nutrients.get(position).value = -1;
				}
				else if (nutrients.get(position).value == -1)
				{
					nutrients.get(position).value = 0;
				}
				notifyDataSetChanged();

			}
		});

		return v;
	}

	public Filter getFilter()
	{
		if (filter == null)
		{
			filter = new MyFilter();
		}
		return filter;
	}

	public int getCount()
	{
		return nutrients.size();
	}

	public String getItem(int position)
	{
		return nutrients.get(position).toString();
	}

	public long getItemId(int position)
	{
		return position;
	}

	private class MyFilter extends Filter
	{

		protected FilterResults performFiltering(CharSequence prefix)
		{
			FilterResults results = new FilterResults();
			// reset if prefix = 0
			if (prefix == null || prefix.length() == 0)
			{
				synchronized (mLock)
				{
					results.values = nutrientsall;
					results.count = nutrientsall.size();
				}
			}
			else
			{

				String prefixString = prefix.toString().toLowerCase();
				ArrayList<Nutrient> items = (ArrayList<Nutrient>) nutrientsall;
				int count = items.size();
				final ArrayList<Nutrient> newItems = new ArrayList<Nutrient>(
						count);
				for (int i = 0; i < count; i++)
				{
					Log.v("mylog", "for");
					final String item = items.get(i).name.toString();
					final String itemName = items.get(i).name.toString()
							.toLowerCase();

					if (itemName.startsWith(prefixString))
					{
						newItems.add(items.get(i));
					}
					else
					{

						final String[] words = item.split(" ");
						final int wordCount = words.length;
						for (int k = 0; k < wordCount; k++)
						{
							if (words[k].startsWith(prefixString))
							{
								newItems.add(items.get(i));
								break;
							}
						}
					}
				}

				results.values = newItems;
				results.count = newItems.size();
			}
			return results;
		}

		protected void publishResults(CharSequence prefix, FilterResults results)
		{
			nutrients = (ArrayList<Nutrient>) results.values;
			// inform adapter of change
			if (results.count > 0)
			{
				notifyDataSetChanged();
			}
			else
			{
				notifyDataSetInvalidated();
			}
		}
	}
}
