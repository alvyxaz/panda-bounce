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
import com.pandabounce.MyGame;
import com.pandabounce.resources.Art;

public class SurpriseBox {
	
	/**
	 * Effect types
	 * 1 - "confused mode", make panda roll to opposite direction 
	 *		than it is poked
	 * 2 - decrease pace of the game (everything)
	 * 3 - decrease panda speed
	 * 4 - increase pace of the game (everything)
	 * 5 - increase panda speed
	 * 6 - 2x points - 2x dmg
	 * 7 - Berserk
	 * 8 - Invincibility
	 */
	public int type;
	public float effectTime = 5f;
	
	public Rectangle hitBox;
	
	private float opacity = 1f;
	
	private Body body;
	
	public float regenerationTimer = 5f;
	public boolean regenerate = false;
	
	public SurpriseBox(int x, int y, World world){

		hitBox = new Rectangle(x, y, Art.box.getRegionWidth(), Art.box.getRegionHeight());
		type = MyGame.random.nextInt(8) + 1;
		
		// Creating body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2((x+  hitBox.width/2)*MyGame.WORLD_TO_BOX , (y+ hitBox.height/2)*MyGame.WORLD_TO_BOX ));
		
		// Creating body
		body = world.createBody(bodyDef);
		body.setLinearDamping(0);
		body.setAngularDamping(1f);

		// Creating a shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(hitBox.width/2*MyGame.WORLD_TO_BOX, hitBox.height/2*MyGame.WORLD_TO_BOX);
		
		// Creating fixture 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.restitution = 1f; // Maximum bounce ratio
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		body.setUserData("surprise_box");
		
		// body.getFixtureList().get(0).setUserData(this);
		body.getFixtureList().get(0).setUserData(this);
		// Cleaning up
		shape.dispose();
	}
	
	public void draw(SpriteBatch spriteBatch){
		if (regenerationTimer < 0) {
			// Drawing
			spriteBatch.setColor(1, 1, 1, opacity);
			spriteBatch.draw(Art.box, hitBox.x, hitBox.y);
			spriteBatch.setColor(Color.WHITE);

		}
		
	}
	
	public void update(float deltaTime){
		if (regenerationTimer < 0) {
			// Fade in effect
			if(opacity < 1){
				opacity += deltaTime;
				if(opacity > 1){
					opacity = 1;
				}
			}
		}
		
		regenerationTimer -= deltaTime;
	}
	
	public void regenerate(int x, int y)
	{
		regenerationTimer = MyGame.random.nextFloat() * 25;
		type = MyGame.random.nextInt(8) + 1;
		
		hitBox.x = x;
		hitBox.y = y;
		opacity = 0;
		
		body.setTransform((x+  hitBox.width/2)*MyGame.WORLD_TO_BOX, (y+  hitBox.height/2)*MyGame.WORLD_TO_BOX, 0f);
		regenerate = false;
	}
}
