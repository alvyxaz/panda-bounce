package com.pandabounce.screens;

import com.pandabounce.Game;
import com.pandabounce.entities.Hedgehog;
import com.pandabounce.entities.Bamboo;
import com.pandabounce.resources.Art;

public class Level1 extends GameScreen {
	
	public Level1(Game game) {
		super(game);
		panda.hitBox.y = 20;
		panda.hitBox.x = Game.SCREEN_HALF_WIDTH - panda.hitBoxCenterX;
		
		// Planting stars
		stars[0] = new Bamboo(80, 500);
		stars[1] = new Bamboo(160, 500);
		stars[2] = new Bamboo(240, 500);
		
		// Making people
		people[0] = new Hedgehog(150, 250);
		people[1] = new Hedgehog(250, 500);
		people[2] = new Hedgehog(350, 250);
		
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
