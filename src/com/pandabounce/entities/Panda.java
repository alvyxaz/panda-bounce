package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.resources.Art;

public class Panda {
	public Rectangle hitBox;
	
	// Basic states
	public int state = 0;
	private int STATE_WAITING = 0;
	private int STATE_JUMPING = 1;
	
	// Jump related
	private int jumpSpeed = 200; // px per second
	private float jumpTime = 1;
	private float jumpAngle;
	
	public Panda(int x, int y) {
		hitBox = new Rectangle(x, y, Art.panda[0].getRegionWidth(), Art.panda[0].getRegionHeight());
	}
	
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.draw(Art.panda[0], hitBox.x, hitBox.y);
	}
	
	public void update(float deltaTime){
		if(state == STATE_JUMPING){
			hitBox.x += Math.cos(jumpAngle) * jumpSpeed * deltaTime;
			hitBox.y += Math.sin(jumpAngle) * jumpSpeed * deltaTime;
			jumpTime -= deltaTime;
			if(jumpTime <= 0)
				state = STATE_WAITING;
		}
	}
	
	public void jumpTo(float angle){
		if(state != STATE_JUMPING){
			this.state = STATE_JUMPING;
			jumpAngle = (float) Math.toRadians(angle);
			jumpTime = 1;
		}
		
	}
	
}
