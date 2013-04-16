package com.vitaminme.recipe;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vitaminme.main.R;

public class RecipeSelectedAdapter extends ArrayAdapter<String>
{
	private Context context;
	private List<String> ingredients = new ArrayList<String>();

	public RecipeSelectedAdapter(Context context, List<String> ingredients)
	{
		super(context, R.layout.ingredients_row_view, ingredients);
		// TODO Auto-generated constructor stub

		this.context = context;
		this.ingredients = ingredients;

	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		System.out.println("recipe selected adapter");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.ingredients_row_view, parent, false);

		TextView tv1 = (TextView) v.findViewById(R.id.textView1);
		tv1.setSelected(true);

		tv1.setText(ingredients.get(position));
		tv1.setEnabled(false);


		return v;

	}

}
