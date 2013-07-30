package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class GuiScore {

	public int score = 0;
	public String scoreText = String.format("%09d", score);
	public int multiplier = 1;
	public String multiplierText = "1";
	public int starsInSingleSlide = 0;
	private float multiplierTime;
	public int starsPickedUp = 0;
	
	private int x;
	private int y;
	
	private int multiplierX;
	private int multiplierY;
	
	private float scaleMultiplier = 1f;
	
	private GuiLargeNotifications largeNotifications;
	
	public GuiScore(GuiLargeNotifications notifications) {
		x = 10;
		y = Game.SCREEN_HEIGHT - Art.guiScoreBar.getRegionHeight()-10;
		
		multiplierX = 10;
		multiplierY = 10;
		
		largeNotifications = notifications;
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		multiplierTime -= deltaTime;
		
		if(multiplierTime < 0 && multiplier > 1){
		    multiplier /= 2;
		    multiplierTime = 5;
		    
		    if (multiplier == 1) {
		    	multiplierTime = 0;
		    }
		}
		
		spriteBatch.draw(Art.guiScoreBar, x, y);
		if(scaleMultiplier != 1){
			Art.fontKomika24Gold.setScale(scaleMultiplier);
			Art.fontKomika24Gold.draw(spriteBatch, scoreText, x + 56, y + 41 );
			Art.fontKomika24Gold.setScale(1);
		} else {
			Art.fontKomika24Gold.draw(spriteBatch, scoreText, x + 56, y + 41);
		}
		
		// Draw multiplier
		spriteBatch.draw(Art.guiMultiplierBar, multiplierX, multiplierY);
		
		Art.fontKomika24Gold.draw(spriteBatch, multiplierText, multiplierX + 50, multiplierY + 40);
		
		// Reduce scale multiplier if it's not normal
		if(scaleMultiplier > 1){
			scaleMultiplier -= deltaTime *0.5;
			if(scaleMultiplier < 1)
				scaleMultiplier = 1;
		}
		
	}
	
	public void onStarPickedUp() {
		starsPickedUp++;
		if(starsPickedUp == 50){
			/*
			 * ACHIEVEMENT: Touch the sky
			 */
			Achievements.unlockAchievement(Achievements.touchTheSky);
		}
		
		starsInSingleSlide++;
		
		if (starsInSingleSlide > 1 || multiplierTime > 0) {
			if(multiplier == 1 && starsInSingleSlide == 2){
				largeNotifications.add(Art.textComboStarted);
				
				/*
				 * ACHIEVEMENT: C-C-C-Combo
				 */
				Achievements.unlockAchievement(Achievements.ccombo);
			}
			multiplierTime = 3;
			multiplier++;
			multiplierText = multiplier+"";
			
			if(multiplier == 15){
				/*
				 * ACHIEVEMENT: C-C-C-Combo
				 */
				Achievements.unlockAchievement(Achievements.ccombo);
			}
		}

	}
	
	public int getMultiplier() {
		return multiplier;
	}
	
	public void add(int x){
		score += multiplier * x;
		scoreText = String.format("%09d", score);
		scaleMultiplier = 1.2f;
		if(score > 10000){
			/*
			 * ACHIEVEMENT: Allmost a millionaire
			 */
			Achievements.unlockAchievement(Achievements.almostAMillionaire);
		}
	}
	
	public void set(int score){
		this.score = score;
		this.scoreText = String.format("%09d", score);
	}
	
	public void reset(){
		this.set(0);
		starsPickedUp = 0;
		resetMultiplier();
	}
	
	public void resetMultiplier() {
		this.multiplier = 1;
		this.multiplierTime = 0;
		this.multiplierText = "1";
	}
	
	int numDigits(int x){
		return (x == 0 ? 0 : 
			(x < 10 ? 1 :   
	    	(x < 100 ? 2 :   
	    	(x < 1000 ? 3 :   
	    	(x < 10000 ? 4 :   
	    	(x < 100000 ? 5 :   
	    	(x < 1000000 ? 6 :   
	    	(x < 10000000 ? 7 :  
	    	(x < 100000000 ? 8 :  
	    	(x < 1000000000 ? 9 :  
	    	10))))))))));
	}
	
}
