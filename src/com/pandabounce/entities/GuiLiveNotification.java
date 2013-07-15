package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.resources.Art;

public class GuiLiveNotification {
	
	public float x;
	public float y;
	
	public boolean display = false;
	
	public String notification = "";
	
	public float timeToLive = 0; // In seconds
	
	public void draw(SpriteBatch spriteBatch, float deltaTime) {
		Art.fontKomika24Gold.draw(spriteBatch, notification, x, y);
		
		timeToLive -= deltaTime;
		
		x += deltaTime * 30;
		y += deltaTime * 30;
		
		if(timeToLive < 0){
			display = false;
		}
		
	}
	
	public void generate(String text, int x, int y){
		this.notification = text;
		this.x = x;
		this.y = y;
		display = true;
		timeToLive = 2f;
	}
	
}
