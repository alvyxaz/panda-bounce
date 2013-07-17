package com.pandabounce.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {

	public static Music music;
	
	public static Sound starPickedUp;
	public static Sound wallHit;
	
	public static void loadSounds(){
		music = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/Jaunty-Gumption.mp3"));
		music.setLooping(true);
		
		starPickedUp = Gdx.audio.newSound(Gdx.files.internal("assets/audio/star.wav"));
		wallHit = Gdx.audio.newSound(Gdx.files.internal("assets/audio/wall.wav"));
	
	}
	
	public static void dispose(){
		music.dispose();
	}
}
