package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.MyGame;
import com.pandabounce.resources.Art;

public class GuiEndWindow {

	private float x;
	private final float y;
	private final float width;
	private final float height;

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

	private int[] commentScore;
	private String[] commentText;

	public GuiEndWindow() {
		width = Art.guiEndWindow.getRegionWidth();
		height = Art.guiEndWindow.getRegionHeight();
		x = MyGame.SCREEN_HALF_WIDTH - width / 2;
		y = MyGame.SCREEN_HALF_HEIGHT - height / 2;

		submitScore = new Rectangle(x + 20, y - 27,
				Art.guiSubmitScoreButton.getRegionWidth(),
				Art.guiSubmitScoreButton.getRegionHeight());
		playAgain = new Rectangle(x + width - 160, y - 27,
				Art.guiPlayAgainButton.getRegionWidth(),
				Art.guiPlayAgainButton.getRegionHeight());

		initializeComments();
	}

	public void initializeComments() {
		int count = 8;
		commentScore = new int[count];
		commentText = new String[count];

		commentScore[0] = 0;
		commentText[0] = "At least it's not negative?..";

		commentScore[1] = 150;
		commentText[1] = "Could do better";

		commentScore[2] = 800;
		commentText[2] = "You're getting the hang of it";

		commentScore[3] = 2500;
		commentText[3] = "That's actually pretty good";

		commentScore[4] = 5000;
		commentText[4] = "So proud of you";

		commentScore[5] = 10000;
		commentText[5] = "Great job!";

		commentScore[6] = 15000;
		commentText[6] = "Like a Pro";

		commentScore[7] = 20000;
		commentText[7] = "You are the 1%";
	}

	public void show(int score) {
		MyGame.ads.showAds();
		state = FLYING_IN;
		this.score = score;
		this.scoreText = "" + score;
		this.x = MyGame.SCREEN_WIDTH;

		for (int i = 0; i < commentScore.length; i++) {
			if (commentScore[i] > score) {
				comment = commentText[i - 1];
				break;
			}
			if (i + 1 == commentScore.length) {
				comment = commentText[i];
			}
		}

		// Saving highscore
		int oldBest = MyGame.preferences.getInteger("best");
		if (score > oldBest) {
			MyGame.preferences.putInteger("best", score);
			MyGame.preferences.flush();
			bestText = "" + score;
		} else {
			bestText = "" + oldBest;
		}

		// Increment games played
		int gamesPlayed = MyGame.preferences.getInteger("games played", 0) + 1;
		MyGame.preferences.putInteger("games played", gamesPlayed);
	}

	public void draw(SpriteBatch spriteBatch, float deltaTime) {
		spriteBatch.draw(Art.guiEndWindow, x, y);
		spriteBatch.draw(Art.guiSubmitScoreButton, x + 20, submitScore.y);
		spriteBatch.draw(Art.guiPlayAgainButton, x + width - 160, playAgain.y);

		Art.fontKomika24Gold.draw(spriteBatch, scoreText, x + 167, y + 200);
		Art.fontKomika24Gold.draw(spriteBatch, bestText, x + 167, y + 168);

		Art.fontKomika24.setScale(0.6f);
		Art.fontKomika24.drawWrapped(spriteBatch, comment, x + 37, y + 120,
				300, HAlignment.CENTER);
		Art.fontKomika24.setScale(1);

		switch (state) {
		case FLYING_IN:
			if (x > MyGame.SCREEN_HALF_WIDTH - width / 2) {
				x -= deltaTime * 1000;
			} else {
				x = MyGame.SCREEN_HALF_WIDTH - width / 2;
				state = INPUT;
			}
			break;
		case INPUT:
			break;
		}
	}

	public boolean isWaitingForInput() {
		return state == INPUT;
	}

}
