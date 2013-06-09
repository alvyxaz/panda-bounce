package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.entities.Panda;
import com.pandabounce.resources.Art;

/*
 * TODO: Should work as a base for level objects (screens).
 * Everything that has to be repeated  in every level should be
 * in this object.
 */
public class GameScreen extends BaseScreen {

	protected Panda panda;
	protected float targetAngle;
	
	public GameScreen(Game game) {
		super(game);
		panda = new Panda(200, 400);
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		
		panda.draw(spriteBatch);
		
		spriteBatch.end();
	}

	@Override
	public void update(float deltaTime) {
		panda.update(deltaTime);
		
		if(Gdx.input.isTouched()){
			
			if(Input.isTouching(panda.hitBox)){
				panda.jump();
			} else {
				panda.setTarget(Input.getX(), Input.getY());
			}
			
		}
	}

	@Override
	public boolean beforeScreenSwitch(float deltaTime) {
		
		return false;
	}
	
	
	
}
