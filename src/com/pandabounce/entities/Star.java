package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class Star {
	
	public Rectangle hitBox;
	public float originalY;			// Used to base levitation on
	
	public boolean taken = false; 	// Whether or not the star has been taken
	
	private float angle = 0;		// Rotation angle
	
	// Levitation related
	private int levitationDirection = 1;
	private float levitationCoef = 0;
	private float levitation = 0;
	private int maxLevitation = 10;
	 
	public Star(int x, int y){
		hitBox = new Rectangle(x, y, Art.star.getRegionWidth(), Art.star.getRegionHeight());
		angle = Game.random.nextInt(360);
		levitationCoef = Game.random.nextFloat() * 3.14f;
		originalY = y;
	}
	
	/*
	 * Mark the star as picked up
	 */
	public void pickUp(){
		taken = true;
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		if(taken) return;
		
		// Drawing
		spriteBatch.draw(Art.star, hitBox.x, hitBox.y, hitBox.width/2, hitBox.height/2, hitBox.width, hitBox.height, 1.0f, 1.0f, angle);
		
		// Rotating star
		angle += deltaTime * 40;
	
		// Levitating
		levitationCoef += deltaTime * 2;
		levitation = maxLevitation * (float) Math.sin(levitationCoef);
		hitBox.y = originalY + levitation;
	}
}
