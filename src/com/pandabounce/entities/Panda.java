package com.pandabounce.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;
import com.pandabounce.resources.Sounds;

public class Panda {
	public Rectangle hitBox;
	public float hitBoxCenterX;	// Relative Y center of the hitBox
	
	public float health; // Percentage
	public float maxHealth = 50;
	
	// Basic states
	public int state = 0;
	public int STATE_WAITING = 0;
	public int STATE_SLIDING = 1;
	
	// Slide related
	private float speedCoef = 1;
	private float xVelocity;
	private float yVelocity;
	private float slideAngle = 0;
	
	// Animations
	private float animationCycle;
	private float timePerFrame = 0.1f;
	private int frameCount = 1;
	private int currentFrame = 0;
	private int direction = 1; // -1 for inverted, 1 - regular
	private TextureRegion [] animation;
	private boolean isHorizontal = false;
	public boolean refreshAnimation = false;
	
	// Dust
	private Dust dust;
	private float dustTimer;
	private float dustDelay = 0.05f;
	
	// Effects
	public int effectType = 0;
	public float effectTimer;
	
	// Damage indication
	public boolean damaged = false;
	private float damageTimer;
	
	public boolean touched = false;
	
	private Body body;
	
	private int startX;
	private int startY;
	
	public Panda(int x, int y, World world) {
		health = maxHealth ;
		
		startX = x;
		startY = y;
		hitBox = new Rectangle(x, y, Art.pandaIdle[0].getRegionWidth(), Art.pandaIdle[0].getRegionHeight());
		hitBoxCenterX = hitBox.width/2;
		animation = Art.pandaIdle;

		dust = new Dust();
		
		// Creating body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2((x+  hitBoxCenterX)*Game.WORLD_TO_BOX , (y+ hitBox.height/2)*Game.WORLD_TO_BOX ));
		bodyDef.fixedRotation = true;

		// Creating body
		body = world.createBody(bodyDef);
		body.setLinearDamping(0);
		body.setAngularDamping(1f);
		
		// Creating a shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Art.pandaIdle[0].getRegionWidth()/3*Game.WORLD_TO_BOX, Art.pandaIdle[0].getRegionWidth()/3*Game.WORLD_TO_BOX);
		
		// Creating fixture 
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.restitution = 1f; // Maximum bounce ratio
		fixtureDef.filter.categoryBits = PhysicsFilter.CATEGORY_PLAYER;
		
		body.createFixture(fixtureDef);
		body.setUserData("panda");
		
		// Cleaning up
		shape.dispose();
		
	}
	
	public void regenerate(){
		body.setTransform(startX*Game.WORLD_TO_BOX, startY*Game.WORLD_TO_BOX, -(float)Math.PI/2);
		health = maxHealth;
		effectType = 0;
		
		// Stop being invincible
		damageTimer = 0;
		damaged = false;
		
		Filter filter = body.getFixtureList().get(0).getFilterData();
		filter.maskBits = ~0x0000;
		body.getFixtureList().get(0).setFilterData(filter);
		
		// Physics
		slideAngle = 0;
		startIdle();
		body.setLinearVelocity(0, 0);
		state = STATE_WAITING;
	}
	
	public void draw(SpriteBatch spriteBatch, float deltaTime){
		// Animation rotation fix
		float angleOffset = 0;
		if(!isHorizontal){
			angleOffset = (float)Math.PI/2;
		}
		
		dust.draw(spriteBatch, deltaTime);
		
		if(damageTimer > 0 && damageTimer < 0.2f){
			spriteBatch.setColor(Color.RED);
		}
		
		if((damageTimer * 14)%2 < 1 ){
			spriteBatch.draw(animation[currentFrame], 
					body.getPosition().x*Game.BOX_TO_WORLD - hitBox.width/2, 	// x
					body.getPosition().y * Game.BOX_TO_WORLD - hitBox.height/2, // y
					hitBox.width/2,	// OriginX
					hitBox.height/2,	// OriginY
					hitBox.width, 	// Width
					hitBox.height,	// Height
					1f,	// ScaleX
					1f,	// ScaleY
					(float)Math.toDegrees(slideAngle + angleOffset)); // Rotation
		}
		
		spriteBatch.setColor(Color.WHITE);
	}
	
