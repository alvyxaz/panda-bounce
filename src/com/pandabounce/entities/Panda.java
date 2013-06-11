package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pandabounce.resources.Art;

public class Panda {
	public Rectangle hitBox;
	public float hitBoxCenterX;	// Relative Y center of the hitBox
	
	private Vector2 target;
	public Rectangle targetArrow;
	private int targetOffset = 20;
	
	// Basic states
	public int state = 0;
	private int STATE_WAITING = 0;
	private int STATE_JUMPING = 1;
	
	// Jump related
	private float targetAngle = 90;	// Degrees
	private int jumpSpeed = 200; // px per second
	private float jumpAngle;
	private int originalDistance = 0;
	private float speedCoef = 10;
	private int jumpDistance = 0;
	private int maxJumpDistance = 150;
	
	public boolean touched = false;
	
	public Panda(int x, int y) {
		hitBox = new Rectangle(x, y, Art.panda[0].getRegionWidth(), Art.panda[0].getRegionHeight());
		hitBoxCenterX = hitBox.width/2;
		targetArrow = new Rectangle(hitBox.x, hitBox.y, Art.targetArrow.getRegionWidth(), Art.targetArrow.getRegionHeight());
		target = new Vector2();
	}
	
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.draw(Art.panda[0], hitBox.x, hitBox.y);
		
		// Debugging hitbox
		spriteBatch.setColor(0, 0, 1, 0.5f);
		spriteBatch.draw(Art.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		spriteBatch.draw(Art.px, target.x, target.y, 10, 10);
		spriteBatch.setColor(Color.WHITE);
	}
	
	public void update(float deltaTime){
		
		// Performing jump if it's in progress
		// TODO: Make it jump, instead of just gliding. Additional y value should do the trick
		if(state == STATE_JUMPING){
			float dX = (float) (Math.cos(jumpAngle) * jumpSpeed * deltaTime * Math.sin(speedCoef));
			float dY = (float) (Math.sin(jumpAngle) * jumpSpeed * deltaTime * Math.sin(speedCoef));
			hitBox.x += dX;
			hitBox.y += dY;
			jumpDistance -= (float) Math.sqrt(dX * dX + dY * dY);
			if(jumpDistance/originalDistance < 0.5f){
				speedCoef -= deltaTime;
			}
			
			if(speedCoef < 0){
				state = STATE_WAITING;
			}
			
			
		}
	}
	
	public void jump(float dX, float dY){
		dY *= -1;
		if(state != STATE_JUMPING){
			this.state = STATE_JUMPING;
			jumpAngle = (float) Math.atan2(dY, dX);
			jumpDistance = (int) (3*Math.sqrt(dY * dY + dX * dX));
			
			jumpSpeed = jumpDistance*2;
			
			originalDistance = jumpDistance;
			speedCoef = (float)Math.PI/2;
		}
		
	}
	
}
