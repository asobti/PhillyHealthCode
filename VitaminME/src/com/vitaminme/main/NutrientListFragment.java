package com.vitaminme.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.vitaminme.api.ApiCallParams;
import com.vitaminme.api.ApiCallTask;
import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Pagination;
import com.vitaminme.data.ParseNutrients;
import com.vitaminme.recipelist.RecipeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NutrientListFragment extends Fragment
{
	private ListView lv;
	private Vibrator vib;

	ArrayAdapter<String> adapter;
	EditText inputSearch;
	ArrayList<HashMap<String, String>> productList;
	ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();
	ProgressDialog mDialog;

	Activity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		activity = getActivity();
		activity.setTitle(R.string.title_fragment_search_nutrients);

		ViewGroup vg = (ViewGroup) inflater.inflate(
				R.layout.fragment_nutrient_list, null);

		mDialog = new ProgressDialog(activity);
		mDialog.setMessage("Loading...");
		mDialog.setCancelable(false);
		mDialog.show();

		ApiCallParams params = new ApiCallParams();
		params.url = "http://vitaminme.notimplementedexception.me/nutrients";
		// params.callBackObject = new ParseNutrients(activity);

		ApiCallTask task = new ApiCallTask();
//		task.execute(params);

		vib = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

		lv = (ListView) vg.findViewById(R.id.list_view);
		inputSearch = (EditText) vg.findViewById(R.id.inputSearch);
		ImageButton x = (ImageButton) vg.findViewById(R.id.x_button);
		x.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				vib.vibrate(20);
				inputSearch.setText("");
			}
		});

		ImageButton check = (ImageButton) vg.findViewById(R.id.go_button);
		check.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				boolean next = false;
				vib.vibrate(20);
				// PopUp();
				// Toast.makeText(getBaseContext(), "next activity",
				// Toast.LENGTH_SHORT).show();
				for (Nutrient n : nutrients)
				{
					if (n.value == 1 || n.value == -1)
						next = true;
				}

				if (next)
				{
					Intent intent = new Intent(activity, RecipeList.class);
					intent.putExtra("Nutrients", nutrients);
					startActivity(intent);
				}
				else
					Toast.makeText(activity.getBaseContext(),
							"Nothing selected", Toast.LENGTH_SHORT).show();

			}
		});

		ImageButton list = (ImageButton) vg.findViewById(R.id.list_button);
		list.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				vib.vibrate(20);
				PopUpSelection();

			}
		});

		inputSearch.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3)
			{
				try
				{
					adapter.getFilter().filter(cs);
				}
				catch (Exception ex)
				{
					System.out.println("page 1 ontextchanged: "
							+ ex.getMessage());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0)
			{
				// TODO Auto-generated method stub
			}
		});

		return vg;
	}

	private void PopUpSelection()
	{

		AlertDialog.Builder box = new AlertDialog.Builder(activity);
		ArrayList<Nutrient> selectedMinus = new ArrayList<Nutrient>();
		ArrayList<Nutrient> selectedPlus = new ArrayList<Nutrient>();
		String list = "";
		for (int i = 0; i < nutrients.size(); i++)
		{
			if (nutrients.get(i).value == -1)
			{
				selectedMinus.add(nutrients.get(i));
			}
			else if (nutrients.get(i).value == 1)
			{
				selectedPlus.add(nutrients.get(i));
			}
		}
		box.setTitle("Selected Nutrients");
		for (Nutrient n : selectedPlus)
		{
			list = list + " + " + n.name.toString() + "\n";
		}
		for (Nutrient n : selectedMinus)
		{
			list = list + " - " + n.name.toString() + "\n";
		}
		if (list == "")
		{
			list = "No nutrients selected";
		}
		box.setMessage(list);
		box.setPositiveButton("Go Back", new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				// do nothing
			}
		});

		AlertDialog helpDialog = box.create();
		helpDialog.show();

	}

	public void callback(ArrayList<Nutrient> nut, Pagination pag)
	{
		if (mDialog.isShowing())
			mDialog.dismiss();
		this.nutrients = nut;
		adapter = new NutrientListAdapter(activity,
				R.layout.nutrient_list_item_wbuttons, nutrients);
		lv.setAdapter(adapter);
		lv.setTextFilterEnabled(true);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

}
