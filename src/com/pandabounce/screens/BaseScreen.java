package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.*;

public abstract class BaseScreen implements Screen {
	Game game;
	SpriteBatch spriteBatch;
	
	int firstFramesToSkip = 5;
	
	protected StringBuilder fpsText;
	public BaseScreen screenToSwitchTo;
	
	public OrthographicCamera guiCam;
	
	public BaseScreen(Game game){
		this.game = game;
		spriteBatch = new SpriteBatch(300);
		fpsText = new StringBuilder();
		
		Gdx.graphics.getGLCommon().glClearColor(0, 0.7f, 0.8f, 1);
		
		// Setting up GUI camera so that bottom left corner is 0 0
		guiCam = new OrthographicCamera(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		guiCam.translate(Game.SCREEN_WIDTH/2, Game.SCREEN_HEIGHT/2);
		guiCam.update();
		
		spriteBatch.setProjectionMatrix(guiCam.combined);
	}
	
	public abstract void draw(float deltaTime);
	
	public abstract void update(float deltaTime);
	
	/**
	 * Get's called every time before switching to another screen.
	 * Perfect for making transitions between screens (Fade out effect, etc.)
	 * Screen switching is initialized when screenToSwitchTo field is not null.
	 * 
	 * @param deltaTime
	 * @return true if screen is ready to be switched
	 */
	public abstract boolean beforeScreenSwitch(float deltaTime);
	
	@Override
	public void render(float deltaTime) {
		if(firstFramesToSkip > 0){
			firstFramesToSkip--;
			return;
		}
		
		update(deltaTime);
		
		Gdx.graphics.getGLCommon().glClear(GL10.GL_COLOR_BUFFER_BIT);
		draw(deltaTime);
		
		timePassed += Gdx.graphics.getDeltaTime();
		fpsCount++;
		if(timePassed > 1f){
			timePassed -= 1f;
			fps = fpsCount;
			fpsCount = 0;
			
			// Updating FPS string
			fpsText.setLength(0);
			fpsText.append("FPS: ");
			fpsText.append(fps);
		}
		
		// Check whether the screen has to be switched.
		if(screenToSwitchTo != null){
			if(beforeScreenSwitch(Gdx.graphics.getDeltaTime())){
				game.setScreen(screenToSwitchTo);
				screenToSwitchTo = null;
			}
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}
	
	float timePassed = 0.0f;
	int fpsCount = 0;
	public int fps = 0;

}
