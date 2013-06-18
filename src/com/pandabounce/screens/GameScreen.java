package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.entities.LiveNotification;
import com.pandabounce.entities.Panda;
import com.pandabounce.entities.Person;
import com.pandabounce.entities.Bamboo;
import com.pandabounce.entities.Score;
import com.pandabounce.resources.Art;

/*
 * TODO: Should work as a base for level objects (screens).
 * Everything that has to be repeated  in every level should be
 * in this object.
 */
public abstract class GameScreen extends BaseScreen {

	protected Panda panda;
	protected float targetAngle;
	
	/*-------------------------------------
	 * Transitions
	 */
	private int transitionState = 0;
	private static final int FADE_IN = 0;
	private static final int LIVE = 1;
	private static final int FADE_OUT = 2;
	private float opacity = 1f;
	
	/*-------------------------------------
	 * Data
	 */
	protected Bamboo [] stars;	// Star entities
	protected Person [] people;
	protected Score score;
	protected LiveNotification [] notifications;
	
	public GameScreen(Game game) {
		super(game);
		panda = new Panda(200, 400);
		stars = new Bamboo[3];
		people = new Person[3];
		score = new Score();
		notifications = new LiveNotification [10];
		for(int i = 0; i < notifications.length; i++){
			notifications[i] = new LiveNotification();
		}
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		drawLevel(deltaTime);
		switch(transitionState){
			case FADE_IN:
				spriteBatch.setColor(0, 0, 0, opacity);
				spriteBatch.draw(Art.px, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
				spriteBatch.setColor(Color.WHITE);
				break;
			case LIVE:
				break;
			case FADE_OUT:
				spriteBatch.setColor(0, 0, 0, opacity);
				spriteBatch.draw(Art.px, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
				spriteBatch.setColor(Color.WHITE);
				break;
		}
		spriteBatch.end();
	}
	
	public abstract void drawLevel(float deltaTime);
	public abstract void updateLevel(float deltaTime);

	@Override
	public void update(float deltaTime) {
		switch(transitionState){
			case FADE_IN:
				opacity -= deltaTime * 2;
				if(opacity < 0){
					opacity = 0;
					transitionState = LIVE;
				}
				break;
			case LIVE:
				updateLevel(deltaTime);
				
				// INPUT
				if(Gdx.input.isTouched()){
					if(Input.isTouching(panda.hitBox)){
						if(panda.touched == false){
							panda.slide(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
						}
						panda.touched = true;
					} else {
						panda.touched = false;
					}
				}
				
				// Updating stars
				for(int i = 0; i < stars.length; i++){
					if(stars[i].hitBox.overlaps(panda.hitBox)){
						
						// Pushing a notification to the buffer, where there's open space
						for(int z = 0; z < notifications.length; z++){
							if(!notifications[z].display){
								notifications[z].generate("+10", 
										(int) (panda.hitBox.x + panda.hitBoxCenterX) , 
										(int) (panda.hitBox.y + panda.hitBox.height/2));
								break;
							}
						}
						int newX = Game.random.nextInt(Game.SCREEN_WIDTH-(int)stars[i].hitBox.width);
						int newY = Game.random.nextInt(Game.SCREEN_HEIGHT-(int)stars[i].hitBox.height);
						stars[i].regenerate(newX, newY);
						score.add(10);
					}
				}
				
				break;
		}
	}

	public void drawBackground(SpriteBatch spriteBatch){
		spriteBatch.draw(Art.background, 0, 0);
	}
	
	@Override
	public boolean beforeScreenSwitch(float deltaTime) {
		opacity += deltaTime * 2;
		if(opacity > 1){
			return true;
		}
		return false;
	}
	
	
	
}
