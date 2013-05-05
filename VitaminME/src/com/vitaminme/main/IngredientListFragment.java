package com.vitaminme.main;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vitaminme.api.ApiAdapter;
import com.vitaminme.api.ApiFilter;
import com.vitaminme.api.ApiFilterOp;
import com.vitaminme.data.Ingredient;
import com.vitaminme.exceptions.APICallException;
import com.vitaminme.recipelist.RecipeList;

public class IngredientListFragment extends Fragment {
	private ListView lv;
	private Vibrator vib;
	Boolean searched = false;
	CharSequence search;
	ArrayAdapter<String> adapter;
	EditText inputSearch;
	ArrayList<HashMap<String, String>> productList;
	ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	ProgressDialog mDialog;
	ImageButton x;
	Activity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		activity.setTitle(R.string.title_fragment_search_ingredients);

		ViewGroup vg = (ViewGroup) inflater.inflate(
				R.layout.fragment_ingredient_list, null);

		new getIngredients().execute();

		vib = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

		lv = (ListView) vg.findViewById(R.id.list_view);
		inputSearch = (EditText) vg.findViewById(R.id.inputSearch);
		x = (ImageButton) vg.findViewById(R.id.x_button);
		x.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vib.vibrate(20);
				inputSearch.setText("");
				new getIngredients().execute();
				x.setVisibility(View.INVISIBLE);
				adapter.notifyDataSetChanged();
				
			}
		});

		ImageButton check = (ImageButton) vg.findViewById(R.id.go_button);
		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean next = false;
				vib.vibrate(20);
				// PopUp();
				// Toast.makeText(getBaseContext(), "next activity",
				// Toast.LENGTH_SHORT).show();
				for (Ingredient n : ingredients) {
					if (n.value == 1 || n.value == -1)
						next = true;
				}

				if (next) {
					Intent intent = new Intent(activity, RecipeList.class);
					intent.putExtra("Ingredients", ingredients);
					startActivity(intent);
				} else
					Toast.makeText(activity.getBaseContext(),
							"Nothing selected", Toast.LENGTH_SHORT).show();

			}
		});

		ImageButton list = (ImageButton) vg.findViewById(R.id.list_button);
		list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vib.vibrate(20);
				PopUpSelection();

			}
		});

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				
				search = cs.toString();								
				
				try {
					if (inputSearch.getText().toString().equals("")) {
						x.setVisibility(View.INVISIBLE);
					} else {
						x.setVisibility(View.VISIBLE);
					}
					
					if(cs.length() == 3 | cs.length() == 4  && !searched)	{
						ApiFilter filter = new ApiFilter("term", ApiFilterOp.like, cs.toString());
						new getIngredients().execute(filter);
						searched = true;
					}
					if(cs.length() == 2 && searched){
						ApiFilter filter = new ApiFilter("term", ApiFilterOp.like, cs.toString());
						new getIngredients().execute(filter);
						searched = false;
					}
					
					IngredientListFragment.this.adapter.getFilter().filter(cs);

					
				} catch (Exception ex) {
					System.out.println("Ingredient Search ontextchanged: "
							+ ex.getMessage());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});

		return vg;
	}

	public void onStart() {
		super.onStart();
		// Toast.makeText(activity.getApplicationContext(), "started",
		// Toast.LENGTH_LONG).show();
		// new getNutrients().execute();
	}

	private void PopUpSelection() {

		AlertDialog.Builder box = new AlertDialog.Builder(activity);
		ArrayList<Ingredient> selectedMinus = new ArrayList<Ingredient>();
		ArrayList<Ingredient> selectedPlus = new ArrayList<Ingredient>();
		String list = "";
		boolean empty = false;
		for (int i = 0; i < ingredients.size(); i++) {
			if (ingredients.get(i).value == -1) {
				selectedMinus.add(ingredients.get(i));
			} else if (ingredients.get(i).value == 1) {
				selectedPlus.add(ingredients.get(i));
			}
		}
		box.setTitle("Selected Ingredients");
		for (Ingredient n : selectedPlus) {
			list = list + " + " + n.term.toString() + "\n";
		}
		for (Ingredient n : selectedMinus) {
			list = list + " - " + n.term.toString() + "\n";
		}
		if (list == "") {
			list = "No ingredients selected";
			empty = true;
		}
		box.setMessage(list);
		box.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// do nothing
			}
		});
		if (!empty) {
			box.setNegativeButton("Remove All",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							for (int i = 0; i < ingredients.size(); i++) {
								ingredients.get(i).value = 0;
								adapter.notifyDataSetChanged();
							}
						}
					});
		}

		AlertDialog helpDialog = box.create();
		helpDialog.show();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private final class getIngredients extends
			AsyncTask<ApiFilter, Void, ArrayList<Ingredient>> {

		private ProgressDialog mDialog;
		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(activity);
			mDialog.setMessage("Loading...");
			mDialog.setCancelable(false);
//			mDialog.show();
			
		}

		@Override
		protected ArrayList<Ingredient> doInBackground(ApiFilter... arg) {
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("count", "1000"));
			List<ApiFilter> filters = new ArrayList<ApiFilter>();
			
			for(ApiFilter f : arg) {
				filters.add(f);
			}
			// 9172 ingredients in db
			
			// example filter
			//filters.add(new ApiFilter("term", ApiFilterOp.like, "alt"));			

			try {
				return api.getIngredients(params, filters);
			} catch (APICallException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(final ArrayList<Ingredient> nut) {
			// if (mDialog.isShowing())
			// {
			// mDialog.hide();
			// }

			if (nut != null && nut.size() > 0) {
				ingredients = nut;
				adapter = new IngredientListAdapter(activity,
						R.layout.nutrient_list_item_wbuttons, ingredients);

				lv.setAdapter(adapter);
				lv.setTextFilterEnabled(true);

			} else if (nut == null) {
				// @Mayank: not sure what the context should be for the toast
				Toast.makeText(activity, "No network found", Toast.LENGTH_LONG)
						.show();
			} else if (nut.size() == 0) {
				// @Mayank: not sure what the context should be for the toast
				Toast.makeText(activity, "No ingredients found",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(activity,
						"There was an error. Please try again",
						Toast.LENGTH_LONG).show();
			}

			if (mDialog.isShowing())
				mDialog.dismiss();
			
			
		}		
	}


}
