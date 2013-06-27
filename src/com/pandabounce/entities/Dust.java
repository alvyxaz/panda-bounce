package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.resources.Art;

public class Dust {
	public static int MAX_PARTICLES = 15;
	
	// Dust particles data
	public float [] x;
	public float [] y;
	public float [] size;
	public float [] timeToLive;
	public float [] speed;
	public float [] angle;
	
	public Dust() {
		x = new float[MAX_PARTICLES];
		y = new float[MAX_PARTICLES];
		size = new float[MAX_PARTICLES];
		timeToLive = new float[MAX_PARTICLES];
		speed = new float[MAX_PARTICLES];
		angle = new float[MAX_PARTICLES];
		
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		spriteBatch.setColor(1, 1, 1, 0.5f);
		for(int i = 0; i < MAX_PARTICLES; i++){
			if(timeToLive[i] > 0) {
				spriteBatch.draw(Art.dustCloud, x[i], y[i], size[i], size[i]);
				
				// Updating coordinates
				x[i] += (float) Math.cos(angle[i]) * speed[i] * deltaTime;
				y[i] += (float) Math.sin(angle[i]) * speed[i] * deltaTime;

				size[i] += deltaTime*20;
				
				// Reducing time to live
				timeToLive[i] -= deltaTime;
			}
		}
		spriteBatch.setColor(Color.WHITE);
	}
	
	public void startCloud(int count, float x, float y){
		for(int i = 0; i < MAX_PARTICLES; i++){
			if(count <= 0) return;
			
			// Found a free spot for one of the particles
			if(timeToLive[i] <= 0){
				count--;
				this.size[i] 		= 30 + 	(float) Math.random() * 40;
				this.x[i] 			= x -10 + (float)Math.random() * 20 - this.size[i]/2;
				this.y[i] 			= y -10 + (float)Math.random() * 20 - this.size[i]/2;;
				this.speed[i] 		= 10 + 	(float) Math.random() * 10;
				this.angle[i] 		= (float) Math.random() * 3.14f;
				this.timeToLive[i] 	= (float) Math.random();
				
			}
			
		}
	}
	
}
