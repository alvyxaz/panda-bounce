package com.pandabounce.entities;

import java.lang.reflect.Field;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.pandabounce.MyGame;

public class Achievements {
	
	/*
	 * Achievements
	 */
	public static final int LOCKED = 0; 
	public static final int UNLOCKED_OFFLINE = 1; 
	public static final int UNLOCKED_ONLINE = 2; 
	
	public static int count = 13;
	public static final int pandasFirstSteps = 0;
	public static final int waspEncounter = 1;
	public static final int hedgehogEncounter = 2;
	public static final int touchTheSky = 3;
	public static final int riskTaker = 4;
	public static final int doubleTheFun = 5;
	public static final int iveBeenExpectingYou = 6;
	public static final int ccombo = 7;
	public static final int groundShaker = 8;
	public static final int firstBlood = 9;
	public static final int allergicToBees = 10;
	public static final int almostAMillionaire = 11;
	public static final int theSurvivor = 12;
	
	private static int [] achievements;
	private static String [] achievementTitles;
	
	public static void loadData(){
		/*
		 * Achievements
		 */
		Preferences prefs = Gdx.app.getPreferences("achievements");
		
		// Cleaning up achievements
		for(int i = 0; i < count; i++)
			prefs.putInteger(Integer.toString(i), LOCKED);
		prefs.flush();
		
		achievements = new int[count];
		for(int i = 0; i < count; i++){
			achievements[i] = prefs.getInteger(Integer.toString(i), LOCKED);
		}
		
		achievementTitles = new String[count];
		achievementTitles[0] = "achiev_pandas_first_steps";
		achievementTitles[1] = "achiev_wasp_encounter";
		achievementTitles[2] = "achiev_hedgehog_encounter";
		achievementTitles[3] = "achiev_touch_the_sky";
		achievementTitles[4] = "achiev_risk_taker";
		achievementTitles[5] = "achiev_double_the_fun";
		achievementTitles[6] = "achiev_ive_been_expecting_you";
		achievementTitles[7] = "achiev_ccombo";
		achievementTitles[8] = "achiev_ground_shaker";
		achievementTitles[9] = "achiev_first_blood";
		achievementTitles[10] = "achiev_allergic_to_bees";
		achievementTitles[11] = "achiev_almost_a_millionaire";
		achievementTitles[12] = "achiev_the_survivor";
	}
	
	public static void unlockAchievement(int ach){
		if(achievements[ach] != LOCKED) return;
		
		Preferences prefs = Gdx.app.getPreferences("achievements");
		
		if(MyGame.google.isSignedIn()){
			MyGame.google.unlockAchievement(achievementTitles[ach]);
			achievements[ach] = UNLOCKED_ONLINE;
			prefs.putInteger(Integer.toString(ach), UNLOCKED_ONLINE);
		} else {
			achievements[ach] = UNLOCKED_OFFLINE;
			prefs.putInteger(Integer.toString(ach), UNLOCKED_OFFLINE);
			System.out.println("ACHIEVEMENT: " + achievementTitles[ach]);
		}
		prefs.flush();
	}
	
	public static void pushOnline(){
		Preferences prefs = Gdx.app.getPreferences("achievements");
		for(int i = 0; i < count; i++){
			if(achievements[i]  == UNLOCKED_OFFLINE){
				MyGame.google.unlockAchievement(achievementTitles[i]);
				achievements[i] = UNLOCKED_ONLINE;
				prefs.putInteger(Integer.toString(i), UNLOCKED_ONLINE);
			}
		}
	}
	
}
