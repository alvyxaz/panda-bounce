package com.pandabounce.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pandabounce.Game;

public class Art {

	// Single pixel texture. For debugging purposes
	public static Texture px;
	
	private static Texture pandaTexture;
	public static TextureRegion [] panda;
	
	private static Texture otherTexture;
	public static TextureRegion targetArrow;
	public static TextureRegion star;
	public static TextureRegion starEmpty;
	
	private static Texture guiTexture;
	public static TextureRegion logo;
	
	private static Pixmap bgPixmap;
	public static TextureRegion background;
	
	// Fonts
	public static BitmapFont fontDefault;
	
	public static void loadTextures() {
		px = new Texture(Gdx.files.internal("assets/textures/1px.png"));
		
		loadPandaTextures();
		loadBackgroundTextures();
		loadOtherTextures();
		loadFonts();
	}
	
	private static void loadBackgroundTextures(){
		bgPixmap = new Pixmap(Gdx.files.internal("assets/textures/background.png"));
		
		// Calculating new texture size
		float ratio = (float)Game.SCREEN_WIDTH/(float)Game.SCREEN_HEIGHT;
		int sourceWidth, sourceHeight;
		
		if(ratio > 1){
			sourceWidth = bgPixmap.getWidth();
			sourceHeight = Game.SCREEN_HEIGHT/Game.SCREEN_WIDTH * bgPixmap.getHeight();
		} else {
			sourceWidth = (int)(bgPixmap.getWidth() * ratio);
			sourceHeight = bgPixmap.getHeight();
		}
		
		// Creating a pixmap
		Pixmap temp = new Pixmap(bgPixmap.getWidth(), bgPixmap.getHeight(), Pixmap.Format.RGBA8888);
		temp.drawPixmap(bgPixmap, 0, 0, sourceWidth, sourceHeight, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
	
		// Creating a texture
		Texture bgTexture = new Texture(temp);
		background = new TextureRegion(bgTexture, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
		// TODO: dispose
		temp.dispose();
		bgPixmap.dispose();
	}
	
	private static void loadFonts() {
		fontDefault = new BitmapFont();
	}
	
	private static void loadOtherTextures() {
		otherTexture = new Texture(Gdx.files.internal("assets/textures/other.png"));
		targetArrow = new TextureRegion(otherTexture, 0, 0, 44, 119);
		star = new TextureRegion(otherTexture, 44, 0, 51, 48);
		starEmpty = new TextureRegion(otherTexture, 95, 0, 51, 48);
		
		guiTexture = new Texture(Gdx.files.internal("assets/textures/gui.png"));
		logo = new TextureRegion(guiTexture, 0, 0, 231, 86);
	}
	
	private static void loadPandaTextures(){
		pandaTexture = new Texture(Gdx.files.internal("assets/textures/panda.png"));
		panda = new TextureRegion[1];
		panda[0] = new TextureRegion(pandaTexture,0, 0, 78, 105);
	}
	
}
