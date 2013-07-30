package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class Bee {

	public Rectangle hitBox;
	
	// movement related vars
	private int moveSpeed = 200; // Pixels per second
	private int restlessness = 2;
	private float moveAngle;
	private float dontRegenerateFor = 0;
	private int targetX, targetY;	
	private float signX, signY;
	private Vector2 pandasPosition;
	
	private Body body;
	
	public int state = 0;
	public final static int FLYING_IN = 0;
	public final static int WAITING = 1;
	public final static int ATTACKING = 2;
	public final static int IDLE = 3;
	
	private float timer;
	
	private int frame = 0;
	private float frameTimer = 0;
	
	public Bee( World world, Vector2 pandasPosition){
		this.pandasPosition = pandasPosition;
		hitBox = new Rectangle(0, 0, Art.bee[0].getRegionWidth(), Art.bee[0].getRegionHeight());
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
		shape.setAsBox(hitBox.width/7*Game.WORLD_TO_BOX, hitBox.width/2.8f*Game.WORLD_TO_BOX);
		
		// Creating fixture 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 40f;
		fixtureDef.restitution = 1f; // Maximum bounce ratio
		fixtureDef.isSensor = true;
		fixtureDef.filter.categoryBits = PhysicsFilter.CATEGORY_BEE;
		fixtureDef.filter.maskBits = ~PhysicsFilter.MASK_PLAYER;
		
		body.createFixture(fixtureDef);
		body.setUserData("bee");
		
		body.setLinearVelocity(1.8f, 1.8f);
		
		// Cleaning up
		shape.dispose();
	}
	
	public void draw(SpriteBatch spriteBatch){
		spriteBatch.draw(Art.bee[frame], 
				body.getPosition().x*Game.BOX_TO_WORLD - hitBox.width/2, 	// x
				body.getPosition().y * Game.BOX_TO_WORLD - hitBox.height/2, // y
				hitBox.width/2,	// OriginX
				hitBox.height/2,	// OriginY
				hitBox.width, 	// Width
				hitBox.height,	// Height
				1f,	// ScaleX
				1f,	// ScaleY
				(float)Math.toDegrees(body.getAngle())); // Rotation
		
		if(state == WAITING && timer < 1){
			spriteBatch.draw(Art.exclamation, hitBox.x, hitBox.y);
		}
	}
	
	public void update(float deltaTime) {
		frameTimer += deltaTime;
		if(frameTimer > 0.02f){
			frameTimer = 0;
			frame = (++frame)%2;
		}
		
		dontRegenerateFor -= deltaTime;
		if(state == IDLE && dontRegenerateFor < 0) {
			regenerate();
		} else if(state == IDLE){
			return;
		}
		
		hitBox.x = body.getPosition().x*Game.BOX_TO_WORLD;
		hitBox.y = body.getPosition().y * Game.BOX_TO_WORLD;
		
		switch(state){
		case FLYING_IN:
			float tempSignX = Math.signum(targetX - hitBox.x);
			float tempSignY = Math.signum(targetY - hitBox.y);
			
			if(tempSignX != signX || tempSignY != signY){
				state = WAITING;
				body.setLinearVelocity(0, 0);
				timer = 3f + Game.random.nextFloat()*3;
			}
			
			signX = tempSignX;
			signY = tempSignY;
			break;
		case WAITING:
			timer -= deltaTime;
			if(timer < 0){
				state = ATTACKING;
				moveAngle = (float) Math.atan2(	pandasPosition.y*Game.BOX_TO_WORLD - hitBox.y , pandasPosition.x*Game.BOX_TO_WORLD - hitBox.x);
				float newVelocityX = (float)Math.cos(moveAngle)* moveSpeed * 2 * Game.WORLD_TO_BOX ;
				float newVelocityY = (float)Math.sin(moveAngle)* moveSpeed * 2 * Game.WORLD_TO_BOX ;
				body.setLinearVelocity(newVelocityX, newVelocityY);
				body.setTransform(body.getPosition().x, body.getPosition().y, moveAngle - (float)Math.PI/2);
				
				Filter filter = body.getFixtureList().get(0).getFilterData();
				filter.maskBits = ~PhysicsFilter.MASK_PLAYER;
				body.getFixtureList().get(0).setFilterData(filter);
				
				/*
				 * ACHIEVEMENT: Wasp encounter
				 */
				Achievements.unlockAchievement(Achievements.waspEncounter);
			}
			break;
		case ATTACKING:
			if(!Game.SCREEN_RECTANGLE.contains(hitBox)){
				state = IDLE;
				dontRegenerateFor = 2 + Game.random.nextInt(5);
			}
			break;
		}
	}
	
	public void regenerate(){
		int x = 0, y = 0;
		state = FLYING_IN;
		
		float angle = 0;
		dontRegenerateFor = 3f; // Don't regenerate for 3 seconds
		
		int wall = Game.random.nextInt(3);
		
		// Checking where it will come from
		switch(wall){
		case 0: // Top
			x = Game.random.nextInt(Game.SCREEN_WIDTH);
			y = Game.SCREEN_HEIGHT;
			
			break;
		case 1: // Right
			x = Game.SCREEN_WIDTH;
			y = Game.random.nextInt(Game.SCREEN_HEIGHT);
			break;
		case 2: // Bottom
			x = Game.random.nextInt(Game.SCREEN_WIDTH);
			y = (int)(- hitBox.height*2);
			break;
		case 3: // Left
			x = (int)(- hitBox.width*2);
			y = Game.random.nextInt(Game.SCREEN_HEIGHT);
			break;
		}
		
		targetX = Game.random.nextInt(Game.SCREEN_WIDTH-(int)hitBox.width);
		targetY = Game.random.nextInt(Game.SCREEN_HEIGHT-(int)hitBox.height);
		
		signX = Math.signum(targetX - x);
		signY = Math.signum(targetY - y);
		
		angle = (float) Math.atan2(	targetY - y , targetX - x);
		
		float newVelocityX = (float)Math.cos(angle)* moveSpeed * Game.WORLD_TO_BOX ;
		float newVelocityY = (float)Math.sin(angle)* moveSpeed * Game.WORLD_TO_BOX ;
		body.setTransform(x * Game.WORLD_TO_BOX, y * Game.WORLD_TO_BOX, angle - (float)Math.PI/2);
		body.setLinearVelocity(newVelocityX, newVelocityY);
		
		Filter filter = body.getFixtureList().get(0).getFilterData();
		filter.maskBits = PhysicsFilter.MASK_PLAYER;
		body.getFixtureList().get(0).setFilterData(filter);
		
	}
	
}
