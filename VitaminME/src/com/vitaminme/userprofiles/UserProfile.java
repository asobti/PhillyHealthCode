package com.vitaminme.userprofiles;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.vitaminme.api.ApiAdapter;
import com.vitaminme.api.ApiFilter;
import com.vitaminme.api.ApiFilterOp;
import com.vitaminme.data.Ingredient;
import com.vitaminme.exceptions.APICallException;
import com.vitaminme.main.BaseActivity;
import com.vitaminme.main.R;
import com.facebook.*;
import com.facebook.model.*;

public class UserProfile extends BaseActivity {
	private Vibrator vib;
	boolean firstStart = true;
	boolean searched = false;
	Button addIgnoreButton;
	ListView excludesListView;
	ArrayAdapter<String> ignoreSearchAdapter;
	ExcludesListAdapter excludesAdapter;
	TextWatcher searchFieldWatcher;
	ImageButton x;
	List<String> myExcludesList = new ArrayList<String>();
	AutoCompleteTextView searchBarIngredients;
	ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	ProgressDialog mDialog;
	List<String> ingredientsArray = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

		try {
			PackageInfo info;
			info = getPackageManager().getPackageInfo("com.vitaminme.main",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.v("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));// g
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Facebook Login
		final Button fbLogin = (Button) findViewById(R.id.login_button);
		fbLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Session.openActiveSession(UserProfile.this, true,
						new Session.StatusCallback() {

							// callback when session changes state
							@Override
							public void call(Session session,
									SessionState state, Exception exception) {
								if (session.isOpened()) {
									// make request to the /me API
									Request.executeMeRequestAsync(session,
											new Request.GraphUserCallback() {

												// callback after Graph API
												// response with user
												// object
												@Override
												public void onCompleted(
														GraphUser user,
														Response response) {
													if (user != null) {
														// fbLogin.setVisibility(View.INVISIBLE);
														TextView loginResults = (TextView) findViewById(R.id.login_info);
														loginResults.setText("You are logged into Facebook as "
																+ user.getName() + "\n" + " ID = " + user.getId());
													}
												}
											});

								}

							}
						});

			}

		});

		// Common diet Spinner
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!firstStart) {
					// need diet object with ingredients
					// veg Example
					myExcludesList.add("All Meats");
					myExcludesList.add("All Dairy");
					excludesAdapter.notifyDataSetChanged();

				} else {
					firstStart = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// Ignore List (replace with user class data)
		// TextView message = (TextView) findViewById(R.id.message);
		// if (!myExcludesList.isEmpty()) {
		// message.setVisibility(View.INVISIBLE);
		// }
		excludesAdapter = new ExcludesListAdapter(UserProfile.this,
				myExcludesList) {
			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
				setListViewHeight(excludesListView);
			}

		};
		excludesListView = (ListView) findViewById(R.id.excludes_list);
		excludesListView.setAdapter(excludesAdapter);
		setListViewHeight(excludesListView);

		// Ingredient autocomplete serach
		for (int i = 0; i < ingredients.size(); i++) {
			ingredientsArray.add(ingredients.get(i).term.toString());
		}

		addIgnoreButton = (Button) findViewById(R.id.addIgnoreButton);
		addIgnoreButton.setVisibility(View.INVISIBLE);
		x = (ImageButton) findViewById(R.id.x_button);
		x.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				vib.vibrate(20);
				searchBarIngredients.setText("");
				x.setVisibility(View.INVISIBLE);
				ignoreSearchAdapter.notifyDataSetChanged();

			}
		});
		// ignoreSearchAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_dropdown_item_1line, ingredientsArray);
		searchBarIngredients = (AutoCompleteTextView) findViewById(R.id.searchBar);
		// searchBarIngredients.setAdapter(ignoreSearchAdapter);
		searchFieldWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (searchBarIngredients.getText().toString().equals("")) {
					x.setVisibility(View.INVISIBLE);
				} else {
					x.setVisibility(View.VISIBLE);
				}

				if (cs.length() == 3 | cs.length() == 4 && !searched) {
					ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
							searchBarIngredients.getText().toString());
					new getIngredients().execute(filter);
					searchBarIngredients.showDropDown();
					searched = true;
				}
				if (cs.length() == 2 && searched) {
					ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
							searchBarIngredients.getText().toString());
					new getIngredients().execute(filter);
					searchBarIngredients.showDropDown();
					searched = false;
				}

				addIgnoreButton.setVisibility(View.INVISIBLE);
				ApiFilter filter = new ApiFilter("term", ApiFilterOp.like,
						searchBarIngredients.getText().toString());
				new getIngredients().execute(filter);

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		};
		searchBarIngredients.addTextChangedListener(searchFieldWatcher);
		searchBarIngredients.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				x.setVisibility(View.INVISIBLE);
				addIgnoreButton.setVisibility(View.VISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						searchBarIngredients.getWindowToken(), 0);
				addIgnoreButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						vib.vibrate(20);
						boolean inList = false;
						for (int i = 0; i < myExcludesList.size(); i++) {
							if (myExcludesList.get(i).equals(
									searchBarIngredients.getText().toString())) {
								Log.v("mytag", "inside if");
								Toast.makeText(
										UserProfile.this,
										"You already have "
												+ searchBarIngredients
														.getText().toString()
												+ " in your list",
										Toast.LENGTH_LONG).show();
								inList = true;
							}
						}
						if (!inList) {
							myExcludesList.add(0, searchBarIngredients
									.getText().toString());
							excludesAdapter.notifyDataSetChanged();
						}
						searchBarIngredients.setText("");
					}

				});

			}

		});

	}

	public static void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	private final class getIngredients extends
			AsyncTask<ApiFilter, Void, ArrayList<Ingredient>> {

		private ProgressDialog mDialog;
		private final ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(UserProfile.this);
			mDialog.setMessage("Loading...");
			mDialog.setCancelable(false);
			// mDialog.show();

		}

		@Override
		protected ArrayList<Ingredient> doInBackground(ApiFilter... arg) {
			ArrayList<Entry<String, String>> params = new ArrayList<Entry<String, String>>();
			params.add(new SimpleEntry<String, String>("count", "1000"));
			List<ApiFilter> filters = new ArrayList<ApiFilter>();

			for (ApiFilter f : arg) {
				filters.add(f);
			}
			// 9172 ingredients in db

			// example filter
			// filters.add(new ApiFilter("term", ApiFilterOp.like, "alt"));

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
				ingredientsArray.clear();
				for (int i = 0; i < ingredients.size(); i++) {
					ingredientsArray.add(ingredients.get(i).term.toString());
				}
				Log.v("mytag",
						"ingredients found = " + ingredientsArray.toString());
				ignoreSearchAdapter = new ArrayAdapter<String>(
						UserProfile.this,
						android.R.layout.simple_dropdown_item_1line,
						ingredientsArray);
				searchBarIngredients = (AutoCompleteTextView) findViewById(R.id.searchBar);
				searchBarIngredients.setAdapter(ignoreSearchAdapter);
				// searchBarIngredients.setTextFilterEnabled(true);

			} else if (nut == null) {
				// @Mayank: not sure what the context should be for the toast
				Toast.makeText(UserProfile.this, "No network found",
						Toast.LENGTH_LONG).show();
			} else if (nut.size() == 0) {
				// @Mayank: not sure what the context should be for the toast
				Toast.makeText(UserProfile.this, "No ingredients found",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(UserProfile.this,
						"There was an error. Please try again",
						Toast.LENGTH_LONG).show();
			}

			if (mDialog.isShowing())
				mDialog.dismiss();

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
