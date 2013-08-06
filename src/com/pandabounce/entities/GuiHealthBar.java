package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.MyGame;
import com.pandabounce.resources.Art;

public class GuiHealthBar {

	private Panda panda;
	
	private int maxWidth = 125;
	private int width;
	private int height;
	
	private int x;
	private int y; 
	
	public GuiHealthBar(Panda panda) {
		this.panda = panda;
		this.width = maxWidth;
		this.x = MyGame.SCREEN_WIDTH - 10 - Art.guiHealthBar.getRegionWidth();
		this.y = MyGame.SCREEN_HEIGHT - Art.guiHealthBar.getRegionHeight() - 10;
		this.height = Art.guiHealth.getRegionHeight();
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime) {
		// Updating width
		this.width = (int)(panda.health/100 * maxWidth);
		
		// Drawing
		spriteBatch.draw(Art.guiHealthBar, x, y);
		
		if(width > 0)
			spriteBatch.draw(Art.healthBar, x+59, y+13, width, height);
	}
	
}
