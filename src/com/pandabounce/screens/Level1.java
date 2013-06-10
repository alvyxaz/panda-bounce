package com.pandabounce.screens;

import com.pandabounce.Game;
import com.pandabounce.entities.Island;
import com.pandabounce.entities.Star;
import com.pandabounce.entities.Surface;

public class Level1 extends GameScreen {
	
	public Level1(Game game) {
		super(game);
		panda.hitBox.y = 20;
		panda.hitBox.x = Game.SCREEN_HALF_WIDTH - panda.hitBoxCenterX;
		
		// Planting stars
		stars[0] = new Star(80, 500);
		stars[1] = new Star(160, 500);
		stars[2] = new Star(240, 500);
		
		// Making land
		surfaces = new Surface[1];
		surfaces[0] = new Island(300, 700);
	}

	@Override
	public void drawLevel(float deltaTime) {
		
		// Drawing surfaces
		for(int i = 0; i < surfaces.length; i++)
			surfaces[i].draw(spriteBatch, deltaTime);
			
		// Drawing stars 
		for(int i = 0; i < stars.length; i++)
			stars[i].draw(spriteBatch, deltaTime);
				
		panda.draw(spriteBatch);
		
		
		
	
	}
	
	@Override
	public void updateLevel(float deltaTime) {
		panda.update(deltaTime);
	}
}
