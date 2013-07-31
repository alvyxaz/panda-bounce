package com.pandabounce.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.pandabounce.MyGame;

public class Sounds {

	public static Music music;
	
	public static Sound starPickedUp;
	public static Sound wallHit;
	public static Sound damage;
	public static Sound whoosh;
	public static Sound box;	
	public static Sound achievement;	
	
	private static int maxBufferSize = 2;
	private static Sound 	[] soundBuffer;
	private static boolean 	[] soundImportance;
	private static long 	[] soundTimer;
	
	public static void loadSounds(){
		music = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/Jaunty-Gumption.mp3"));
		music.setLooping(true);
		
		starPickedUp = Gdx.audio.newSound(Gdx.files.internal("assets/audio/star.wav"));
		wallHit = Gdx.audio.newSound(Gdx.files.internal("assets/audio/wall.wav"));
		damage = Gdx.audio.newSound(Gdx.files.internal("assets/audio/damage.wav"));
		whoosh = Gdx.audio.newSound(Gdx.files.internal("assets/audio/whoosh.wav"));
		box = Gdx.audio.newSound(Gdx.files.internal("assets/audio/box.wav"));
		achievement = Gdx.audio.newSound(Gdx.files.internal("assets/audio/achievement.wav"));
	
		soundBuffer = new Sound [3];
		soundImportance = new boolean [3];
		soundTimer = new long [3];
	}
	
	public static void stopMusic(){
		music.stop();
	}
	
	public static void playSound(Sound sound, boolean important){
		if(MyGame.mute) return;
		
		long currentTime = System.currentTimeMillis();
		int newIndex = -1;
		float lowestTime = Float.MAX_VALUE;
		for(int i = 0; i < maxBufferSize; i++){
			if(currentTime - soundTimer[i] > 3000 || soundBuffer[i] == null){
				newIndex = i;
				break;
			} else {
				if(!soundImportance[i] && lowestTime > soundTimer[i]){
					lowestTime = soundTimer[i];
					newIndex = i;
				}
			}
		}
		if(newIndex != -1){
			if(soundBuffer[newIndex] != null) {
				soundBuffer[newIndex].stop();
			}
			soundBuffer[newIndex] = sound;
			soundBuffer[newIndex].stop();
			soundBuffer[newIndex].play();
			soundImportance[newIndex] = important;
			soundTimer[newIndex] = System.currentTimeMillis();
		}
		
	}
	
	
	
	public static void dispose(){
		music.dispose();
		
		starPickedUp.dispose();
		wallHit.dispose();
		damage.dispose();
		whoosh.dispose();
		box.dispose();
		achievement.dispose();
	}

}
