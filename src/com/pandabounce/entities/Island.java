package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.resources.Art;

public class Island extends Surface{
	
	public Island(int x, int y){
		hitBox = new Rectangle(x, y, 150, 150);
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){	
		spriteBatch.setColor(Color.DARK_GRAY);
		spriteBatch.draw(Art.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
		spriteBatch.setColor(Color.WHITE);
	}
	
}
