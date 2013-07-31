package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.pandabounce.MyGame;
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
	public boolean pickedUp;
	
	private int state = 0;
	private static final int FADING_IN = 0;
	private static final int ACTIVE = 1;
	private static final int PARTICLES = 2;
	
	private int nrOfParticles = 10;
	private float [] particlesX;
	private float [] particlesY;
	private float [] particlesTimer;
	private float [] particlesSize;
	private float [] particlesAngle;
	private float [] particlesSpeed;
	
	public Star(int x, int y, World world){

		// Initializing particles
		particlesX = new float[nrOfParticles];
		particlesY = new float[nrOfParticles];
		particlesTimer = new float[nrOfParticles];
		particlesSize = new float[nrOfParticles];
		particlesAngle = new float[nrOfParticles];
		particlesSpeed = new float[nrOfParticles];
		
		hitBox = new Rectangle(x, y, Art.star.getRegionWidth(), Art.star.getRegionHeight());
		angle = MyGame.random.nextInt(360);
		levitationCoef = MyGame.random.nextFloat() * 3.14f;
		originalY = y;
		
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
		body.setUserData("star");
		
		// body.getFixtureList().get(0).setUserData(this);
		body.getFixtureList().get(0).setUserData(this);
		// Cleaning up
		shape.dispose();
	}
	
	private void generateParticles() {
		for(int i = 0; i < nrOfParticles; i++){
			particlesX[i] = hitBox.x + hitBox.width/2;
			particlesY[i] = hitBox.y + hitBox.height/2;
			particlesSize[i] = hitBox.width/4 + MyGame.random.nextFloat()*hitBox.width/2;
			particlesTimer[i] = particlesSize[i]/hitBox.width * 0.6f;
			particlesAngle[i] = (float) (Math.PI*2/nrOfParticles)  * i;
			particlesSpeed[i] = 80 * hitBox.width / particlesSize[i];
		}
	}
	
	public void onPickedUp(){
		state = PARTICLES;
		
		generateParticles();
		
		// Masking player
		Filter filter = body.getFixtureList().get(0).getFilterData();
		filter.maskBits = PhysicsFilter.MASK_PLAYER;
		body.getFixtureList().get(0).setFilterData(filter);
		
		pickedUp = false;
	}
	
	/*
	 * Regenerate bamboo in a different place
	 */
	public void regenerate() {
		int x = MyGame.random.nextInt((int) (MyGame.SCREEN_WIDTH - hitBox.width));
		int y = MyGame.random.nextInt((int) (MyGame.SCREEN_HEIGHT - hitBox.height));
		hitBox.x = x;
		hitBox.y = y;
		originalY = y;
		opacity = 0;
		
		// Unmasking player
		Filter filter = body.getFixtureList().get(0).getFilterData();
		filter.maskBits = ~PhysicsFilter.MASK_PLAYER;
		body.getFixtureList().get(0).setFilterData(filter);
		
		body.setTransform((x+  hitBox.width/2)*MyGame.WORLD_TO_BOX, (y+  hitBox.height/2)*MyGame.WORLD_TO_BOX, 0f);
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		
		
		switch(state){
		case FADING_IN:
			// Fade in effect
			if(opacity < 1){
				opacity += deltaTime;
				if(opacity > 1){
					opacity = 1;
					state = ACTIVE;
				}
			}
			
			spriteBatch.setColor(1, 1, 1, opacity);
			spriteBatch.draw(Art.star, hitBox.x, hitBox.y, hitBox.width/2, hitBox.height/2, hitBox.width, hitBox.height, 1.0f, 1.0f, angle);
			spriteBatch.setColor(Color.WHITE);
			break;
		case ACTIVE: 
			spriteBatch.draw(Art.star, hitBox.x, hitBox.y, hitBox.width/2, hitBox.height/2, hitBox.width, hitBox.height, 1.0f, 1.0f, angle);
			break;
		case PARTICLES:
			boolean done = true;
			for(int i = 0; i < nrOfParticles; i++){
				particlesTimer[i] -= deltaTime;
				
				if(particlesTimer[i] > 0){
					particlesX[i] += Math.cos(particlesAngle[i]) * deltaTime * particlesSpeed[i] * (1f-particlesTimer[i]);
					particlesY[i] += Math.sin(particlesAngle[i]) * deltaTime * particlesSpeed[i] * (1f-particlesTimer[i]);
					particlesSize[i] += deltaTime * 10;
					
					spriteBatch.draw(Art.starParticle, particlesX[i], particlesY[i], particlesSize[i], particlesSize[i]);
					done = false;
				}
			}
			
			if(done){
				regenerate();
				state = FADING_IN;
			}
			break;
		}
		
		// Rotating star
		angle += deltaTime * 40;
	
		// Levitating
		levitationCoef += deltaTime * 2;
		levitation = maxLevitation * (float) Math.sin(levitationCoef);
		hitBox.y = originalY + levitation;
	}
}
