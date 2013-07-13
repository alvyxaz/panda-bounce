package com.pandabounce.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.resources.Art;

public class TitleScreen extends BaseScreen {

	private Rectangle logo;
	private Rectangle textPlay;
	
	private int state = 0;
	private static final int STATE_OPENING = 0;
	private static final int STATE_LIVE = 1;
	
	public TitleScreen(Game game) {
		super(game);

		int height = - Art.logo.getRegionHeight();
		
		logo = new Rectangle(Game.SCREEN_HALF_WIDTH - Art.logo.getRegionWidth()/2, height,
				Art.logo.getRegionWidth(), Art.logo.getRegionHeight());
		
		height -= Art.guiPlay.getRegionHeight() + 20;
		
		textPlay = new Rectangle(Game.SCREEN_HALF_WIDTH  - Art.guiPlay.getRegionWidth()/2, height,
				Art.guiPlay.getRegionWidth(), Art.guiExit.getRegionHeight());
		
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();

		spriteBatch.disableBlending();
		spriteBatch.draw(Art.background, 0, 0);
		spriteBatch.enableBlending();
		
		spriteBatch.draw(Art.logo, logo.x, logo.y);
		
		spriteBatch.draw(Art.guiPlay, textPlay.x, textPlay.y, textPlay.width, textPlay.height);
		
		spriteBatch.end();
	}

	@Override
	public void update(float deltaTime) {
		
		switch(state){
			case STATE_OPENING:
				if(logo.y < Game.SCREEN_HEIGHT- Art.logo.getRegionHeight() - 50){
					logo.y += 400 * deltaTime;
					textPlay.y += 400 * deltaTime;
				} else {
					state = STATE_LIVE;
				}
				break;
			case STATE_LIVE:
				
				if(Input.isTouching(textPlay)){
					this.screenToSwitchTo = new ModeFreestyle(this.game);
				}
				
				break;
		}
		
	}

	@Override
	public boolean beforeScreenSwitch(float deltaTime) {

		if(textPlay.y < Game.SCREEN_HEIGHT){
			logo.y += 400 * deltaTime;
			textPlay.y += 400 * deltaTime;
		} else {
			return true;
		}
		
		return false;
	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

}
