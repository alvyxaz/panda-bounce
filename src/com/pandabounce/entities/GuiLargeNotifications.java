package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pandabounce.MyGame;

public class GuiLargeNotifications {
	
	private TextureRegion [] buffer;
	private int count = 0;
	
	// Current 
	private float x;
	private float y;
	private float height;
	private float width;
	private int activeIndex;
	
	// Destination
	private float destinationX;
	private float destinationY;
	private float originalWidth;
	private float originalHeight;
	
	private int state = 0;
	private static final int IDLE = 0;
	private static final int FLYING_IN = 1;
	private static final int DISPLAYING = 2;
	private static final int FLYING_OUT = 3;
	private static final int HEIGHT_UP = 4;
	private static final int HEIGHT_DOWN = 5;
	
	private float displayTimer = 0;
	
	public GuiLargeNotifications() {
		buffer = new TextureRegion[3];
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		switch(state){
		case IDLE:
			break;
		case FLYING_IN:
			if(x > destinationX){
				x -= deltaTime * 1500;
			} else {
				state = HEIGHT_UP;
			}
			break;
		case HEIGHT_UP:
			if(height < originalHeight){
				height += deltaTime * 500;
			}  else {
				height = originalHeight;
				state = DISPLAYING;
				displayTimer = 1f;
			}
			break;
		case DISPLAYING:
			displayTimer -= deltaTime;
			x -= deltaTime * 20;
			if(displayTimer < 0){
				state = FLYING_OUT;
			}
			break;
		case HEIGHT_DOWN:
			if(height > 5){
				height -= deltaTime * 500;
			} else {
				height = 5;
				state = FLYING_OUT;
			}
			break;
		case FLYING_OUT:
			if(x > -originalWidth){
				x -= deltaTime * 1500;
			} else {
				buffer[activeIndex] = null;
				for(int i = 0; i < buffer.length;i++){
					if(buffer[i] != null){
						prepareData(i);
						activeIndex = i;
						state = FLYING_IN;
						return;
					}
				}
				state = IDLE;
			}
			break;
		}
		
		if(state != IDLE){
			spriteBatch.draw(buffer[activeIndex], x, y, width, height);
		}
	}
	
	public void add(TextureRegion notification){
		for(int i = 0; i < buffer.length; i++) {
			if(buffer[i] == null){
				buffer[i] = notification;	
				
				if(state == IDLE){
					prepareData(i);
					state = FLYING_IN;
					activeIndex = i;
				}
				return;
			}
		}
	}
	
	private void prepareData(int index){
		originalWidth = buffer[index].getRegionWidth();
		originalHeight = buffer[index].getRegionHeight();
		destinationX = MyGame.SCREEN_HALF_WIDTH - originalWidth/2;
		destinationY = MyGame.SCREEN_HALF_HEIGHT - originalHeight/2;
		
		x = MyGame.SCREEN_WIDTH;
		y = MyGame.SCREEN_HALF_HEIGHT - originalHeight/2;
		width = originalWidth;
		height = 5;
	}
	
}
