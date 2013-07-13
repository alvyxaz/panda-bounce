package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.resources.Art;

public class GuiTimer {

	private int x;
	private int y;
	
	private int [] digits;
	
	public float time;
	private float maxTime;
	
	public boolean enabled = false;
	
	public GuiTimer(int x, int y){
		digits = new int[4];
		this.x = x;
		this.y = y;
	}
	
	public void draw(SpriteBatch spriteBatch){
		Art.fontDefault.draw(spriteBatch, digits[0] + "" + digits[1] + " : " + digits[2] + "" + digits[3], x, y);
	}
	
	public void update(float deltaTime){
		time -= deltaTime;
		digits[0] = (int) (time / 600);
		digits[1] = (int)((time / 60) % 10);
		digits[2] = (int)((time % 60) / 10);
		digits[3] = (int)((time % 60) % 10);
	}
	
	public void setMaxTime(float time){
		this.maxTime = time;
		this.time = time;
	}
	
	public void reset(){
		time = maxTime;
	}
	
	
	
}
