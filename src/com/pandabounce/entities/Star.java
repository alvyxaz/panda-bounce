package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class Star {
	
	public Rectangle hitBox;
	public float originalY;			// Used to base levitation on
	
	private float angle = 0;		// Rotation angle
	
	private float opacity = 1;
	
	// Levitation related
	private int levitationDirection = 1;
	private float levitationCoef = 0;
	private float levitation = 0;
	private int maxLevitation = 10;
	
	private Body body;
	public boolean regenerate;
	
	public Star(int x, int y, World world){

		hitBox = new Rectangle(x, y, Art.star.getRegionWidth(), Art.star.getRegionHeight());
		angle = Game.random.nextInt(360);
		levitationCoef = Game.random.nextFloat() * 3.14f;
		originalY = y;
		
		// Creating body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2((x+  hitBox.width/2)*Game.WORLD_TO_BOX , (y+ hitBox.height/2)*Game.WORLD_TO_BOX ));
		
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
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		body.setUserData("star");
		
		// body.getFixtureList().get(0).setUserData(this);
		body.getFixtureList().get(0).setUserData(this);
		// Cleaning up
		shape.dispose();
	}
	
	/*
	 * Regenerate bamboo in a different place
	 */
	public void regenerate(int x, int y) {
		hitBox.x = x;
		hitBox.y = y;
		originalY = y;
		opacity = 0;
		
		body.setTransform((x+  hitBox.width/2)*Game.WORLD_TO_BOX, (y+  hitBox.height/2)*Game.WORLD_TO_BOX, 0f);
		regenerate = false;
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		// Drawing
		spriteBatch.setColor(1, 1, 1, opacity);
		spriteBatch.draw(Art.star, hitBox.x, hitBox.y, hitBox.width/2, hitBox.height/2, hitBox.width, hitBox.height, 1.0f, 1.0f, angle);
		spriteBatch.setColor(Color.WHITE);
		
		// Fade in effect
		if(opacity < 1){
			opacity += deltaTime;
			if(opacity > 1){
				opacity = 1;
			}
		}
		
		// Rotating star
		angle += deltaTime * 40;
	
		// Levitating
		levitationCoef += deltaTime * 2;
		levitation = maxLevitation * (float) Math.sin(levitationCoef);
		hitBox.y = originalY + levitation;
	}
}
