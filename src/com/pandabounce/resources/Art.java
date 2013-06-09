package com.pandabounce.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Art {

	public static Texture px;
	
	private static Texture pandaTexture;
	public static TextureRegion [] panda;
	
	public static void loadTextures() {
		px = new Texture(Gdx.files.internal("assets/textures/1px.png"));
		
		loadPandaTextures();
	}
	
	private static void loadPandaTextures(){
		pandaTexture = new Texture(Gdx.files.internal("assets/textures/panda.png"));
		panda = new TextureRegion[1];
		panda[0] = new TextureRegion(pandaTexture,0, 0, 89, 103);
	}
	
}
