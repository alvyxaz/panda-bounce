package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class Panda {
	public Rectangle hitBox;
	public float hitBoxCenterX;	// Relative Y center of the hitBox
	
	public float health = 100; // Percentage
	
	// Basic states
	public int state = 0;
	private int STATE_WAITING = 0;
	private int STATE_SLIDING = 1;
	
	// Jump related
	private float xVelocity = 0;
	private float yVelocity = 0;
	private float speedCoef = 1;
	
	public boolean touched = false;
	
	public Panda(int x, int y) {
		hitBox = new Rectangle(x, y, Art.panda[0].getRegionWidth(), Art.panda[0].getRegionHeight());
		hitBoxCenterX = hitBox.width/2;
	}
	
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.draw(Art.panda[0], hitBox.x, hitBox.y);
		
		// Debugging hitbox
//		spriteBatch.setColor(0, 0, 1, 0.5f);
//		spriteBatch.draw(Art.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
//		spriteBatch.setColor(Color.WHITE);
	}
	
	public void update(float deltaTime){
		
		if(state == STATE_SLIDING){
			
			hitBox.x += xVelocity * speedCoef * deltaTime;
			hitBox.y += yVelocity * speedCoef * deltaTime;
			
			speedCoef -= deltaTime;
			
			if(speedCoef < 0) {
				state = STATE_WAITING;
			}
			
			// colission with room
			if (hitBox.x < 0) {
				hitBox.x = 0;
				xVelocity *= -1;
			}
			
			if (hitBox.x + hitBox.width > Game.SCREEN_WIDTH) {
				hitBox.x = Game.SCREEN_WIDTH - hitBox.width;
				xVelocity *= -1;
			}
			
			if (hitBox.y < 0) {
				hitBox.y = 0;
				yVelocity *= -1;
			}
			
			if (hitBox.y + hitBox.height > Game.SCREEN_HEIGHT) {
				hitBox.y = Game.SCREEN_HEIGHT - hitBox.height;
				yVelocity *= -1;
			}
			
		}
	}
	
	public void slide(float dX, float dY){
		state = STATE_SLIDING; dY *= -1;
		xVelocity = xVelocity * speedCoef + dX*7;
		yVelocity = yVelocity * speedCoef + dY*7;
		speedCoef = 1;
	}
	
}
