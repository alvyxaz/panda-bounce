package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class GuiEndWindow {

	private float x;
	private float y;
	private float width;
	private float height;
	
	public Rectangle playAgain;
	public Rectangle submitScore;
	
	public GuiEndWindow() {
		width = 300;
		height = 160;
		x = Game.SCREEN_HALF_WIDTH - width/2;
		y = Game.SCREEN_HALF_HEIGHT - height/2;
		
		playAgain = new Rectangle(x-10, y - 50, 130, 80);
		submitScore = new Rectangle(x + width - 130 + 10, y - 50, 130, 80);
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		spriteBatch.setColor(0.86f, 0.76f, 0.71f, 1f);
		spriteBatch.draw(Art.px, x, y, width, height);
		
		spriteBatch.setColor(Color.DARK_GRAY);
		spriteBatch.draw(Art.px, playAgain.x, playAgain.y, playAgain.width, playAgain.height);
		spriteBatch.draw(Art.px, submitScore.x, submitScore.y, submitScore.width, submitScore.height);
		spriteBatch.setColor(Color.WHITE);
		
		Art.fontDefault.draw(spriteBatch, "Play Again", playAgain.x+20, playAgain.y+40);
		Art.fontDefault.draw(spriteBatch, "Submit Score", submitScore.x+20, submitScore.y+40);
	}
	
}
