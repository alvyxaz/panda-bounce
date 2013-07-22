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
	private int moveSpeed = 200; // Pixels per second
	private int restlessness = 2;
	private float moveAngle;
	private float dontRegenerateFor = 0;
	private boolean seen = false;
		
	private Body body;
	
	public Hedgehog( World world){
		hitBox = new Rectangle(0, 0, Art.hedgehog.getRegionWidth(), Art.hedgehog.getRegionHeight());
		moveAngle = (float) (Math.random() * Math.PI);
		
		// Creating body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2((0+  hitBox.width/2)*Game.WORLD_TO_BOX , (0+ hitBox.height/2)*Game.WORLD_TO_BOX ));
		bodyDef.angle = moveAngle;
		bodyDef.fixedRotation = true;
		
		// Creating body
		body = world.createBody(bodyDef);
		body.setLinearDamping(0);
		body.setAngularDamping(1f);

		// Creating a shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(hitBox.width/2.4f*Game.WORLD_TO_BOX, hitBox.height/2.8f*Game.WORLD_TO_BOX);
		
		// Creating fixture 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 40f;
		fixtureDef.restitution = 1f; // Maximum bounce ratio
		fixtureDef.filter.categoryBits = PhysicsFilter.CATEGORY_HEDGEHOG;
		fixtureDef.filter.maskBits = PhysicsFilter.MASK_HEDGEHOG;
		
		body.createFixture(fixtureDef);
		body.setUserData("hedgehog");
		
		body.setLinearVelocity(1.8f, 1.8f);
		
		// Cleaning up
		shape.dispose();
	}
	
	public void draw(SpriteBatch spriteBatch){
		/*----------------------------------------------------------------
		 * DRAWING
		 */
		if (!seen) {
			// left
			if (hitBox.x <= 0) {
				spriteBatch.draw(Art.exclamation, 0, hitBox.y);
			}
			// bottom
			if (hitBox.y <= 0) {
				spriteBatch.draw(Art.exclamation, hitBox.x, 0);
			}
			
			// right
			if (hitBox.x >= Game.SCREEN_WIDTH) {
				spriteBatch.draw(Art.exclamation, Game.SCREEN_WIDTH - Art.exclamation.getRegionWidth(), hitBox.y);
			}
			
			// top
			if (hitBox.y >= Game.SCREEN_HEIGHT) {
				spriteBatch.draw(Art.exclamation, hitBox.x, Game.SCREEN_HEIGHT - Art.exclamation.getRegionHeight());
			}
		} else {
			spriteBatch.draw(Art.hedgehog, 
					body.getPosition().x*Game.BOX_TO_WORLD - hitBox.width/2, 	// x
					body.getPosition().y * Game.BOX_TO_WORLD - hitBox.height/2, // y
					hitBox.width/2,	// OriginX
					hitBox.height/2,	// OriginY
					hitBox.width, 	// Width
					hitBox.height,	// Height
					1f,	// ScaleX
					1f,	// ScaleY
					(float)Math.toDegrees(body.getAngle())); // Rotation
			
		}

	}
	
	public void update(float deltaTime) {
		hitBox.x = body.getPosition().x*Game.BOX_TO_WORLD - hitBox.width/2;
		hitBox.y = body.getPosition().y * Game.BOX_TO_WORLD - hitBox.height/2;
		dontRegenerateFor -= deltaTime;

	
		if (!seen) {
			if (Game.SCREEN_RECTANGLE.contains(hitBox)) {
				seen = true;
			}
		}
		
		if(dontRegenerateFor < 0) {
			// Checking if hedgehog is out of the screen
			if(!Game.SCREEN_RECTANGLE.contains(hitBox)){
				regenerate();
				seen = false;
			} 
		}
	}
	
	public void regenerate(){
		int x = 0, y = 0;
		float angle = 0;
		dontRegenerateFor = 3f; // Don't regenerate for 3 seconds
		int wall = Game.random.nextInt(4);
		
		// Checking where it will come from
		switch(wall){
		case 0: // Top
			x = Game.random.nextInt(Game.SCREEN_WIDTH);
			y = Game.SCREEN_HEIGHT + 250;
			angle = (float) Math.atan2(
					Game.SCREEN_HEIGHT/2 - y , 
					Game.SCREEN_WIDTH/2 - x + Game.SCREEN_WIDTH/4 - Game.random.nextInt(Game.SCREEN_WIDTH/2));
			break;
		case 1: // Right
			x = Game.SCREEN_WIDTH + 250;
			y = Game.random.nextInt(Game.SCREEN_HEIGHT);
			angle = (float) Math.atan2(
					Game.SCREEN_HEIGHT/2 - y + Game.SCREEN_HEIGHT/4 - Game.random.nextInt(Game.SCREEN_HEIGHT/2), 
					Game.SCREEN_WIDTH/2 - x);
			break;
		case 2: // Bottom
			x = Game.random.nextInt(Game.SCREEN_WIDTH);
			y = (int)(- hitBox.height*2) - 250;
			angle = (float) Math.atan2(
					Game.SCREEN_HEIGHT/2 - y , 
					Game.SCREEN_WIDTH/2 - x + Game.SCREEN_WIDTH/4 - Game.random.nextInt(Game.SCREEN_WIDTH/2));
			break;
		case 3: // Left
			x = (int)(- hitBox.width*2) - 250;
			y = Game.random.nextInt(Game.SCREEN_HEIGHT);
			angle = (float) Math.atan2(
					Game.SCREEN_HEIGHT/2 - y + Game.SCREEN_HEIGHT/4 - Game.random.nextInt(Game.SCREEN_HEIGHT/2), 
					Game.SCREEN_WIDTH/2 - x);
			break;
		}
		
		float newVelocityX = (float)Math.cos(angle)* moveSpeed * Game.WORLD_TO_BOX ;
		float newVelocityY = (float)Math.sin(angle)* moveSpeed * Game.WORLD_TO_BOX ;
		body.setTransform(x * Game.WORLD_TO_BOX, y * Game.WORLD_TO_BOX, angle - (float)Math.PI/2);
		body.setLinearVelocity(newVelocityX, newVelocityY);
		
		hitBox.x = body.getPosition().x*Game.BOX_TO_WORLD - hitBox.width/2;
		hitBox.y = body.getPosition().y * Game.BOX_TO_WORLD - hitBox.height/2;
	}
}
