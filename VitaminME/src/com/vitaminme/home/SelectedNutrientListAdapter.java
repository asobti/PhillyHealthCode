package com.vitaminme.home;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData.Item;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vitaminme.data.Nutrient;
import com.vitaminme.main.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vitaminme.main.R;

public class SelectedNutrientListAdapter extends ArrayAdapter<String>
{
	private Context context;
	private List<String> nutrientNames = new ArrayList<String>();
	private ArrayList<Nutrient> nutrients;

	public SelectedNutrientListAdapter(Context context, List<String> nutrientNames, ArrayList<Nutrient> nutrients)
	{
		super(context, R.layout.selected_nutrients_row_view, nutrientNames);
		// TODO Auto-generated constructor stub

		this.context = context;
		this.nutrientNames = nutrientNames;
		this.nutrients = nutrients;

	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		System.out.println("recipe selected adapter");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.selected_nutrients_row_view, parent, false);

		TextView tv1 = (TextView) v.findViewById(R.id.nutrient_selected_tv);
		tv1.setSelected(true);

		tv1.setText(nutrientNames.get(position));
		tv1.setEnabled(false);
		
		if(nutrients.get(position).value > 0){
			v.setBackgroundColor(context.getResources().getColor(R.color.spotify));
			tv1.setText("+ " + nutrientNames.get(position));
		}
		else{
			v.setBackgroundColor(context.getResources().getColor(R.color.red));
			tv1.setText("- " + nutrientNames.get(position));
		}


		return v;

	}

}
