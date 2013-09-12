package com.best.httprequests;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.os.AsyncTask;
import android.util.Log;


class AsyncGetAndParseTask extends AsyncTask<String, Void, Results> {
	private static final String TAG = "AsyncGetTask";
	private Exception exception;

	protected Results doInBackground(String... urls) {
		try {
			URL URL = new URL(urls[0]);
			HttpURLConnection urlConnection = (HttpURLConnection) URL
					.openConnection();
			
			try {
				return parseJson(urlConnection.getInputStream());
			} finally {
				urlConnection.disconnect();
			}
		} catch (Exception e) {
			Log.e(TAG,e.getMessage());
			this.exception = e;
			return null;
		}
	}

	protected void onPostExecute(Results results) {
		
	}
	
	private Results parseJson(InputStream jsonStream) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonStream, Results.class);
	}
	
}