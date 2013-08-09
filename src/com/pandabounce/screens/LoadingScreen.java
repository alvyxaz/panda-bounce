package com.pandabounce.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.pandabounce.MyGame;
import com.pandabounce.resources.Art;

public class LoadingScreen extends BaseScreen{

	public LoadingScreen(MyGame game) {
		super(game);

		Art.loadFonts();
		
		spriteBatch.begin();
		
		spriteBatch.setColor(Color.BLACK);
		spriteBatch.draw(Art.px, 0, 0, MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
		spriteBatch.setColor(Color.WHITE);

		Art.fontKomika24Gold.drawWrapped(spriteBatch, 
				"Loading...", 0, MyGame.SCREEN_HALF_HEIGHT,MyGame.SCREEN_WIDTH, HAlignment.CENTER);
		spriteBatch.end();
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		
		spriteBatch.setColor(Color.BLACK);
		spriteBatch.draw(Art.px, 0, 0, MyGame.SCREEN_WIDTH, MyGame.SCREEN_HEIGHT);
		spriteBatch.setColor(Color.WHITE);

		Art.fontKomika24Gold.drawWrapped(spriteBatch, 
				"Loading...", 0, MyGame.SCREEN_HALF_HEIGHT,MyGame.SCREEN_WIDTH, HAlignment.CENTER);
		spriteBatch.end();
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean beforeScreenSwitch(float deltaTime) {
		return true;
	}

}
