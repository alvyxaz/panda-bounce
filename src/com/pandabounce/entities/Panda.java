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
	private float speedCoef = 1;
	private float xVelocity;
	private float yVelocity;
	
	public boolean touched = false;
	
	private Body body;
	
	public Panda(int x, int y, World world) {
		hitBox = new Rectangle(x, y, Art.panda[0].getRegionWidth(), Art.panda[0].getRegionHeight());
		hitBoxCenterX = hitBox.width/2;
		
		// Creating body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2((x+  hitBoxCenterX)*Game.WORLD_TO_BOX , (y+ hitBox.height/2)*Game.WORLD_TO_BOX ));
		
		// Creating body
		body = world.createBody(bodyDef);
		body.setLinearDamping(0);
		body.setAngularDamping(1f);
		
		// Creating a shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Art.panda[0].getRegionWidth()/2*Game.WORLD_TO_BOX, Art.panda[0].getRegionHeight()/2*Game.WORLD_TO_BOX);
		
		// Creating fixture 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.restitution = 1f; // Maximum bounce ratio
		body.createFixture(fixtureDef);
		
		// Cleaning up
		shape.dispose();
		
	}
	
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.draw(Art.panda[0], 
				body.getPosition().x*Game.BOX_TO_WORLD - hitBox.width/2, 	// x
				body.getPosition().y * Game.BOX_TO_WORLD - hitBox.height/2, // y
				hitBox.width/2,	// OriginX
				hitBox.height/2,	// OriginY
				hitBox.width, 	// Width
				hitBox.height,	// Height
				1f,	// ScaleX
				1f,	// ScaleY
				body.getAngle()*Game.BOX_TO_WORLD); // Rotation
	}
	
	public void update(float deltaTime){
		
		if(state == STATE_SLIDING){
			

			hitBox.x = (body.getPosition().x) * Game.BOX_TO_WORLD  - hitBox.width/2;
			hitBox.y = body.getPosition().y * Game.BOX_TO_WORLD  - hitBox.height/2;
			
			// Updating xVelocity and yVelocity sign,
			// TODO: Optimize
			xVelocity = Math.abs(xVelocity) * Math.signum(body.getLinearVelocity().x);
			yVelocity = Math.abs(yVelocity) * Math.signum(body.getLinearVelocity().y);
			
			// Updating physics
			body.setLinearVelocity(xVelocity *speedCoef, yVelocity * speedCoef);
			
			// Updating speed coef
			speedCoef -= deltaTime;
			if(speedCoef < 0) {
				body.setLinearVelocity(0, 0);
				state = STATE_WAITING;
			}
		
		}
	}
	
	public void slide(float dX, float dY){
		state = STATE_SLIDING; dY *= -1;
		xVelocity = dX*7*Game.WORLD_TO_BOX;
		yVelocity = dY*7*Game.WORLD_TO_BOX;
		body.setLinearVelocity(xVelocity, yVelocity);
		speedCoef = 1;
	}
	
}
