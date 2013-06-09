package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.entities.Panda;
import com.pandabounce.resources.Art;

public class GameScreen extends BaseScreen {

	private Panda panda;
	
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
			panda.jumpTo((float) (Math.atan2(Input.getY() - panda.hitBox.y, Input.getX() - panda.hitBox.x ) * 180 / Math.PI));
		}
	}

	@Override
	public boolean beforeScreenSwitch(float deltaTime) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
