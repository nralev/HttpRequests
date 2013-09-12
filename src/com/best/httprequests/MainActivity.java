package com.best.httprequests;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	            startActivityForResult(intent, TAKE_PICTURE);
			}
		});

		AsyncGetAndParseTask task = new AsyncGetAndParseTask() {
			@Override
			protected void onPostExecute(Results results) {
				Result firstResult = results.getResults().get(0);
				AddressComponent firstAddressComponent = firstResult
						.getAddress_components().get(0);
				Log.e(TAG, String.format("short_name %s, formatted_address %s",
						firstAddressComponent.getShort_name(),
						firstResult.getFormatted_address()));
			}
		};
		task.execute("http://maps.googleapis.com/maps/api/geocode/json?address=Sofia&sensor=false");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public String getRequestURLConnection(String url) throws IOException {
		URL URL = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection) URL
				.openConnection();
		try {
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			return in.toString();
		} finally {
			urlConnection.disconnect();
		}
	}

	public String getRequest(String url) throws ClientProtocolException,
			IOException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(new HttpGet(url));
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			out.close();
			return out.toString();

		} else {
			// Closes the connection.
			response.getEntity().getContent().close();
			throw new IOException(statusLine.getReasonPhrase());
		}
	}
	
	
	private void parseXml(InputStream xmlStream) throws XmlPullParserException, IOException{
		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(xmlStream,null);
		
		Result result = null;
		List<AddressComponent> addressComponentList = new ArrayList<AddressComponent>();
		AddressComponent currentAddressComponent =null;
		int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                	result = new Result();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name == "address_component"){
                    	currentAddressComponent = new AddressComponent();
                    } else if (currentAddressComponent != null){
                        if (name == "long_name"){
                        	currentAddressComponent.setLong_name(parser.nextText());
                        } else if (name == "short_name"){
                        	currentAddressComponent.setShort_name(parser.nextText());
                        } 
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("address_component") && currentAddressComponent != null){
                    	addressComponentList.add(currentAddressComponent);
                    } 
            }
            eventType = parser.next();
        }
        result.setAddress_components(addressComponentList);
	}
	
	//Take picture
	
	final int TAKE_PICTURE = 115;
	public void capturePhoto() {
		  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    startActivityForResult(intent, TAKE_PICTURE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	   
	    if ( (requestCode == TAKE_PICTURE) && resultCode == Activity.RESULT_OK) {
	        try {
	        	 Bundle extras = data.getExtras();
	        	 Bitmap bitmap = (Bitmap) extras.get("data");
	            
//	            imageView.setImageBitmap(bitmap);
	        } catch (NullPointerException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        
	    }
	}

}
