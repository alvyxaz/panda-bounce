package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.resources.Art;

public class GuiEndWindow {

	private float x;
	private float y;
	private float width;
	private float height;
	
	public Rectangle playAgain;
	public Rectangle submitScore;
	
	private int state;
	public static final int FLYING_IN = 0;
	public static final int INPUT = 1;
	
	private float inactiveTimer;
	private int score;
	private String scoreText = "";
	private String bestText = "";
	private String comment = "";
	
	private int [] commentScore;
	private String [] commentText;
	
	public GuiEndWindow() {
		width = Art.guiEndWindow.getRegionWidth();
		height = Art.guiEndWindow.getRegionHeight();
		x = Game.SCREEN_HALF_WIDTH - width/2;
		y = Game.SCREEN_HALF_HEIGHT - height/2;
		
		submitScore = new Rectangle(x + 20, y - 27, Art.guiSubmitScoreButton.getRegionWidth(), Art.guiSubmitScoreButton.getRegionHeight());
		playAgain = new Rectangle(x+width-160, y - 27, Art.guiPlayAgainButton.getRegionWidth(), Art.guiPlayAgainButton.getRegionHeight());
	
		initializeComments();
	}
	
	public void initializeComments() {
		int count = 3;
		commentScore = new int [count];
		commentText = new String[count];
		
		commentScore[0] = 0;
		commentText[0] = ":O";
		
		commentScore[1] = 30;
		commentText[1] = "Meh...";
		
		commentScore[2] = 100;
		commentText[2] = "Superb";
	}
	
	public void show(int score){
		
		state = FLYING_IN;
		this.score = score;
		this.scoreText = ""+ score;
		this.x = Game.SCREEN_WIDTH;
		
		for(int i = 0; i < commentScore.length; i++){
			if(commentScore[i] > score){
				comment = commentText[i-1];
				break;
			} 
			if( i+1 == commentScore.length) {
				comment = commentText[i];
			}
		}

		// Saving highscore
		int oldBest = Game.preferences.getInteger("best");
		if(score > oldBest){
			Game.preferences.putInteger("best", score);
			Game.preferences.flush();
			bestText = ""+score;
		} else {
			bestText = ""+oldBest;
		}
		
		/*
		 * ACHIEVEMENT: Panda's first steps
		 */
		Achievements.unlockAchievement(Achievements.pandasFirstSteps);
		
		// Increment games played
		int gamesPlayed = Game.preferences.getInteger("games played", 0) + 1;
		Game.preferences.putInteger("games played", gamesPlayed);
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		spriteBatch.draw(Art.guiEndWindow, x, y);
		spriteBatch.draw(Art.guiSubmitScoreButton, x + 20, submitScore.y);
		spriteBatch.draw(Art.guiPlayAgainButton, x+width-160, playAgain.y);
	
		Art.fontKomika24Gold.draw(spriteBatch, scoreText,x+ 167, y+ 200);
		Art.fontKomika24Gold.draw(spriteBatch, bestText,x+ 167, y+ 168);
		
		Art.fontKomika24.setScale(0.6f);
		Art.fontKomika24.drawWrapped(spriteBatch, comment, x+37, y+120, 300, HAlignment.CENTER);
		Art.fontKomika24.setScale(1);
		
		switch(state){
			case FLYING_IN:
				if(x > Game.SCREEN_HALF_WIDTH-width/2){
					x -= deltaTime * 1000;
				} else {
					x = Game.SCREEN_HALF_WIDTH-width/2;
					state = INPUT;
				}
				break;
			case INPUT:
				break;
		}
	}
	
	public boolean isWaitingForInput(){
		return state == INPUT;
	}
	
}
