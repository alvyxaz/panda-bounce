package com.pandabounce.screens;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.resources.Art;

public class RatingScreen extends BaseScreen {
	
	private String scoresUrl = "http://158.129.18.103/panda-scores";
	
	private String [][] scores;
	
	private boolean scoresLoaded = false;
	
	public RatingScreen(Game game, int score) {
		super(game);
		
		scores = new String[10][2];

		try {
		    HttpClient client = new DefaultHttpClient();  
		    HttpGet get = new HttpGet(scoresUrl);
		    BasicHttpResponse responseGet = (BasicHttpResponse) client.execute(get);  
		    HttpEntity resEntityGet = ((org.apache.http.HttpResponse) responseGet).getEntity();  
		    if (resEntityGet != null) {  
		        String response = EntityUtils.toString(resEntityGet);
		        JSONArray jArray = new JSONArray(response);
		        for (int i = 0; i < jArray.length(); i++) {
		        	JSONArray obj = jArray.getJSONArray(i);
		        	scores[i][0] = obj.getString(0);
		        	scores[i][1] = obj.getString(1);
		        }
		        scoresLoaded = true;
		    }
		    
		    if (scores[9][0] == null || score > Integer.parseInt(scores[9][1])) {
		    	MyTextInputListener listener = new MyTextInputListener(score);
		    	Gdx.input.getTextInput(listener, "New highscore", "Player");
		    }
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();

		if (scoresLoaded) {
	        for (int i = 0; i < scores.length; i++) {
	        	if (scores[i][0] != null && scores[i][1] != null) {
		    		Art.fontDefault.draw(spriteBatch, (i+1) + ". " + scores[i][0], 50, Game.SCREEN_HEIGHT - (i+1) * 55);
		    		Art.fontDefault.draw(spriteBatch, scores[i][1], Game.SCREEN_WIDTH - 110, Game.SCREEN_HEIGHT - (i+1) * 55);	
	        	}
	        }	
		} else {
    		// Draw spinner or something
		}
		
		spriteBatch.end();
	}

	@Override
	public void update(float deltaTime) {

		
	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean beforeScreenSwitch(float deltaTime) {
		// TODO Auto-generated method stub
		return true;
	}

}
