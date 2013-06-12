package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.entities.Panda;
import com.pandabounce.entities.Person;
import com.pandabounce.entities.Star;
import com.pandabounce.entities.Surface;
import com.pandabounce.resources.Art;

/*
 * TODO: Should work as a base for level objects (screens).
 * Everything that has to be repeated  in every level should be
 * in this object.
 */
public abstract class GameScreen extends BaseScreen {

	protected Panda panda;
	protected float targetAngle;
	
	protected Rectangle room;
	
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
	protected Star [] stars;	// Star entities
	protected int starsTaken = 0; 	// Number of stars taken
	
	protected Surface [] surfaces;
	
	protected Person [] people;
	
	
	
	public GameScreen(Game game) {
		super(game);
		panda = new Panda(200, 400);
		stars = new Star[3];
		people = new Person[3];
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
							panda.jump(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
						}
						panda.touched = true;
					} else {
						panda.touched = false;
					}
				}
				
				// Updating stars
				for(int i = 0; i < stars.length; i++){
					if(!stars[i].taken && stars[i].hitBox.overlaps(panda.hitBox)){
						stars[i].pickUp();
						starsTaken += 1;
					}
				}
				
				// Updating surfaces
				for(int i = 0; i < surfaces.length; i++){
					if(surfaces[i].hitBox.contains(panda.hitBox.x + panda.hitBoxCenterX, panda.hitBox.y)){
						System.out.println("PYZE");
					}
				}
				
				break;
		}
		
	}

	/*
	 * Indicates how many stars have been taken
	 */
	protected void drawStarsIndicator(){
		int width = 60;
		int x = 10;
		int y = Game.SCREEN_HEIGHT - 60;
		
		for(int i = 0; i < stars.length; i++){
			if( i < starsTaken){
				spriteBatch.draw(Art.star, x + width * i, y);
			} else {
				spriteBatch.draw(Art.starEmpty, x + width * i, y);
			}
		}
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
