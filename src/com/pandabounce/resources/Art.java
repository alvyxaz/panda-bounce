package com.pandabounce.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	
	// Fonts
	public static BitmapFont fontDefault;
	
	public static void loadTextures() {
		px = new Texture(Gdx.files.internal("assets/textures/1px.png"));
		
		loadPandaTextures();
		loadOtherTextures();
		
		loadFonts();
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
		panda[0] = new TextureRegion(pandaTexture,0, 0, 89, 103);
	}
	
}
