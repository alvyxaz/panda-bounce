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
	public static TextureRegion starEmpty;
	public static TextureRegion hedgehog;
	public static TextureRegion healthBar;
	public static TextureRegion dustCloud;
	
	private static Texture guiTexture;
	public static TextureRegion logo;
	public static TextureRegion [] scoreNumbers;
	public static TextureRegion guiPlay;
	public static TextureRegion guiHighscores;
	public static TextureRegion guiExit;
	
	private static Pixmap bgPixmap;
	public static TextureRegion background;
	
	// Fonts
	public static BitmapFont fontDefault;
	
	public static void loadTextures() {
		px = new Texture(Gdx.files.internal("assets/textures/1px.png"));
		friendlyBlob = new Texture(Gdx.files.internal("assets/textures/friendlyBlob.png"));
		
		loadPandaTextures();
		loadBackgroundTextures();
		loadOtherTextures();
		loadFonts();
		loadGUI();
		
	}
	
	private static void loadGUI(){
		guiTexture = new Texture(Gdx.files.internal("assets/textures/gui.png"));
		logo = new TextureRegion(guiTexture, 0, 36, 297, 164);
		
		scoreNumbers = new TextureRegion[10];
		for(int i = 0; i < scoreNumbers.length; i++){
			scoreNumbers[i] = new TextureRegion(guiTexture, 0 + i*26 + i, 0, 26, 35 );
		}
		
		guiPlay = new TextureRegion(guiTexture, 326, 0, 142, 77);
		guiExit = new TextureRegion(guiTexture, 298, 77, 139, 77);	
		guiHighscores = new TextureRegion(guiTexture, 0, 200, 261, 77);
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
		hedgehog = new TextureRegion(otherTexture, 146 , 0, 51, 78);
		healthBar = new TextureRegion(otherTexture, 197, 0, 1, 21);
		dustCloud = new TextureRegion(otherTexture, 199, 0, 52, 52);
		
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
