package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.resources.Art;

public class TitleScreen extends BaseScreen {

	private Rectangle logo;
	private Rectangle buttonPlay;
	private Rectangle buttonLeaderboard;
	private Rectangle buttonGuide;
	private Rectangle buttonQuit;
	private Rectangle buttonSignIn;
	private Rectangle buttonSound;
	
	private int state = 0;
	private static final int STATE_OPENING = 0;
	private static final int STATE_LIVE = 1;
	
	public TitleScreen(Game game) {
		super(game);

		int height = - Art.logo.getRegionHeight();
		
		logo = new Rectangle(Game.SCREEN_HALF_WIDTH - Art.logo.getRegionWidth()/2, height,
				Art.logo.getRegionWidth(), Art.logo.getRegionHeight());
		
		height -= Art.guiButtonSmall.getRegionHeight() + 30;
		
		buttonPlay = new Rectangle(Game.SCREEN_HALF_WIDTH - Art.guiButtonSmall.getRegionWidth() - 15, height,
				Art.guiButtonSmall.getRegionWidth(), Art.guiButtonSmall.getRegionHeight());
		
		buttonGuide = new Rectangle(buttonPlay.x + buttonPlay.width + 30, height,
				Art.guiButtonSmall.getRegionWidth(), Art.guiButtonSmall.getRegionHeight());
		
		height -= Art.guiButtonSmall.getRegionHeight() + 30;
		
		buttonLeaderboard = new Rectangle(Game.SCREEN_HALF_WIDTH - Art.guiButtonBig.getRegionWidth()/2, height,
				Art.guiButtonBig.getRegionWidth(), Art.guiButtonBig.getRegionHeight());
		
		height -= Art.guiButtonSmall.getRegionHeight() + 30;
		
		buttonQuit = new Rectangle(Game.SCREEN_HALF_WIDTH - Art.guiButtonSmall.getRegionWidth()/2, height,
				Art.guiButtonSmall.getRegionWidth(), Art.guiButtonSmall.getRegionHeight());
		
		height -= Art.guiButtonSmall.getRegionHeight() + 30;
		
		buttonSignIn = new Rectangle(Game.SCREEN_HALF_WIDTH - Art.guiSignIn.getRegionWidth()/2, height,
				Art.guiSignIn.getRegionWidth(), Art.guiSignIn.getRegionHeight());
		
		buttonSound = new Rectangle(Game.SCREEN_WIDTH - Art.guiSoundOn.getRegionWidth() - 20, 20,
				Art.guiSoundOn.getRegionWidth(), Art.guiSoundOn.getRegionHeight());
		
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();

		spriteBatch.disableBlending();
		spriteBatch.draw(Art.background, 0, 0);
		spriteBatch.enableBlending();
		
		spriteBatch.draw(Art.logo, logo.x, logo.y);
		
		spriteBatch.draw(Art.guiButtonSmall, buttonPlay.x, buttonPlay.y);
		Art.fontKomika24Gold.drawWrapped(spriteBatch, "Play", 
				buttonPlay.x, buttonPlay.y+ buttonPlay.height - 16, buttonPlay.width, HAlignment.CENTER);
		
		spriteBatch.draw(Art.guiButtonSmall, buttonGuide.x, buttonGuide.y);
		Art.fontKomika24Gold.drawWrapped(spriteBatch, "Guide", 
				buttonGuide.x, buttonGuide.y+ buttonGuide.height - 16, buttonGuide.width, HAlignment.CENTER);
		
		spriteBatch.draw(Art.guiButtonBig, buttonLeaderboard.x, buttonLeaderboard.y);
		Art.fontKomika24Gold.drawWrapped(spriteBatch, "Leaderboard", 
				buttonLeaderboard.x, buttonLeaderboard.y+ buttonLeaderboard.height - 16, buttonLeaderboard.width, HAlignment.CENTER);
		
		spriteBatch.draw(Art.guiButtonSmall, buttonQuit.x, buttonQuit.y);
		Art.fontKomika24Gold.drawWrapped(spriteBatch, "Quit", 
				buttonQuit.x, buttonQuit.y+ buttonQuit.height - 16, buttonQuit.width, HAlignment.CENTER);
		
		if(Game.google.isSignedIn()){
			spriteBatch.draw(Art.guiSignOut, buttonSignIn.x, buttonSignIn.y);
		} else {
			spriteBatch.draw(Art.guiSignIn, buttonSignIn.x, buttonSignIn.y);
		}
		
		if(Game.mute){
			spriteBatch.draw(Art.guiSoundOff, buttonSound.x, buttonSound.y);
		} else {
			spriteBatch.draw(Art.guiSoundOn, buttonSound.x, buttonSound.y);
		}
		
		spriteBatch.end();
	}

	@Override
	public void update(float deltaTime) {
		
		switch(state){
			case STATE_OPENING:
				if(logo.y < Game.SCREEN_HEIGHT- Art.logo.getRegionHeight() - 50){
					logo.y += 400 * deltaTime;
					buttonPlay.y += 400 * deltaTime;
					buttonGuide.y += 400 * deltaTime;
					buttonLeaderboard.y += 400 * deltaTime;
					buttonQuit.y += 400 * deltaTime;
					buttonSignIn.y += 400 * deltaTime;
				} else {
					state = STATE_LIVE;
				}
				break;
			case STATE_LIVE:
				if(Input.isTouching(buttonPlay)){
					this.screenToSwitchTo = new ModeSurvival(this.game);
				} else if(Input.isReleasing(buttonLeaderboard)){
					Game.google.getScores();
				} else if(Input.isReleasing(buttonQuit)){
					Gdx.app.exit();
				} else if(Input.isReleasing(buttonSound)){
					Game.mute = !Game.mute;
				}
				
				if(Input.isReleasing(buttonSignIn) && !Game.google.isSignedIn()){
					Game.google.Login();
				}
				
				if(Input.isReleasing(buttonSignIn) && Game.google.isSignedIn()){
					Game.google.LogOut();
				}
				break;
		}
		
	}

	@Override
	public boolean beforeScreenSwitch(float deltaTime) {

		if(buttonPlay.y < Game.SCREEN_HEIGHT){
			logo.y += 400 * deltaTime;
			buttonPlay.y += 400 * deltaTime;
			buttonGuide.y += 400 * deltaTime;
			buttonLeaderboard.y += 400 * deltaTime;
			buttonQuit.y += 400 * deltaTime;
			buttonSignIn.y += 400 * deltaTime;
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
