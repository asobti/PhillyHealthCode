package com.vitaminme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vitaminme.api.ApiCallParams;
import com.vitaminme.api.ApiCallTask;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
 
public class MainActivity extends Activity {

    private ListView lv;
    
    ArrayAdapter<String> adapter;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> productList;
    public ArrayList<Nutrient> nuts = new ArrayList<Nutrient>();
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
		String url = "http://vitaminme.notimplementedexception.me/nutrients?count=100000";
		ApiCallParams params = new ApiCallParams();
		params.url = url;
		ParseNutrients parseNutrients = new ParseNutrients();
		parseNutrients.setCallback(this);
		params.callBackObject = parseNutrients;
		Context context = MainActivity.this;
		ApiCallTask task = new ApiCallTask(context);
		task.execute(params);
        
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        ImageButton x = (ImageButton)findViewById(R.id.x_button);
        x.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v){
        		inputSearch.setText("");
        	}
        });
        
        ImageButton check = (ImageButton) findViewById(R.id.check_button);
        check.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Toast.makeText(getBaseContext(),"dialog box..", Toast.LENGTH_SHORT).show();
        	}
        });
        
        adapter = new NutrientListAdapter(this, R.layout.nutrient_list_item_wbuttons, nuts);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
        
        inputSearch.addTextChangedListener(new TextWatcher() {
        	 
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                MainActivity.this.adapter.getFilter().filter(cs);
            }
         
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
         
            }
         
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
 
    }

	public void callback(ArrayList<Nutrient> nutrients) {
	    adapter = new NutrientListAdapter(this, R.layout.nutrient_list_item_wbuttons, nutrients);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
        
        inputSearch.addTextChangedListener(new TextWatcher() {
        	 
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                MainActivity.this.adapter.getFilter().filter(cs);
            }
         
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
         
            }
         
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
	}
 
}