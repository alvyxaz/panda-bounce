package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class Hedgehog {
	
	public Rectangle hitBox;
	
	// movement related vars
	private int moveSpeed = 60;
	private int restlessness = 3;
	private float moveAngle;
	
	private Body body;
	
	public Hedgehog(int x, int y, World world){
		hitBox = new Rectangle(x, y, Art.hedgehog.getRegionWidth(), Art.hedgehog.getRegionHeight());
		moveAngle = (float) (Math.random() * Math.PI);
		
		// Creating body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2((x+  hitBox.width/2)*Game.WORLD_TO_BOX , (y+ hitBox.height/2)*Game.WORLD_TO_BOX ));
		bodyDef.angle = moveAngle;

		// Creating body
		body = world.createBody(bodyDef);
		body.setLinearDamping(0);
		body.setAngularDamping(1f);


		// Creating a shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(hitBox.width/2*Game.WORLD_TO_BOX, hitBox.height/2*Game.WORLD_TO_BOX);
		
		// Creating fixture 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.restitution = 1f; // Maximum bounce ratio
		body.createFixture(fixtureDef);
		
		// Cleaning up
		shape.dispose();
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		/*****************************************************
		 * DRAWING
		 */
		spriteBatch.draw(Art.hedgehog, 
				body.getPosition().x*Game.BOX_TO_WORLD - hitBox.width/2, 	// x
				body.getPosition().y * Game.BOX_TO_WORLD - hitBox.height/2, // y
				hitBox.width/2,	// OriginX
				hitBox.height/2,	// OriginY
				hitBox.width, 	// Width
				hitBox.height,	// Height
				1f,	// ScaleX
				1f,	// ScaleY
				body.getAngle()*Game.BOX_TO_WORLD); // Rotation
		
		/*****************************************************
		 * UPDATING
		 */
		// Velocity
//		float newVelocityX = (float)Math.cos(body.getAngle())* moveSpeed * deltaTime ;
//		float newVelocityY = (float)Math.sin(body.getAngle())* moveSpeed * deltaTime ;
//		body.setLinearVelocity(newVelocityX, newVelocityY);
		
	}
}
