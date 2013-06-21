package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class GuiHealthBar {

	private Panda panda;
	
	private int maxWidth = 200;
	private int width;
	private int height;
	
	private int x;
	private int y; 
	
	public GuiHealthBar(Panda panda) {
		this.panda = panda;
		this.width = maxWidth;
		this.x = Game.SCREEN_HALF_WIDTH + 40;
		this.y = Game.SCREEN_HEIGHT- 36;
		this.height = Art.healthBar.getRegionHeight();
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime) {
		// Updating width
		this.width = (int)(panda.health/100 * maxWidth);
		
		// Drawing
		spriteBatch.setColor(Color.DARK_GRAY);
		spriteBatch.draw(Art.px, x-5,y-5, maxWidth+10, height + 10);
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.draw(Art.healthBar, x, y, width, height);
	}
	
}
