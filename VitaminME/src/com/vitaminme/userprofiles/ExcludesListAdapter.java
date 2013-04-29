package com.vitaminme.userprofiles;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vitaminme.main.R;

public class ExcludesListAdapter extends ArrayAdapter<String> {
	private Context context;
	private ViewGroup parent;
	private Vibrator vib;
	boolean notSure = false;
	private List<String> ingredients = new ArrayList<String>();

	public ExcludesListAdapter(Context context, List<String> ingredients) {
		super(context, R.layout.ingredient_exclude_item, ingredients);
		// TODO Auto-generated constructor stub

		this.context = context;
		this.ingredients = ingredients;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final String ingredientText = ingredients.get(position);
		this.parent = parent;
		vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.ingredient_exclude_item, parent,
				false);

		final TextView tv1 = (TextView) v.findViewById(R.id.ingredient_name);
		tv1.setSelected(true);
		tv1.setText(ingredientText);
		tv1.setEnabled(false);
		final ImageButton sureButton = (ImageButton) v
				.findViewById(R.id.cancel_remove_button);
		sureButton.setVisibility(View.INVISIBLE);
		ImageButton ingredientRemoveButton = (ImageButton) v
				.findViewById(R.id.ingredient_remove_button);
		ingredientRemoveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				vib.vibrate(20);
				if (notSure) {
					tv1.setText(ingredientText);
					sureButton.setVisibility(View.INVISIBLE);
					notSure = false;

				} else {
					tv1.setText("Are you sure?");
					sureButton.setVisibility(View.VISIBLE);
					notSure = true;
				}
			}

		});
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				vib.vibrate(20);
				ingredients.remove(position);
				sureButton.setVisibility(View.INVISIBLE);
				notifyDataSetChanged();
				notSure = false;
			}

		});

		return v;

	}

}
