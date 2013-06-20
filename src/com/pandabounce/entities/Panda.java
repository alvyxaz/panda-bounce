package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class Panda {
	public Rectangle hitBox;
	public float hitBoxCenterX;	// Relative Y center of the hitBox
	
	public float health = 100; // Percentage
	
	// Basic states
	public int state = 0;
	private int STATE_WAITING = 0;
	private int STATE_SLIDING = 1;
	
	// Jump related
	private float xVelocity = 0;
	private float yVelocity = 0;
	private float speedCoef = 1;
	
	public boolean touched = false;
	
	private Body body;
	
	public Panda(int x, int y, World world) {
		
		// Creating body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x, y));
		
		// Creating body
		body = world.createBody(bodyDef);
		body.setLinearDamping(0);
		
		// Creating a shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Art.panda[0].getRegionWidth()/2, Art.panda[0].getRegionHeight()/2);
		
		// Creating fixture 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.restitution = 1f; // Maximum bounce ratio
		body.createFixture(fixtureDef);
		
		// Cleaning up
		shape.dispose();
		
		hitBox = new Rectangle(x, y, Art.panda[0].getRegionWidth(), Art.panda[0].getRegionHeight());
		hitBoxCenterX = hitBox.width/2;
	}
	
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.draw(Art.panda[0], hitBox.x, hitBox.y);
		
		// Debugging hitbox
//		spriteBatch.setColor(0, 0, 1, 0.5f);
//		spriteBatch.draw(Art.px, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
//		spriteBatch.setColor(Color.WHITE);
	}
	
	public void update(float deltaTime){
		
		if(state == STATE_SLIDING){
			
			hitBox.x += xVelocity * speedCoef * deltaTime;
			hitBox.y += yVelocity * speedCoef * deltaTime;

			System.out.println(( yVelocity * speedCoef) + " " + body.getLinearVelocity().y);
			
			// Updating physics
			body.setLinearVelocity(xVelocity * speedCoef, yVelocity * speedCoef);
		
			
			speedCoef -= deltaTime;
			
			if(speedCoef < 0) {
				body.setLinearVelocity(0, 0);
				state = STATE_WAITING;
			}
			
			// colission with room
			if (hitBox.x < 0) {
				hitBox.x = 0;
				xVelocity *= -1;
			}
			
			if (hitBox.x + hitBox.width > Game.SCREEN_WIDTH) {
				hitBox.x = Game.SCREEN_WIDTH - hitBox.width;
				xVelocity *= -1;
			}
			
			if (hitBox.y < 0) {
				hitBox.y = 0;
				yVelocity *= -1;
			}
			
			if (hitBox.y + hitBox.height > Game.SCREEN_HEIGHT) {
				hitBox.y = Game.SCREEN_HEIGHT - hitBox.height;
				yVelocity *= -1;
			}
			
		}
	}
	
	public void slide(float dX, float dY){
		state = STATE_SLIDING; dY *= -1;
		xVelocity = xVelocity * speedCoef + dX*7;
		yVelocity = yVelocity * speedCoef + dY*7;
		speedCoef = 1;
	}
	
}
