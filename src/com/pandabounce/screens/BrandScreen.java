package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.MyGame;
import com.pandabounce.controls.Input;
import com.pandabounce.resources.Art;

public class BrandScreen extends BaseScreen {

	/*-------------------------------------
	 * Transitions
	 */
	private int transitionState = 0;
	private static final int FADE_IN = 0;
	private static final int LIVE = 1;
	private static final int FADE_OUT = 2;
	private float opacity = 1f;
	
	private Rectangle logo;
	
	private float autoFadeOutIn;
	
	public BrandScreen(MyGame game) {
		super(game);
		logo = new Rectangle(
				MyGame.SCREEN_HALF_WIDTH-Art.friendlyBlob.getWidth()/2,
				MyGame.SCREEN_HALF_HEIGHT- Art.friendlyBlob.getHeight()/2,
				Art.friendlyBlob.getWidth(),
				Art.friendlyBlob.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		autoFadeOutIn = 3f;
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		spriteBatch.draw(Art.friendlyBlob, logo.x, logo.y);
		
		spriteBatch.setColor(0, 0, 0, opacity);
		spriteBatch.draw(Art.px, 0, 0, MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
		spriteBatch.setColor(Color.WHITE);
		
		spriteBatch.end();
	}

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
				
				autoFadeOutIn -= deltaTime;
				if(autoFadeOutIn < 0){
					this.switchScreenTo(game.screenTitle);
					transitionState = FADE_OUT;
				} else {
					if(Gdx.input.isTouched()){
						this.switchScreenTo(game.screenTitle);
						transitionState = FADE_OUT;
					}
				}
				
				break;
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

	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

}
