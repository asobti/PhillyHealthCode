package com.vitaminme.home;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vitaminme.data.Ingredient;
import com.vitaminme.main.R;

public class IngredientListAdapter extends ArrayAdapter<String> implements
		Filterable {
	private Context context;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Ingredient> ingredientsall;
	ArrayList<Ingredient> myIngredients;
	private Filter filter;
	private Vibrator vib;
	private final Object mLock = new Object();
	// private Ingredient i;

	AtomicReference<Object> selectionRef;

	public IngredientListAdapter(Context context,
			ArrayList<Ingredient> ingredients,
			AtomicReference<Object> selectionRef) {
		super(context, R.layout.fragment_search_list_item);
		this.context = context;
		this.ingredients = ingredients;
		this.myIngredients = (ArrayList<Ingredient>) selectionRef.get();
		this.ingredientsall = ingredients;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.fragment_search_list_item, parent,
				false);
		vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		final Ingredient thisIngredient = ingredients.get(position);
		TextView nutrientText = (TextView) v.findViewById(R.id.itemName);
		nutrientText.setText(thisIngredient.term);

		ImageButton plus = (ImageButton) v.findViewById((R.id.plus_icon));
		ImageButton minus = (ImageButton) v.findViewById(R.id.minus_icon);
		int myIndex = 0;
		for (Ingredient i : myIngredients) {

			if (i.term.toString().equals(thisIngredient.term.toString())) {
				if (i.value > 0) {
					plus.setImageResource(R.drawable.plus_green);
					minus.setImageResource(R.drawable.minus_gray);
				} else {
					plus.setImageResource(R.drawable.plus_gray);
					minus.setImageResource(R.drawable.minus_red);
				}
				thisIngredient.value = i.value;
				break;
			}
			myIndex += 1;
		}
		final int index = myIndex;

		plus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				vib.vibrate(20);
				if (thisIngredient.value == 0) {
					thisIngredient.value = 1;
					myIngredients.add(thisIngredient);

				} else if (thisIngredient.value == -1) {
					myIngredients.remove(index);
					thisIngredient.value = 1;
					myIngredients.add(thisIngredient);

				} else if (thisIngredient.value == 1) {
					myIngredients.remove(index);
					thisIngredient.value = 0;
				}
				notifyDataSetChanged();
			}
		});

		minus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				vib.vibrate(20);
				if (thisIngredient.value == 0) {
					thisIngredient.value = -1;
					myIngredients.add(thisIngredient);
				} else if (thisIngredient.value == 1) {
					myIngredients.remove(index);
					thisIngredient.value = -1;
					myIngredients.add(thisIngredient);
				} else if (thisIngredient.value == -1) {
					myIngredients.remove(index);
					thisIngredient.value = 0;
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
						String ps1 = prefixString.substring(0, spaceIndex);
						String ps2 = prefixString.substring(spaceIndex + 1);
						if (itemName.startsWith(prefixString)) {
							newItems.add(items.get(i));

						}
						if (itemName.startsWith(ps2)) {
							final String[] words = itemName.split(" ");
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
