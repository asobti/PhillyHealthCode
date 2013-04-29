package com.vitaminme.userprofiles;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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

import com.vitaminme.main.BaseActivity;
import com.vitaminme.main.R;

public class UserProfile extends BaseActivity
{
	private Vibrator vib;
	boolean firstStart = true;
	Button addIgnoreButton;
	ListView excludesListView;
	ArrayAdapter<String> ignoreSearchAdapter;
	ExcludesListAdapter excludesAdapter;
	TextWatcher searchFieldWatcher;
	List<String> myExcludesList = new ArrayList<String>();
	AutoCompleteTextView searchBarIngredients;
	String[] allIngredients = { "apple", "orange", "pear", "apple sauce",
			"perl", "appetizer" };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		// Common diet Spinner
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3)
			{
				if (!firstStart)
				{
					// need diet object with ingredients
					// veg Example
					myExcludesList.add("All Meats");
					myExcludesList.add("All Dairy");
					excludesAdapter.notifyDataSetChanged();

				}
				else
				{
					firstStart = false;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub

			}
		});

		// Info Button Ingredients
		ImageButton infoIngredients = (ImageButton) findViewById(R.id.infoIconIngredients);
		infoIngredients.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				AlertDialog.Builder box = new AlertDialog.Builder(
						UserProfile.this);
				box.setTitle("Help");
				box.setMessage("Add ingredients to this list to ignore them in your recipie searches");
				box.setPositiveButton("Go Back",
						new DialogInterface.OnClickListener()
						{

							public void onClick(DialogInterface dialog,
									int which)
							{
								// do nothing, go back
							}
						});

				AlertDialog helpDialog = box.create();
				helpDialog.show();
			}

		});
		// User Data Button Ingredients
		ImageButton infoUserData = (ImageButton) findViewById(R.id.infoIconUserData);
		infoUserData.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				AlertDialog.Builder box = new AlertDialog.Builder(
						UserProfile.this);
				box.setTitle("Help");
				box.setMessage("help for user data");
				box.setPositiveButton("Go Back",
						new DialogInterface.OnClickListener()
						{

							public void onClick(DialogInterface dialog,
									int which)
							{
								// do nothing, go back
							}
						});

				AlertDialog helpDialog = box.create();
				helpDialog.show();

			}

		});
		// Ignore List (replace with user class data)

		excludesAdapter = new ExcludesListAdapter(UserProfile.this,
				myExcludesList)
		{
			@Override
			public void notifyDataSetChanged()
			{
				super.notifyDataSetChanged();
				setListViewHeight(excludesListView);
			}

		};
		excludesListView = (ListView) findViewById(R.id.excludes_list);
		excludesListView.setAdapter(excludesAdapter);
		setListViewHeight(excludesListView);

		// Ingredient autocomplete serach

		addIgnoreButton = (Button) findViewById(R.id.addIgnoreButton);
		addIgnoreButton.setVisibility(View.INVISIBLE);
		ignoreSearchAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, allIngredients);
		searchBarIngredients = (AutoCompleteTextView) findViewById(R.id.searchBar);
		searchBarIngredients.setAdapter(ignoreSearchAdapter);
		searchFieldWatcher = new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3)
			{
				addIgnoreButton.setVisibility(View.INVISIBLE);

			}

			@Override
			public void afterTextChanged(Editable arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3)
			{
				// TODO Auto-generated method stub

			}
		};
		searchBarIngredients.addTextChangedListener(searchFieldWatcher);
		searchBarIngredients.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3)
			{
				addIgnoreButton.setVisibility(View.VISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						searchBarIngredients.getWindowToken(), 0);
				addIgnoreButton.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View arg0)
					{
						vib.vibrate(20);
						boolean inList = false;
						for (int i = 0; i < myExcludesList.size(); i++)
						{
							if (myExcludesList.get(i).equals(
									searchBarIngredients.getText().toString()))
							{
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
						if (!inList)
						{
							myExcludesList.add(0, searchBarIngredients
									.getText().toString());
							excludesAdapter.notifyDataSetChanged();
						}
						searchBarIngredients.setText("");
					}

				});

			}

		});

		// User data

		final EditText editHeight = (EditText) findViewById(R.id.editHeight);
		editHeight.setOnEditorActionListener(new OnEditorActionListener()
		{

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2)
			{
				if (!editHeight.getText().toString().equals(""))
				{
					// editHeight.setText(editHeight.getText().toString() +
					// " inches");
					editHeight.setBackgroundColor(Color.TRANSPARENT);
					editHeight.setTextColor(Color.BLACK);
				}
				else
				{
					editHeight.setBackgroundColor(getResources().getColor(
							R.color.light_gray));
				}
				return false;
			}

		});
		final EditText editWeight = (EditText) findViewById(R.id.editWeight);
		editWeight.setOnEditorActionListener(new OnEditorActionListener()
		{

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2)
			{
				TextView lbs = (TextView) findViewById(R.id.lbs);
				TextView inches = (TextView) findViewById(R.id.inches);

				if (!editWeight.getText().toString().equals(""))
				{
					// editWeight.setText(editWeight.getText().toString() +
					// " lbs");
					editWeight.setBackgroundColor(Color.TRANSPARENT);
					editWeight.setTextColor(Color.BLACK);
					inches.setVisibility(View.VISIBLE);
					lbs.setVisibility(View.VISIBLE);
				}
				else
				{
					editWeight.setBackgroundColor(getResources().getColor(
							R.color.light_gray));
					inches.setVisibility(View.INVISIBLE);
					lbs.setVisibility(View.INVISIBLE);
				}
				return false;
			}

		});

	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item)
	// {
	// switch (item.getItemId())
	// {
	// // case android.R.id.home:
	// // {
	// // onBackPressed();
	// // finish();
	// // return true;
	// // }
	// case R.id.save:
	// // Save user profile
	// Toast.makeText(UserProfile.this, "Saved!", Toast.LENGTH_LONG)
	// .show();
	// default:
	// {
	// return super.onOptionsItemSelected(item);
	// }
	// }
	//
	// }

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.user_profile, menu);
	// return true;
	// }

	public static void setListViewHeight(ListView listView)
	{
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
		{
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++)
		{
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

}
