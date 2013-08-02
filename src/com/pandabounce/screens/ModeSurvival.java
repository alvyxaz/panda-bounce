package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.pandabounce.MyGame;
import com.pandabounce.entities.Bee;
import com.pandabounce.entities.Hedgehog;
import com.pandabounce.entities.Star;
import com.pandabounce.entities.SurpriseBox;

/*
 * New enemy every 20 sec
 */
public class ModeSurvival extends GameScreen {

	public ModeSurvival(MyGame game) {
		super(game);

		timer.enabled = true;
		timer.ascending = true;

		// Planting stars
		stars[0] = new Star(MyGame.random.nextInt(MyGame.SCREEN_WIDTH - 50),
				MyGame.random.nextInt(MyGame.SCREEN_HEIGHT - 50), world);
		stars[1] = new Star(MyGame.random.nextInt(MyGame.SCREEN_WIDTH - 50),
				MyGame.random.nextInt(MyGame.SCREEN_HEIGHT - 50), world);
		stars[2] = new Star(MyGame.random.nextInt(MyGame.SCREEN_WIDTH - 50),
				MyGame.random.nextInt(MyGame.SCREEN_HEIGHT - 50), world);

		// Making hedgehogs
		hedgehogs = new Hedgehog[3];

		bees = new Bee[1];

		box = new SurpriseBox(MyGame.random.nextInt(MyGame.SCREEN_WIDTH - 50),
				MyGame.random.nextInt(MyGame.SCREEN_HEIGHT - 50), world);
	}

	@Override
	public void restartGame() {
		super.restartGame();
	}

	@Override
	public void updateLevel(float deltaTime) {
		if (timer.time > 10) {
			if (hedgehogs[0] == null) {
				hedgehogs[0] = new Hedgehog(world);
				hedgehogs[0].regenerate();
			}
		}
		if (timer.time > 20) {
			if (hedgehogs[1] == null) {
				hedgehogs[1] = new Hedgehog(world);
				hedgehogs[1].regenerate();
			}
		}
		if (timer.time > 30) {
			if (hedgehogs[2] == null) {
				hedgehogs[2] = new Hedgehog(world);
				hedgehogs[2].regenerate();
			}
		}
		if (timer.time > 45) {
			if (bees[0] == null) {
				bees[0] = new Bee(world, panda.getPosition());
				bees[0].regenerate();
			}
		}

		box.update(deltaTime);
	}

	@Override
	public void drawLevel(float deltaTime) {
		drawBackground(spriteBatch);

		if (box.regenerate) {
			box.regenerate(
					MyGame.random
							.nextInt((int) (MyGame.SCREEN_WIDTH - box.hitBox.width)),
					MyGame.random
							.nextInt((int) (MyGame.SCREEN_HEIGHT - box.hitBox.height)));
		}

		dust.draw(spriteBatch, deltaTime);

		box.draw(spriteBatch);

		// Drawing Hedgehogs
		for (int i = 0; i < hedgehogs.length; i++) {
			if (hedgehogs[i] != null) {
				hedgehogs[i].draw(spriteBatch);
			}
		}

		// Drawing bees
		for (int i = 0; i < bees.length; i++) {
			if (bees[i] != null) {
				bees[i].draw(spriteBatch);
			}
		}

		panda.draw(spriteBatch, deltaTime);

		for (int i = 0; i < stars.length; i++) {
			if (stars[i].pickedUp) {
				stars[i].onPickedUp();
			}
			stars[i].draw(spriteBatch, deltaTime);
		}

		score.draw(spriteBatch, deltaTime);

		healthBar.draw(spriteBatch, deltaTime);

		// Updating and drawing notifications
		for (int i = 0; i < notifications.length; i++) {
			if (notifications[i].display) {
				notifications[i].draw(spriteBatch, deltaTime);
			}
		}

		largeNotifications.draw(spriteBatch, deltaTime);
		// Art.fontDefault.draw(spriteBatch, this.fpsText, 0, 50);

		effect.draw(spriteBatch);
		timer.draw(spriteBatch);
	}

	@Override
	public void prepare() {
		Gdx.graphics.getGLCommon().glClearColor(0.39f, 0.56f, 0.11f, 1);
	}
}