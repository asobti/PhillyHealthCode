package com.vitaminme.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ApiCallTask extends AsyncTask<ApiCallParams, Void, ApiResponse>{

	ProgressDialog dialog;
	private Context context;
	
	@Override
    protected void onPreExecute() {
		if(this.context != null) {
			this.dialog = ProgressDialog.show(this.context, "Loading", "Please wait...", true);
		}
	}
	
	public ApiCallTask(Context context) {
		this.context = context;
	}
	
	public ApiCallTask() {
		
	}
	
	ApiCallParams param;
	
	@Override
	protected ApiResponse doInBackground(ApiCallParams... params) {
		param =  params[0];
		String response = downloadUrl(param.url);
		JSONObject jsonObj = null;
		try {
			 jsonObj = new JSONObject(response);
		} catch(Exception e) {
			
		}
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.jsonObj = jsonObj;
		return apiResponse;
	}
	
	@Override
    protected void onPostExecute(ApiResponse apiResponse) {
		if(this.context != null && this.dialog != null && this.dialog.isShowing()) {
			this.dialog.dismiss();
		}
		param.callBackObject.callback(apiResponse);
	}
	
	protected String downloadUrl(String url) {
		String responseStr = "";
		HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = null;
	    
	    try {
			response = httpclient.execute(new HttpGet(url));
		} catch (ClientProtocolException e1) {
			
		} catch (IOException e1) {
			
		}
		
	    StatusLine statusLine = response.getStatusLine();
	    
	    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        try {
				response.getEntity().writeTo(out);
			} catch (IOException e1) {

			}
	        responseStr = out.toString();
	        try {
				out.close();
			} catch (IOException e) {

			}
	    } else{
	        //Closes the connection.
	        try {
				response.getEntity().getContent().close();
			} catch (IllegalStateException e) {
				
			} catch (IOException e) {
				
			}
	    }
	    return responseStr;
	}

}
