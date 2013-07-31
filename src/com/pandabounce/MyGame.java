package com.pandabounce;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.controls.Input;
import com.pandabounce.entities.Achievements;
import com.pandabounce.resources.*;
import com.pandabounce.screens.*;

public class MyGame extends Game implements ApplicationListener {
	public static int SCREEN_WIDTH = 480;
	public static int SCREEN_HEIGHT = 800;
	public static int SCREEN_HALF_WIDTH = 240;
	public static int SCREEN_HALF_HEIGHT = 400;
	public static Rectangle SCREEN_RECTANGLE;
	public static boolean isAndroid;
	public static Random random;
	
	public static float BOX_TO_WORLD = 100f;
	public static float WORLD_TO_BOX = 0.01f;
	
	public static Preferences preferences;
	
	public static boolean mute;
	
	public static GoogleInterface google;
	
	public TitleScreen screenTitle;
	public ModeSurvival screenSurvival;
	
	public MyGame(GoogleInterface google){
		this.google = google;
	}
	
    public void create () {
    	preferences = Gdx.app.getPreferences("prefs");
    	mute = preferences.getBoolean("mute", false);
    	
    	Achievements.loadData();
    	
    	// Preparing OpenGL viewport
    	calculateScreenSize();
		Gdx.graphics.getGLCommon().glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Preparing helpers
		Input.initialize();
		random = new Random();
		isAndroid = Gdx.app.getType() == ApplicationType.Android;
		
		// Loading resources
		Art.loadTextures();
		Sounds.loadSounds();
		
		// Initializing screens
		screenTitle = new TitleScreen(this);
		screenSurvival = new ModeSurvival(this);
		
		// Setting first screen to render
		setScreen(screenTitle);
    }

    public void render () {
    	Input.update();
		getScreen().render(Gdx.graphics.getDeltaTime());
    }
    
    /*
     * Dynamically calculates viewport size, according to the window ratio.
     */
    public void calculateScreenSize(){
		float ratio = Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
		int minSize = SCREEN_HEIGHT;
		
		SCREEN_WIDTH = (int)(minSize * ratio);
		SCREEN_HEIGHT = minSize;
		
		SCREEN_HALF_WIDTH = SCREEN_WIDTH/2;
		SCREEN_HALF_HEIGHT = SCREEN_HEIGHT/2;
		
		SCREEN_RECTANGLE = new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	}

    public void resize (int width, int height) {
    }

    public void pause () {
    }

    public void resume () {
    }

    // TODO : Dispose all resources
    public void dispose () {
    	Sounds.dispose();
    	Art.dispose();
    }
}
