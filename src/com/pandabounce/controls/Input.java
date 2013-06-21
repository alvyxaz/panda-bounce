package com.pandabounce.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.Game;


public class Input {

	public static Touch[] touch;
	
	public static float xRatio;
	public static float yRatio;
	
	private static KeyboardKey [] keys;
	
	public static int getX(){
		return touch[0].x;
	}
	
	public static int getY() {
		return touch[0].y;
	}
	
	public static void update() {
		for(int i= 0; i < touch.length; i++){
			touch[i].lastTouched = touch[i].touched;
			if(Gdx.input.isTouched(i)){
				touch[i].touched = true;
				touch[i].x = (int)(Gdx.input.getX(i) * xRatio);
				touch[i].y = Game.SCREEN_HEIGHT - (int)(Gdx.input.getY(i) * yRatio);
			} else {
				touch[i].touched = false;
			}
		}
		
		for(int i = 0; i < keys.length; i++){
			keys[i].lastPressed = keys[i].pressed;
			keys[i].pressed = Gdx.input.isKeyPressed(keys[i].key);
		}
	}
	
	/*
	 * Check whether a rectangle is being touched at the moment
	 */
	public static boolean isTouching(Rectangle rect){
		for(int i = 0; i < 2; i++){
			if(!touch[i].touched) continue;
			if(rect.x <= touch[i].x && rect.x+rect.width >= touch[i].x){	
				if(rect.y <= touch[i].y && rect.y+rect.height >= touch[i].y)
					return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Check whether a rectangle has been released after a touck.
	 * 
	 * TODO: Seems like something's not working
	 * 
	 * @param rect Rectangle of interest
	 */
	public static boolean isReleasing(Rectangle rect){
		for(int i = 0; i < 2; i++){
			if(touch[i].touched) continue;
			if(!touch[i].lastTouched) continue;
			if(rect.x <= touch[i].x && rect.x+rect.width >= touch[i].x){	
				if(rect.y <= touch[i].y && rect.y+rect.height >= touch[i].y)
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Check whether a certain keyboard key was released. Have in mind
	 * that in order to track a key, it has to be registered in keys array,
	 * which happens in initialize() method.
	 * 
	 * @param rect Rectangle of interest
	 */
	public static boolean keyReleased(int key){
		for(int i = 0; i < keys.length; i++){
			if(keys[i].key == key)
				return keys[i].lastPressed && ! keys[i].pressed;
		}
		return false;
	}
	
	/**
	 * In case there are multiple touches in the screen, return an index 
	 * of touch that is touching a given rectangle
	 * 
	 * @param rect Rectangle of interest
	 * @return Index of touch, -1 if not touching.
	 */
	public static int getTouchIndex(Rectangle rect){
		for(int i = 0; i < 2; i++){
			if(!Gdx.input.isTouched(i)) continue;
			if(rect.x <= touch[i].x && rect.x+rect.width >= touch[i].x){	
				if(rect.y <= Game.SCREEN_HEIGHT-touch[i].y && rect.y+rect.height >= Game.SCREEN_HEIGHT-touch[i].y)
					return i;
			}
		}
		return -1;
	}
	
	public static void initialize(){
		touch = new Touch[2];
		
		xRatio = Game.SCREEN_WIDTH / (float)Gdx.graphics.getWidth();
		yRatio = Game.SCREEN_HEIGHT / (float)Gdx.graphics.getHeight();
		for(int i= 0; i < touch.length; i++)
			touch[i] = new Touch();
		
		keys = new KeyboardKey[6];
		keys[0] = new KeyboardKey(Keys.F12);
		keys[1] = new KeyboardKey(Keys.PLUS);
		keys[2] = new KeyboardKey(Keys.MINUS);
		keys[3] = new KeyboardKey(Keys.G);
		keys[4] = new KeyboardKey(Keys.C);
		keys[5] = new KeyboardKey(Keys.F5);
	}
	


}
