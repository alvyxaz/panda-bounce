package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.resources.Art;

public class Dust {
	public static int MAX_PARTICLES = 10;
	
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
		
		for(int i = 0; i < MAX_PARTICLES; i++){
			if(timeToLive[i] > 0) {
				spriteBatch.draw(Art.px, x[i], y[i], size[i], size[i]);
				
				// Updating coordinates
				x[i] = (float) Math.cos(angle[i]) * speed[i] * deltaTime;
				y[i] = (float) Math.sin(angle[i]) * speed[i] * deltaTime;
				
				// Reducing time to live
				timeToLive[i] -= deltaTime;
			}
		}
	}
	
	public void startCloud(int count, int x, int y){
		for(int i = 0; i < MAX_PARTICLES; i++){
			if(count <= 0) return;
			
			// Found a free spot for one of the particles
			if(timeToLive[i] <= 0){
				count--;
				this.x[i] 			= x;
				this.y[i] 			= y;
				this.size[i] 		= 10 + 	(float) Math.random() * 20;
				this.speed[i] 		= 20 + 	(float) Math.random() * 10;
				this.angle[i] 		= (float) Math.random() * 3.14f;
				this.timeToLive[i] 	= (float) Math.random() * 2;
			}
			
		}
	}
	
}
