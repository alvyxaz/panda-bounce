package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class GuiScore {

	private int score = 0;
	private int maxDigits = 9;
	private int multiplier = 1;
	public int starsInSingleSlide = 0;
	private float multiplierTime;
	
	private int digitWidth;
	private int digitHeight;
	
	private int initialY;
	private int initialX;
	
	private float scaleMultiplier = 1f;
	
	public GuiScore() {
		digitWidth = Art.scoreNumbers[0].getRegionWidth();
		digitHeight = Art.scoreNumbers[1].getRegionHeight();
		
		initialY = Game.SCREEN_HEIGHT - digitHeight - 10;
		initialX = (digitWidth + 2) * maxDigits + 10;
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		multiplierTime -= deltaTime;
		System.out.println("timeleft: " + multiplierTime);
		if(multiplierTime < 0){
		    multiplier = 1;
		    multiplierTime = 0;
		}
		
		int tempScore = score;
		int number;
		int position = initialX;
		
		while(tempScore > 0){
			number = tempScore % 10;
			tempScore /= 10;
			position -= digitWidth +2;
			spriteBatch.draw(Art.scoreNumbers[number], position, initialY, digitWidth * scaleMultiplier, digitHeight * scaleMultiplier);
		}

		int padding = maxDigits - numDigits(score);
		while(padding > 0){
			padding -= 1;
			position -= digitWidth +2;
			spriteBatch.draw(Art.scoreNumbers[0], position, initialY, digitWidth * scaleMultiplier, digitHeight * scaleMultiplier);
		}
		
		Art.fontDefault.draw(spriteBatch, "+" + multiplier, 15, 25);
		
		// Reduce scale multiplier if it's not normal
		if(scaleMultiplier > 1){
			scaleMultiplier -= deltaTime *0.5;
			if(scaleMultiplier < 1)
				scaleMultiplier = 1;
		}
		
	}
	
	public void onStarPickedUp() {
		starsInSingleSlide++;
		
		if (starsInSingleSlide > 1 || multiplierTime > 0) {
			multiplierTime = 5;
			multiplier++;
		}
		
		System.out.println(starsInSingleSlide + "");

	}
	
	public int getMultiplier() {
		return multiplier;
	}
	
	public void add(int x){
		score += multiplier * x;
		scaleMultiplier = 1.3f;
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
