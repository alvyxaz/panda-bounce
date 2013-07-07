package com.pandabounce.screens;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;

public class MyTextInputListener implements TextInputListener {
		
		private int score;
	
		public MyTextInputListener(int score) {
			this.score = score;
		}
		
	   @Override
	   public void input (String text) {
		    // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://localhost/panda-scores/");

		    try {
		        // Add your data
		        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("text", text));
		        nameValuePairs.add(new BasicNameValuePair("score", Integer.toString(score)));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }

	   }

	   @Override
	   public void canceled () {
	   }
	}
