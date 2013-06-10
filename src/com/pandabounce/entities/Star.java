package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.resources.Art;

public class Star {
	
	public Rectangle hitBox;
	
	private boolean display = true; 
	
	public Star(int x, int y){
		hitBox = new Rectangle(x, y, 50, 50);
	}
	
	public void pickUp(){
		display = false;
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		if(!display) return;
		
		spriteBatch.setColor(Color.YELLOW);
		spriteBatch.draw(Art.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		spriteBatch.setColor(Color.WHITE);
	}
}
