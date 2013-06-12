package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class Person {
	
	public Rectangle hitBox;
	
	// movement related vars
	private int moveSpeed = 30;
	private int restlessness = 3;
	private float moveAngle;
	
	public Person(int x, int y){
		hitBox = new Rectangle(x, y, 50, 100);
		moveAngle = (float) (Math.random() * Math.PI);
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		// Drawing
		spriteBatch.draw(Art.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);

		float dX = (float) (Math.cos(moveAngle) * moveSpeed * deltaTime);
		float dY = (float) (Math.sin(moveAngle) * moveSpeed * deltaTime);
		hitBox.x += dX + (dX > 0 ? Math.random() * restlessness : -Math.random() * restlessness);
		hitBox.y += dY + (dY > 0 ? Math.random() * restlessness : -Math.random() * restlessness);;
		
		// colission with room
		if (hitBox.x < 0) {
			hitBox.x = 0;
			moveAngle = (float) Math.PI - moveAngle;
		}
		
		if (hitBox.x + hitBox.width > Game.SCREEN_WIDTH) {
			hitBox.x = Game.SCREEN_WIDTH - hitBox.width;
			moveAngle = (float) Math.PI - moveAngle;
		}
		
		if (hitBox.y < 0) {
			hitBox.y = 0;
			moveAngle = (float)  Math.abs(Math.PI - moveAngle);
		}
		
		if (hitBox.y + hitBox.height > Game.SCREEN_HEIGHT) {
			hitBox.y = Game.SCREEN_HEIGHT - hitBox.height;
			moveAngle = (float) (2*Math.PI - moveAngle);
		}
		
	}
}
