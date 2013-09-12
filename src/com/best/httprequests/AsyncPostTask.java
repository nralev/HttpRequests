package com.best.httprequests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

class AsyncPostTask extends AsyncTask<String, Void, String> {
	private static final String TAG = "AsyncGetTask";
	private Exception exception;

	protected String doInBackground(String... urls) {
		try {
			URL URL = new URL( urls[0]);
			HttpURLConnection urlConnection = (HttpURLConnection) URL
					.openConnection();
			urlConnection.setDoOutput(true);
		    urlConnection.setChunkedStreamingMode(0);
		    urlConnection.setRequestMethod("POST");
		    
		    //Send request payload
		      DataOutputStream wr = new DataOutputStream (
		    		  urlConnection.getOutputStream ());
		      wr.writeBytes (urls[1]);
		      wr.flush ();
		      wr.close ();
		   
		    	
			try {
				return convertStreamToString(urlConnection.getInputStream());
			} finally {
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}

	protected void onPostExecute(String result) {
		Log.e(TAG, result);
	}
		
	private String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	        sb.append(line);
	    }
	    is.close();
	    return sb.toString();
	}
}