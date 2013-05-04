package com.vitaminme.recipe;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.vitaminme.api.ApiCallParams;
import com.vitaminme.api.ApiCallTask;
import com.vitaminme.data.Pagination;
import com.vitaminme.data.ParseRecipe;
import com.vitaminme.data.Recipe;
import com.vitaminme.main.BaseActivity;
import com.vitaminme.main.R;

public class RecipeDetails extends BaseActivity
{

	private ViewPager myViewPager;
	private ViewPagerAdapter myPagerAdapter;
	String recipe_id = "";
	Recipe recipe = new Recipe();
	ProgressDialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		recipe_id = getIntent().getStringExtra("recipe_id");
		System.out.println("recipe ID: " + recipe_id);

		GetRecipe();
	}

	public void GetRecipe()
	{
		ApiCallParams apiParams = new ApiCallParams();
		apiParams.url = "http://vitaminme.notimplementedexception.me/recipes/"
				+ recipe_id;

		apiParams.callBackObject = new ParseRecipe(RecipeDetails.this);

		ApiCallTask task = new ApiCallTask();
		task.execute(apiParams);

		mDialog = new ProgressDialog(RecipeDetails.this);
		mDialog.setMessage(getResources().getText(R.string.loading_message));
		mDialog.setCancelable(false);
		mDialog.show();
	}

	public void callback(Recipe recipe, Pagination pagination)
	{
		// runningBG = false;
		// totalNumResults = pagination.num_results;

		// System.out.println("counter: " + counter);

		// if (counter < pagination.num_results)
		// {
		// System.out.println("num_recipe: " + recipes.size());
		// System.out.println("num_results: " + pagination.num_results);
		this.recipe = recipe;
		setTitle(recipe.name);
		// System.out.println("num_results: " + pagination.num_results);
		if (mDialog.isShowing())
			mDialog.dismiss();
		setUpView();
		setTab();

		// fillListView();
		// counter += pagination.page_results;
		// }

	}

	private void setUpView()
	{
		myViewPager = (ViewPager) this.findViewById(R.id.viewPager);
		myPagerAdapter = new ViewPagerAdapter(this,
				getSupportFragmentManager(), recipe);
		myViewPager.setAdapter(myPagerAdapter);
		myViewPager.setCurrentItem(1);
	}

	private void setTab()
	{
		myViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageScrollStateChanged(int position)
			{
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
			}

			@Override
			public void onPageSelected(int position)
			{
				switch (position)
				{
				case 0:
					findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
					findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.third_tab).setVisibility(View.INVISIBLE);
					break;

				case 1:
					findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
					findViewById(R.id.third_tab).setVisibility(View.INVISIBLE);
					break;

				case 2:
					findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.third_tab).setVisibility(View.VISIBLE);
					break;
				}
			}

		});

	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item)
	// {
	// switch (item.getItemId())
	// {
	// // case android.R.id.home:
	// // onBackPressed();
	// // finish();
	// // return true;
	// case R.id.add_favorite:
	// Toast.makeText(getBaseContext(), "Added to images (not really)",
	// Toast.LENGTH_SHORT).show();
	// case R.id.user_profile:
	// // open user profile
	// Intent intent = new Intent(this, UserProfile.class);
	// startActivity(intent);
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }
}
