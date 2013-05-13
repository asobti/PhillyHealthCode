package com.vitaminme.recipe;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.vitaminme.api.ApiAdapter;
import com.vitaminme.data.Recipe;
import com.vitaminme.main.BaseActivity;
import com.vitaminme.main.R;

public class RecipeDetails extends BaseActivity
{

	private ViewPager myViewPager;
	private ViewPagerAdapter myPagerAdapter;
	String recipe_id = "";
	private Recipe recipe;	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		recipe_id = getIntent().getStringExtra("recipe_id");
		System.out.println("recipe ID: " + recipe_id);

		new GetRecipe().execute(recipe_id);
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
	
	private final class GetRecipe extends AsyncTask<String, Void, Recipe> {
		ProgressDialog mDialog;
		private ApiAdapter api = ApiAdapter.getInstance();
		
		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(RecipeDetails.this);
			mDialog.setMessage(getResources().getText(R.string.loading_message));
			mDialog.setCancelable(false);
			mDialog.show();
		}

		@Override
		protected Recipe doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				return api.getRecipe(params[0]);
			} catch (Exception e) {
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Recipe r) {
			if (r != null) {
				recipe = r;
				setTitle(recipe.name);
				
				
				if (mDialog.isShowing())
					mDialog.dismiss();
				
				setUpView();
				setTab();
			} else {
				//toast: Internet connection issue
			}
		}		
	}
}
