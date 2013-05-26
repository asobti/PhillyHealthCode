package com.vitaminme.recipe;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;
import com.vitaminme.android.BaseActivity;
import com.vitaminme.android.R;
import com.vitaminme.api.ApiAdapter;
import com.vitaminme.data.Recipe;

public class RecipeDetails extends BaseActivity
{

	String recipe_id = "";
	private Recipe recipe;
	private android.support.v4.view.ViewPager myViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe);

		recipe_id = getIntent().getStringExtra("recipe_id");
		new GetRecipe().execute(recipe_id);
	}

	private void setUpView()
	{

		setContentView(R.layout.activity_view_pager);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		myViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);

		ViewPagerAdapter myPagerAdapter = new ViewPagerAdapter(RecipeDetails.this,
				getSupportFragmentManager(), recipe);
		myViewPager.setOffscreenPageLimit(3);
		myViewPager.setAdapter(myPagerAdapter);
		
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(myViewPager);
		myViewPager.setCurrentItem(1);
		

	}


	private final class GetRecipe extends AsyncTask<String, Void, Recipe>
	{
		ProgressDialog mDialog;
		private ApiAdapter api = ApiAdapter.getInstance();

		@Override
		protected void onPreExecute()
		{
			mDialog = new ProgressDialog(RecipeDetails.this);
			mDialog.setMessage(getResources().getText(R.string.loading_message));
			mDialog.setCancelable(false);
			mDialog.show();
		}

		@Override
		protected Recipe doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			try
			{
				return api.getRecipe(params[0]);
			}
			catch (Exception e)
			{
				return null;
			}
		}

		@Override
		protected void onPostExecute(Recipe r)
		{
			if (r != null)
			{
				recipe = r;
				setTitle(recipe.name);

				if (mDialog.isShowing())
					mDialog.dismiss();

				setUpView();
				//setTab();

			}
			else
			{
				if (mDialog.isShowing())
					mDialog.dismiss();
				Toast.makeText(RecipeDetails.this, "Internet Connection Issue",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
