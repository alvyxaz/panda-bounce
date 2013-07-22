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
	public static TextureRegion [] pandaIdle;
	public static TextureRegion [] pandaHorizontal;
	public static TextureRegion [] pandaVertical;

	public static Texture friendlyBlob;
	
	private static Texture otherTexture;
	public static TextureRegion targetArrow;
	public static TextureRegion star;
	public static TextureRegion starParticle;
	public static TextureRegion hedgehog;
	public static TextureRegion healthBar;
	public static TextureRegion dustCloud;
	public static TextureRegion box;
	public static TextureRegion textComboStarted;
	public static TextureRegion textComboOver;
	public static TextureRegion [] bee;
	public static TextureRegion exclamation;
	
	
	private static Texture guiTexture;
	public static TextureRegion logo;
	public static TextureRegion guiScoreBar;
	public static TextureRegion guiHealthBar;
	public static TextureRegion guiHealth;
	public static TextureRegion guiMultiplierBar;
	public static TextureRegion guiEffectBar;
	public static TextureRegion guiButtonSmall;
	public static TextureRegion guiButtonBig;
	
	
	private static Texture guiOtherTexture;
	public static TextureRegion guiEndWindow;
	public static TextureRegion guiPlayAgainButton;
	public static TextureRegion guiSubmitScoreButton;
	
	private static Pixmap bgPixmap;
	public static TextureRegion background;
	
	// Fonts
	public static BitmapFont fontDefault;
	public static BitmapFont fontKomika24Gold;
	public static BitmapFont fontKomika24;
	
	public static void loadTextures() {
		px = new Texture(Gdx.files.internal("assets/textures/1px.png"));
		friendlyBlob = new Texture(Gdx.files.internal("assets/textures/friendlyBlob.png"));
		
		loadPandaTextures();
		loadBackgroundTextures();
		loadOtherTextures();
		loadFonts();
		loadGUI();
		loadOtherGUI();
		
	}
	
	private static void loadOtherGUI() {
		guiOtherTexture = new Texture(Gdx.files.internal("assets/textures/guiOther.png"));
		guiEndWindow = new TextureRegion(guiOtherTexture, 0, 0, 364, 267);
		guiPlayAgainButton = new TextureRegion(guiOtherTexture, 364, 88, 137, 88);
		guiSubmitScoreButton = new TextureRegion(guiOtherTexture, 364, 0, 146, 88);
	}
	
	private static void loadGUI(){
		guiTexture = new Texture(Gdx.files.internal("assets/textures/gui.png"));
		logo = new TextureRegion(guiTexture, 0, 36, 297, 164);
		
		guiScoreBar = new TextureRegion(guiTexture, 277, 200, 233, 53);
		guiHealthBar = new TextureRegion(guiTexture, 297, 45, 193, 51);
		guiHealth = new TextureRegion(guiTexture, 324, 0, 1, 23);
		guiMultiplierBar = new TextureRegion(guiTexture, 325, 0, 90, 45);
		guiEffectBar = new TextureRegion(guiTexture, 0, 277, 254, 38);
		guiButtonBig = new TextureRegion(guiTexture, 0, 214, 274, 63);
		guiButtonSmall = new TextureRegion(guiTexture, 0, 315, 147, 63);
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
		fontKomika24Gold = new BitmapFont(Gdx.files.internal("assets/fonts/komika-24-gold.fnt"), 
				Gdx.files.internal("assets/fonts/komika-24-gold.png"), false);
		fontKomika24 = new BitmapFont(Gdx.files.internal("assets/fonts/komika-24.fnt"), 
				Gdx.files.internal("assets/fonts/komika-24.png"), false);
	}
	
	private static void loadOtherTextures() {
		otherTexture = new Texture(Gdx.files.internal("assets/textures/other.png"));
		targetArrow = new TextureRegion(otherTexture, 0, 0, 44, 119);
		star = new TextureRegion(otherTexture, 14, 0, 53, 50);
		starParticle = new TextureRegion(otherTexture, 227, 159, 48, 48);
		hedgehog = new TextureRegion(otherTexture, 146 , 0, 51, 78);
		healthBar = new TextureRegion(otherTexture, 197, 0, 1, 21);
		dustCloud = new TextureRegion(otherTexture, 199, 0, 52, 52);
		box = new TextureRegion(otherTexture, 251, 0, 77, 83);
		
		textComboStarted = new TextureRegion(otherTexture, 0, 83, 261, 37);
		textComboOver = new TextureRegion(otherTexture, 263, 83, 208, 37);
		
		bee = new TextureRegion[2];
		bee[0] = new TextureRegion(otherTexture, 84, 157, 68, 47);
		bee[1] = new TextureRegion(otherTexture, 152, 157, 72, 47);
		
		exclamation = new TextureRegion(otherTexture, 0, 0, 14, 49);
		
		
	}
	
	private static void loadPandaTextures(){
		pandaTexture = new Texture(Gdx.files.internal("assets/textures/panda.png"));
		pandaIdle = new TextureRegion[1];
		pandaIdle[0] = new TextureRegion(pandaTexture,0, 0, 78, 105);
		
		pandaVertical = new TextureRegion[8];
		pandaVertical[0] = new TextureRegion(pandaTexture, 83, 0, 70,104);
		pandaVertical[1] = new TextureRegion(pandaTexture, 155, 0, 70,104);
		pandaVertical[2] = new TextureRegion(pandaTexture, 228, 0, 70,104);
		pandaVertical[3] = new TextureRegion(pandaTexture, 300, 0, 74,104);
		pandaVertical[4] = new TextureRegion(pandaTexture, 378, 0, 71,104);
		pandaVertical[5] = new TextureRegion(pandaTexture, 2, 108, 71,104);
		pandaVertical[6] = new TextureRegion(pandaTexture, 77, 108, 71,104);
		pandaVertical[7] = new TextureRegion(pandaTexture, 152, 108, 71,104);
	
		pandaHorizontal = new TextureRegion[8];
		pandaHorizontal[0] = new TextureRegion(pandaTexture, 225, 104, 71,104);
		pandaHorizontal[1] = new TextureRegion(pandaTexture, 298, 104, 71,105);
		pandaHorizontal[2] = new TextureRegion(pandaTexture, 3, 215, 64,104);
		pandaHorizontal[3] = new TextureRegion(pandaTexture, 67, 215, 64,104);
		pandaHorizontal[4] = new TextureRegion(pandaTexture, 133, 215, 70,105);
		pandaHorizontal[5] = new TextureRegion(pandaTexture, 206, 215, 62,105);
		pandaHorizontal[6] = new TextureRegion(pandaTexture, 270, 214, 63,106);
		pandaHorizontal[7] = new TextureRegion(pandaTexture, 335, 215, 72,106);
	}
	
}