	public void update(float deltaTime){
		// Effect
	    if (effectType != 0) {
			effectTimer -= deltaTime;
			if (effectTimer < 0) {
				effectType = 0;
			}
	    }
		

		/*----------------------------------------
		 * ANIMATION
		 */
		animationCycle += deltaTime;
		
		// Refreshing animation cycle
		if(animationCycle > timePerFrame*frameCount){
			animationCycle = 0;
		}
		
		currentFrame = (int)(( animationCycle /timePerFrame)) % frameCount;
		
		if(direction == -1){
			currentFrame = frameCount - currentFrame -1;
		}
		
		// Animation calculations related;
		float strength = (float)Math.sqrt(xVelocity * xVelocity*speedCoef + yVelocity * yVelocity*speedCoef);

		if(strength < 2 || Double.isNaN(strength)){
			startIdle();
		}
		
		if(strength < 4 && strength > 1 && speedCoef < 0.4f) {
			// Updating dust
			dustTimer -= deltaTime;
			if(dustTimer < 0){
				dust.startCloud(2, hitBox.x + hitBox.width/2, hitBox.y + hitBox.height/2);
				dustTimer = dustDelay;
			}
		}
		
		timePerFrame = Math.min(1/(strength*3), 0.07f);
		
		// Damage indication
		if(damaged){
			damageTimer += deltaTime;
			if(damageTimer > 2f){
				damageTimer = 0;
				damaged = false;
				
				Filter filter = body.getFixtureList().get(0).getFilterData();
				filter.maskBits = ~0x0000;
				body.getFixtureList().get(0).setFilterData(filter);
			}
		}
		
		/*----------------------------------------
		 * PHYSICS
		 */
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
	
	public void refreshAnimation(){
		// Animation calculations related;
		float strength = (float)Math.sqrt(xVelocity * xVelocity + yVelocity * yVelocity);
		slideAngle = (float)Math.atan2(yVelocity, xVelocity);
		
		if(strength < 2){
			startIdle();
		} else {
			
			if(Math.abs(slideAngle) < Math.PI*3/4 && Math.abs(slideAngle) > Math.PI/4){
				// Going up
				animation = Art.pandaVertical;
				frameCount = 8;
				isHorizontal = false;
				direction = 1;
			} else {
				animation = Art.pandaHorizontal;
				frameCount = 8;
				isHorizontal = true;
				if( Math.abs(slideAngle) < Math.PI/2){
					// Moving right
					direction = -1;
				} else {
					// Moving left
					slideAngle = slideAngle - (float)Math.PI;
					direction = 1;
				}
			}
		}
	}
	
	private void startIdle(){
		currentFrame = 0;
		animation = Art.pandaIdle;
		frameCount = 1;
	}
	
	public void slide(float dX, float dY){
		state = STATE_SLIDING; dY *= -1;
		xVelocity = dX*7*Game.WORLD_TO_BOX;
		yVelocity = dY*7*Game.WORLD_TO_BOX;
		body.setLinearVelocity(xVelocity, yVelocity);
		speedCoef = 1;
		
		refreshAnimation();
	}
	
	public void continueSlide(){
		state = STATE_SLIDING;
		speedCoef = 1;
	}
	
	public void damage(int damage){
		Sounds.playSound(Sounds.damage, false);
		this.damaged = true;
		this.damageTimer = 0;
		
		// Temporary invincible
		Filter filter = body.getFixtureList().get(0).getFilterData();
		filter.maskBits = PhysicsFilter.MASK_HEDGEHOG;
		body.getFixtureList().get(0).setFilterData(filter);
	
		health -= damage;
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
}
