package com.vitaminme.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.vitaminme.api.ApiAdapter;
import com.vitaminme.data.Nutrient;
import com.vitaminme.data.Pagination;
import com.vitaminme.exceptions.APICallException;
import com.vitaminme.recipelist.RecipeList;

public class MainActivity extends Activity {

	private ListView lv;
	private Vibrator vib;

	ArrayAdapter<String> adapter;
	EditText inputSearch;
	ArrayList<HashMap<String, String>> productList;
	ArrayList<Nutrient> nutrients = new ArrayList<Nutrient>();
	Pagination pag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Vitamin.ME");
		setContentView(R.layout.activity_main);
		
		// execute async task
		new getNutrients().execute();

		vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		ImageButton x = (ImageButton) findViewById(R.id.x_button);
		x.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vib.vibrate(20);
				inputSearch.setText("");
			}
		});

		ImageButton check = (ImageButton) findViewById(R.id.go_button);
		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean next = false;
				vib.vibrate(20);
				
				for (Nutrient n : nutrients) {
					if (n.value == 1 || n.value == -1)
						next = true;
				}

				if (next) {
					Intent intent = new Intent(MainActivity.this,
							RecipeList.class);
					intent.putExtra("Nutrients", nutrients);
					startActivity(intent);
				} else
					Toast.makeText(getBaseContext(), "Nothing selected",
							Toast.LENGTH_SHORT).show();

			}
		});

		ImageButton list = (ImageButton) findViewById(R.id.list_button);
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
				try {
					MainActivity.this.adapter.getFilter().filter(cs);
				} catch (Exception ex) {
					System.out.println("page 1 ontextchanged: "
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
	}

	private void PopUpSelection() {

		AlertDialog.Builder box = new AlertDialog.Builder(this);
		ArrayList<Nutrient> selectedMinus = new ArrayList<Nutrient>();
		ArrayList<Nutrient> selectedPlus = new ArrayList<Nutrient>();
		String list = "";
		for (int i = 0; i < nutrients.size(); i++) {
			if (nutrients.get(i).value == -1) {
				selectedMinus.add(nutrients.get(i));
			} else if (nutrients.get(i).value == 1) {
				selectedPlus.add(nutrients.get(i));
			}
		}
		box.setTitle("Selected Nutrients");
		for (Nutrient n : selectedPlus) {
			list = list + " + " + n.name.toString() + "\n";
		}
		for (Nutrient n : selectedMinus) {
			list = list + " - " + n.name.toString() + "\n";
		}
		if (list == "") {
			list = "No nutrients selected";
		}
		box.setMessage(list);
		box.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// do nothing
			}
		});

		AlertDialog helpDialog = box.create();
		helpDialog.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	
	private final class getNutrients extends AsyncTask<Void, Void, ArrayList<Nutrient>> {
		
		private ProgressDialog mDialog;
		private final ApiAdapter api = ApiAdapter.getInstance();
		
		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(MainActivity.this);
			mDialog.setMessage("Loading...");
			mDialog.setCancelable(false);
			mDialog.show();
		}
		
		@Override
		protected ArrayList<Nutrient> doInBackground(Void... arg0) {			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("count", "100");
			
			try {				
				return api.getNutrients(params);
			} catch (APICallException e) {
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(final ArrayList<Nutrient> nut) {
			if (mDialog.isShowing()) {
				mDialog.hide();
			}
			
			if (nut != null && nut.size() > 0) {
				nutrients = nut;
				pag = api.getPaginationObject();
				
				adapter = new NutrientListAdapter(MainActivity.this,
						R.layout.nutrient_list_item_wbuttons, nutrients);
				lv.setAdapter(adapter);
				lv.setTextFilterEnabled(true);
			} else if (nut.size() == 0) {
				Toast.makeText(getApplicationContext(), "No nutrients found", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "There was an error. Please try again", Toast.LENGTH_LONG).show();
			}
		}
		
	}

}
