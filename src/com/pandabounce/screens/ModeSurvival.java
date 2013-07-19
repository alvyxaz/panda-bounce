package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.pandabounce.Game;
import com.pandabounce.entities.Bee;
import com.pandabounce.entities.Hedgehog;
import com.pandabounce.entities.Star;
import com.pandabounce.entities.SurpriseBox;
import com.pandabounce.resources.Art;

/*
 * New enemy every 20 sec
 */
public class ModeSurvival extends GameScreen {
	
	public ModeSurvival(Game game) {
		super(game);

		timer.enabled = true;
		timer.ascending = true;
		
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
		
		bees = new Bee[1];
		for(int i = 0; i < bees.length; i++){
			bees[i] = new Bee(world, panda.getPosition());
			bees[i].regenerate();
		}
		
		box = new SurpriseBox(Game.random.nextInt(Game.SCREEN_WIDTH-50), Game.random.nextInt(Game.SCREEN_HEIGHT-50), world);
	}

	@Override
	public void restartGame() {
		super.restartGame();
	}
	
	@Override
	public void updateLevel(float deltaTime) {
		box.update(deltaTime);
	}
	
	@Override
	public void drawLevel(float deltaTime) {
		drawBackground(spriteBatch);
		
		if (box.regenerate) {
			box.regenerate(Game.random.nextInt((int) (Game.SCREEN_WIDTH - box.hitBox.width)), Game.random.nextInt((int) (Game.SCREEN_HEIGHT - box.hitBox.height)));
		}

		dust.draw(spriteBatch, deltaTime);
		
		box.draw(spriteBatch);
		
		// Drawing Hedgehogs
		for(int i = 0; i < hedgehogs.length; i++)
			hedgehogs[i].draw(spriteBatch);
				
		// Drawing bees
		for(int i = 0; i < bees.length; i++)
			bees[i].draw(spriteBatch);
		
		panda.draw(spriteBatch, deltaTime);
		
		for(int i = 0; i < stars.length; i++){
			if (stars[i].pickedUp) {
				stars[i].onPickedUp();
			}
			stars[i].draw(spriteBatch, deltaTime);
		}
		
		score.draw(spriteBatch, deltaTime);
		
		healthBar.draw(spriteBatch, deltaTime);
		
		// Updating and drawing notifications
		for(int i = 0; i < notifications.length; i++){
			if(notifications[i].display){
				notifications[i].draw(spriteBatch, deltaTime);
			}
		}
		
		largeNotifications.draw(spriteBatch, deltaTime);
		Art.fontDefault.draw(spriteBatch, this.fpsText, 0, 50);
		timer.draw(spriteBatch);
	}

	@Override
	public void prepare() {
		Gdx.graphics.getGLCommon().glClearColor(0.39f, 0.56f, 0.11f, 1);
	}
}