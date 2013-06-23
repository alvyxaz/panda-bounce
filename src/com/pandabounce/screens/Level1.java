package com.pandabounce.screens;

import com.pandabounce.Game;
import com.pandabounce.entities.Hedgehog;
import com.pandabounce.entities.Star;
import com.pandabounce.resources.Art;

public class Level1 extends GameScreen {
	
	public Level1(Game game) {
		super(game);

		// Planting stars
		stars[0] = new Star(Game.random.nextInt(Game.SCREEN_WIDTH-50), Game.random.nextInt(Game.SCREEN_HEIGHT-50), world);
		stars[1] = new Star(Game.random.nextInt(Game.SCREEN_WIDTH-50), Game.random.nextInt(Game.SCREEN_HEIGHT-50), world);
		stars[2] = new Star(Game.random.nextInt(Game.SCREEN_WIDTH-50), Game.random.nextInt(Game.SCREEN_HEIGHT-50), world);
		
		// Making people
		people[0] = new Hedgehog(150, 250, world);
		people[1] = new Hedgehog(250, 500, world);
		people[2] = new Hedgehog(350, 250, world);
		
		System.out.println();
	}

	@Override
	public void drawLevel(float deltaTime) {
		drawBackground(spriteBatch);
		
		// Drawing stars 
		for(int i = 0; i < stars.length; i++)
			stars[i].draw(spriteBatch, deltaTime);
		
		// Drawing people
		for(int i = 0; i < people.length; i++)
			people[i].draw(spriteBatch, deltaTime);
				
		panda.draw(spriteBatch);
		
		Art.fontDefault.draw(spriteBatch, this.fpsText, 0, 20);
		score.draw(spriteBatch, deltaTime);
		
		healthBar.draw(spriteBatch, deltaTime);
		
		// Updating and drawing notifications
		for(int i = 0; i < notifications.length; i++){
			if(notifications[i].display){
				notifications[i].draw(spriteBatch, deltaTime);
			}
		}
	}
	
	@Override
	public void updateLevel(float deltaTime) {
		panda.update(deltaTime);
	}
}
