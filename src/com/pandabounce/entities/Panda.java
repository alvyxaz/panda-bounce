package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.resources.Art;

public class Panda {
	public Rectangle hitBox;
	public float hitBoxCenterX;	// Relative Y center of the hitBox
	
	public Rectangle targetArrow;
	private int targetOffset = 20;
	
	// Basic states
	public int state = 0;
	private int STATE_WAITING = 0;
	private int STATE_JUMPING = 1;
	
	// Jump related
	private float targetAngle = 0;	// Degrees
	private int jumpSpeed = 200; // px per second
	private float jumpTime = 1;
	private float jumpAngle;
	
	public Panda(int x, int y) {
		hitBox = new Rectangle(x, y, Art.panda[0].getRegionWidth(), Art.panda[0].getRegionHeight());
		hitBoxCenterX = hitBox.width/2;
		targetArrow = new Rectangle(hitBox.x, hitBox.y, Art.targetArrow.getRegionWidth(), Art.targetArrow.getRegionHeight());
	}
	
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.draw(Art.panda[0], hitBox.x, hitBox.y);
	
		spriteBatch.draw(Art.targetArrow, hitBox.x + hitBoxCenterX + targetOffset, hitBox.y, -targetOffset , targetArrow.width/2, targetArrow.height,targetArrow.width , 1.0f, 1.0f, targetAngle, true);
		// spriteBatch.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation, clockwise);
		
		// Debugging. Display panda hitbox center
		spriteBatch.draw(Art.px, hitBox.x + hitBoxCenterX, hitBox.y, 4, 4);
	
	}
	
	public void update(float deltaTime){
		
		// Performing jump if it's in progress
		// TODO: Make it jump, instead of just gliding. Additional y value should do the trick
		if(state == STATE_JUMPING){
			hitBox.x += Math.cos(jumpAngle) * jumpSpeed * deltaTime;
			hitBox.y += Math.sin(jumpAngle) * jumpSpeed * deltaTime;
			jumpTime -= deltaTime;
			if(jumpTime <= 0)
				state = STATE_WAITING;
		}
		
	}
	
	public void setTarget(float x, float y){
		targetAngle = (float) (Math.atan2(y - hitBox.y, x - hitBox.x - hitBoxCenterX) * 180 / Math.PI); // Degrees
	}
	
	public void jump(){
		if(state != STATE_JUMPING){
			this.state = STATE_JUMPING;
			jumpAngle = (float) Math.toRadians(targetAngle);
			jumpTime = 1;
		}
		
	}
	
}
