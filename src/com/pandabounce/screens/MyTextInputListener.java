package com.pandabounce.screens;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
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
			DefaultHttpClient httpclient = new DefaultHttpClient();
   
		    String url = RatingScreen.SCORES_URL + "?text=" + text + "&score=" + Integer.toString(score);
		    HttpPost httppost = new HttpPost(url + "&hash=" + Encryption.computeHash(url));
		    System.out.println(url + "&hash=" + Encryption.computeHash(url));
		    try {
		        // Add your data
		        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("text", text));
		        nameValuePairs.add(new BasicNameValuePair("score", Integer.toString(score)));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
	   }

	   @Override
	   public void canceled () {
	   }
	   
	   private static class Encryption {
		   public static final String SALT = "iqwjoidj2o1ij3o12";
			
			public static String computeHash(String url) {
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("SHA-1");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			    byte[] buf = null;
				try {
					buf = md.digest((url + SALT).getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        StringBuffer hexString = new StringBuffer();
		        for (int i = 0; i < buf.length; i++)
		        	hexString.append(String.format("%02X", 0xFF & buf[i]));
		        return hexString.toString();
			}
	   }
	}
