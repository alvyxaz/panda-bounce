package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
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
		
		// Making hedgehogs
		hedgehogs = new Hedgehog[3];
		for(int i = 0; i < hedgehogs.length; i++ ){
			hedgehogs[i] = new Hedgehog(world);
			hedgehogs[i].regenerate();
		}
	}

	@Override
	public void drawLevel(float deltaTime) {
		drawBackground(spriteBatch);
		
		// Drawing stars 
		for(int i = 0; i < stars.length; i++) {
			if (stars[i].regenerate) {
				stars[i].regenerate(Game.random.nextInt((int) (Game.SCREEN_WIDTH - stars[i].hitBox.width)), Game.random.nextInt((int) (Game.SCREEN_HEIGHT - stars[i].hitBox.height)));
			}
		}
		
		for(int i = 0; i < stars.length; i++)
			stars[i].draw(spriteBatch, deltaTime);
		
		dust.draw(spriteBatch, deltaTime);
		
		// Drawing people
		for(int i = 0; i < hedgehogs.length; i++)
			hedgehogs[i].draw(spriteBatch, deltaTime);
				
		panda.draw(spriteBatch, deltaTime);
		
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

	@Override
	public void prepare() {
		Gdx.graphics.getGLCommon().glClearColor(0.39f, 0.56f, 0.11f, 1);
	}
}
