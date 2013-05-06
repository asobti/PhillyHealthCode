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

import com.vitaminme.data.Ingredient;
import com.vitaminme.main.R;
import com.vitaminme.main.R.drawable;
import com.vitaminme.main.R.id;
import com.vitaminme.main.R.layout;

public class IngredientListAdapter extends ArrayAdapter<String> implements
		Filterable {
	private Context context;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Ingredient> ingredientsall;
	private Filter filter;
	private Vibrator vib;
	private final Object mLock = new Object();

	public IngredientListAdapter(Context context, int textViewResourceId,
			ArrayList<Ingredient> ingredients) {
		super(context, textViewResourceId);
		this.context = context;
		this.ingredients = ingredients;
		ingredientsall = ingredients;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.nutrient_list_item_wbuttons, parent,
				false);
		vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		TextView nutrientText = (TextView) v.findViewById(R.id.nutrient_name);
		nutrientText.setText(ingredients.get(position).term);

		nutrientText.setSelected(true);
		nutrientText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder box = new AlertDialog.Builder(context);
				box.setTitle(ingredients.get(position).term);
				box.setMessage(ingredients.get(position).term + " info");
				box.setPositiveButton("Go Back",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						});
				AlertDialog helpDialog = box.create();
				helpDialog.show();

			}

		});

		ImageButton plus = (ImageButton) v.findViewById((R.id.plus_icon));
		ImageButton minus = (ImageButton) v.findViewById(R.id.minus_icon);

		if (ingredients.get(position).value == 0) {
			plus.setImageResource(R.drawable.plus_icon_empty);
			minus.setImageResource(R.drawable.minus_icon_empty);
		} else if (ingredients.get(position).value == -1) {
			plus.setImageResource(R.drawable.plus_icon_empty);
			minus.setImageResource(R.drawable.minus_icon_full);
		} else {
			plus.setImageResource(R.drawable.plus_icon_full);
			minus.setImageResource(R.drawable.minus_icon_empty);
		}

		plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				vib.vibrate(20);

				if (ingredients.get(position).value == 0) {
					ingredients.get(position).value = 1;
				} else if (ingredients.get(position).value == -1) {
					ingredients.get(position).value = 1;

				} else if (ingredients.get(position).value == 1) {
					ingredients.get(position).value = 0;
				}

				notifyDataSetChanged();
			}
		});

		minus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				vib.vibrate(20);

				if (ingredients.get(position).value == 0) {
					ingredients.get(position).value = -1;
				} else if (ingredients.get(position).value == 1) {
					ingredients.get(position).value = -1;
				} else if (ingredients.get(position).value == -1) {
					ingredients.get(position).value = 0;
				}
				notifyDataSetChanged();

			}
		});

		return v;
	}

	public Filter getFilter() {
		if (filter == null) {
			filter = new MyFilter();
		}
		return filter;
	}

	public int getCount() {
		return ingredients.size();
	}

	public String getItem(int position) {
		return ingredients.get(position).toString();
	}

	public long getItemId(int position) {
		return position;
	}

	private class MyFilter extends Filter {

		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			// reset if prefix = 0
			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					results.values = ingredientsall;
					results.count = ingredientsall.size();
				}
			} else {

				String prefixString = prefix.toString().toLowerCase();
				ArrayList<Ingredient> items = (ArrayList<Ingredient>) ingredientsall;
				int count = items.size();
				final ArrayList<Ingredient> newItems = new ArrayList<Ingredient>(
						count);
				for (int i = 0; i < count; i++) {
					Log.v("mylog", "for");
					final String item = items.get(i).term.toString();
					final String itemName = items.get(i).term.toString()
							.toLowerCase();

					if (!containsSpace(prefixString)) {
						if (itemName.startsWith(prefixString)) {
							newItems.add(items.get(i));
						} else {

							final String[] words = item.split(" ");
							final int wordCount = words.length;
							for (int k = 0; k < wordCount; k++) {
								if (words[k].startsWith(prefixString)) {
									newItems.add(items.get(i));
									break;
								}
							}
						}
					} else {
						int spaceIndex = prefixString.indexOf(" ");
						String ps1=prefixString.substring(0, spaceIndex);
						String ps2=prefixString.substring(spaceIndex + 1);

						if (itemName.startsWith(prefixString)) {
							newItems.add(items.get(i));
//						} else {
//
//							final String[] words = item.split(" ");
//							final int wordCount = words.length;
//							for (int k = 0; k < wordCount; k++) {
//								if (words[k].startsWith(ps1)) {
//									newItems.add(items.get(i));
//									break;
//								}
//							}
						}
						if (itemName.startsWith(ps2)){
							final String[] words = item.split(" ");
							final int wordCount = words.length;
							for (int k = 0; k < wordCount; k++) {
								if (words[k].startsWith(ps1)) {
									newItems.add(items.get(i));
									break;
								}
							}
						}
						
						
					}
				}

				results.values = newItems;
				results.count = newItems.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		protected void publishResults(CharSequence prefix, FilterResults results) {
			ingredients = (ArrayList<Ingredient>) results.values;
			// inform adapter of change
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

	public static boolean containsSpace(final String s) {
		if (s != null) {
			for (int i = 0; i < s.length(); i++) {
				if (Character.isWhitespace(s.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

}
