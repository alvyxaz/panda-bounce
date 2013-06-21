package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class Bamboo {
	
	public Rectangle hitBox;
	public float originalY;			// Used to base levitation on
	
	private float angle = 0;		// Rotation angle
	
	private float opacity = 1;
	
	// Levitation related
	private int levitationDirection = 1;
	private float levitationCoef = 0;
	private float levitation = 0;
	private int maxLevitation = 10;
	 
	public Bamboo(int x, int y){
		hitBox = new Rectangle(x, y, Art.star.getRegionWidth(), Art.star.getRegionHeight());
		angle = Game.random.nextInt(360);
		levitationCoef = Game.random.nextFloat() * 3.14f;
		originalY = y;
	}
	
	/*
	 * Regenerate bamboo in a different place
	 */
	public void regenerate(int x, int y) {
		hitBox.x = x;
		hitBox.y = y;
		originalY = y;
		opacity = 0;
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		// Drawing
		spriteBatch.setColor(1, 1, 1, opacity);
		spriteBatch.draw(Art.star, hitBox.x, hitBox.y, hitBox.width/2, hitBox.height/2, hitBox.width, hitBox.height, 1.0f, 1.0f, angle);
		spriteBatch.setColor(Color.WHITE);
		
		// Fade in effect
		if(opacity < 1){
			opacity += deltaTime;
			if(opacity > 1){
				opacity = 1;
			}
		}
		
		// Rotating star
		angle += deltaTime * 40;
	
		// Levitating
		levitationCoef += deltaTime * 2;
		levitation = maxLevitation * (float) Math.sin(levitationCoef);
		hitBox.y = originalY + levitation;
	}
}
