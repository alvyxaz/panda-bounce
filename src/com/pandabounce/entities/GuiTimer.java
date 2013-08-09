package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.resources.Art;

public class GuiTimer {

	private final int x;
	private final int y;

	private final int[] digits;

	public float time;
	private float maxTime;

	private int previousSecond = -1;

	private String timeString = "00:00";

	public boolean enabled = false;

	public boolean ascending = false;

	private boolean gaveAchievement = false;
	
	public GuiTimer(int x, int y) {
		digits = new int[4];
		this.x = x;
		this.y = y;
	}

	public void draw(SpriteBatch spriteBatch) {
		Art.fontKomika24.setColor(Color.BLACK);

		Art.fontKomika24.draw(spriteBatch, timeString, x, y + 20);
		Art.fontKomika24.setColor(Color.WHITE);
	}

	public void update(float deltaTime) {
		time += ascending ? deltaTime : -deltaTime;
		previousSecond = digits[3];

		digits[0] = (int) (time / 600);
		digits[1] = (int) ((time / 60) % 10);
		digits[2] = (int) ((time % 60) / 10);
		digits[3] = (int) ((time % 60) % 10);

		// If timer view needs to be updated
		if (previousSecond != digits[3]) {
			timeString = String.format("%02d:%02d", (int) (time / 60),
					(int) (time % 60));
			if (!gaveAchievement && (int) (time / 60) == 3) {
					/*
					 * ACHIEVEMENT: The survivor
					 */
					Achievements.unlockAchievement(Achievements.theSurvivor);
			}
		}
	}

	public void setMaxTime(float time) {
		this.maxTime = time;
		this.time = time;
	}

	public void reset() {
		time = maxTime;
		if (ascending) {
			time = 0;
		}
	}

}
