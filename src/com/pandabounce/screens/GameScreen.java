package com.pandabounce.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.entities.Bee;
import com.pandabounce.entities.Dust;
import com.pandabounce.entities.GuiEndWindow;
import com.pandabounce.entities.GuiHealthBar;
import com.pandabounce.entities.GuiLargeNotifications;
import com.pandabounce.entities.GuiLiveNotification;
import com.pandabounce.entities.GuiTimer;
import com.pandabounce.entities.Panda;
import com.pandabounce.entities.Hedgehog;
import com.pandabounce.entities.PhysicsFilter;
import com.pandabounce.entities.Star;
import com.pandabounce.entities.GuiScore;
import com.pandabounce.entities.SurpriseBox;
import com.pandabounce.resources.Art;

/*
 * TODO: Should work as a base for level objects (screens).
 * Everything that has to be repeated  in every level should be
 * in this object.
 */
public abstract class GameScreen extends BaseScreen {

	protected Panda panda;
	protected float targetAngle;
	
	/*-------------------------------------
	 * States
	 */
	private int state = 0;
	private int targetState = -1;
	private static final int FADE_IN = 0;
	private static final int READY = 3;
	private static final int PLAYING = 1;
	private static final int PAUSE = 4;
	private static final int END = 5;
	private static final int FADE_OUT = 2;
	private float transitionOpacity = 1f;
	
	/*-------------------------------------
	 * Data
	 */
	protected Star [] stars;	// Star entities
	protected Hedgehog [] hedgehogs;
	protected Bee [] bees;
	protected SurpriseBox box;
	
	protected GuiLiveNotification [] notifications;
	protected GuiLargeNotifications largeNotifications;
	protected GuiEndWindow endWindow;
	
	/*-------------------------------------
	 * GUI elements
	 */
	protected GuiScore score;
	protected GuiHealthBar healthBar;
	protected Rectangle pauseButton;
	protected GuiTimer timer;
	
	private boolean movementRegistered = false;
	
	/*
	 * Graphic enhancements
	 */
	protected Dust dust;
	
	/*-------------------------------------
	 * BOX2D stuff
	 */
	World world;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	
	Body wallBodies[];
	
	public GameScreen(Game game) {
		super(game);
		
		timer = new GuiTimer(Game.SCREEN_WIDTH-100, 30);
		
		pauseButton = new Rectangle(10, 10, 20, 20);
		targetState = READY;
		
		endWindow = new GuiEndWindow();
		
		// Box2D
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		debugMatrix = new Matrix4(guiCam.combined);
		debugMatrix.scale(Game.BOX_TO_WORLD, Game.BOX_TO_WORLD, 1f);
		createWalls(Game.WORLD_TO_BOX);
		createContactListener();
		
		// Entities
		panda = new Panda(Game.SCREEN_HALF_WIDTH, Game.SCREEN_HALF_WIDTH, world);
		stars = new Star[3];

		// GUI
		healthBar = new GuiHealthBar(panda);
		largeNotifications = new GuiLargeNotifications();
		score = new GuiScore(largeNotifications);
		
		notifications = new GuiLiveNotification [10];
		for(int i = 0; i < notifications.length; i++){
			notifications[i] = new GuiLiveNotification();
		}

		// Graphic enhancements
		dust = new Dust();
	}
	
	public float screenShakeTimer = 0;
	public int screenShakeStrength = 1;
	
	public void shakeScreen(int strength){
		screenShakeTimer = 0.3f;
		screenShakeStrength = Math.max(1, Math.min(strength/4, 15));
	}
	
	private void createContactListener(){
		world.setContactListener(new ContactListener(){

			@Override
			public void beginContact(Contact contact) {
				Body bA = contact.getFixtureA().getBody();
				Body bB = contact.getFixtureB().getBody();

				if(bA.getUserData().equals("panda") || bB.getUserData().equals("panda") ){
					
					/*
					 * Collision with Hedgehog
					 */
					if(!panda.damaged && (bA.getUserData().equals("bee") || bB.getUserData().equals("bee"))){
						panda.damage(10);

					}
					
					/*
					 * Collision with Hedgehog
					 */
					if(!panda.damaged && (bA.getUserData().equals("hedgehog") || bB.getUserData().equals("hedgehog"))){
						panda.damage(5);
						panda.continueSlide();
					}
					
					/*
					 * Collision with Wall
					 */
					if(bA.getUserData().equals("wall") || bB.getUserData().equals("wall")){
						panda.refreshAnimation = true;
						shakeScreen(score.multiplier);
						dust.startCloud(5, 
								contact.getWorldManifold().getPoints()[0].x * Game.BOX_TO_WORLD, 
								contact.getWorldManifold().getPoints()[0].y * Game.BOX_TO_WORLD);
					}
					
					/*
					 * Collision with a Star
					 */
					if(bA.getUserData().equals("star") || bB.getUserData().equals("star")){

						int newX = Game.random.nextInt(Game.SCREEN_WIDTH-(int)stars[0].hitBox.width);
						int newY = Game.random.nextInt(Game.SCREEN_HEIGHT-(int)stars[0].hitBox.height);
						
						// TODO: Regenerate a star in a given position
						if(bB.getUserData().equals("star")){
							((Star) contact.getFixtureB().getUserData()).regenerate = true;
						}
						
						if(bA.getUserData().equals("star")){
							((Star) contact.getFixtureA().getUserData()).regenerate = true;
						}
						
						score.onStarPickedUp();
						score.add(10);
						
						// Pushing a notification to the buffer, where there's open space
						for(int z = 0; z < notifications.length; z++){
							if(!notifications[z].display){
								notifications[z].generate("+" + (10 * score.getMultiplier()), 
										(int) (panda.hitBox.x + panda.hitBoxCenterX) , 
										(int) (panda.hitBox.y + panda.hitBox.height/2));
								break;
							}
						}
					}
					
					if(box != null && box.regenerationTimer < 0 && (bA.getUserData().equals("surprise_box") || bB.getUserData().equals("surprise_box"))){						
						if(bA.getUserData().equals("surprise_box")){
							panda.effectType = ((SurpriseBox) contact.getFixtureA().getUserData()).type;
							panda.effectTimer = ((SurpriseBox) contact.getFixtureA().getUserData()).effectTime;
							((SurpriseBox) contact.getFixtureA().getUserData()).regenerate = true;
						}
						
						if(bB.getUserData().equals("surprise_box")){
							panda.effectType = ((SurpriseBox) contact.getFixtureB().getUserData()).type;
							panda.effectTimer = ((SurpriseBox) contact.getFixtureB().getUserData()).effectTime;
							((SurpriseBox) contact.getFixtureB().getUserData()).regenerate = true;
						}
						
						switch (panda.effectType) {
						    case 1:
								largeNotifications.add(Art.textConfused);
						    	break;
						    case 2:
						    	largeNotifications.add(Art.textSlowerWorld);
						    	 break;
						    case 3:
						    	largeNotifications.add(Art.textLazy);
						    	break;
						    case 4:
						    	largeNotifications.add(Art.textFasterWorld);
						    	break;
						    case 5:
						    	largeNotifications.add(Art.textFastPaced);
						    	break;
					    }
					}
				}				
				
			}

			@Override
			public void endContact(Contact contact) {
				if(panda.health <= 0){
					state = END;
					transitionOpacity = 0.5f;
				}
				
				if(panda.refreshAnimation){
					panda.refreshAnimation = false;
					panda.refreshAnimation();
				}
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				
			}
			
		});
	}
	
	private void createWalls(float wtb){
		BodyDef wallBodyDef [] = new BodyDef [4];
		wallBodies = new Body[4];
		for(int i = 0; i < wallBodyDef.length; i++){
			wallBodyDef[i] = new BodyDef();
			wallBodyDef[i].type = BodyType.StaticBody;
		}
		
		wallBodyDef[0].position.set(new Vector2(0, Game.SCREEN_HALF_HEIGHT*wtb)); // Left wall
		wallBodyDef[1].position.set(new Vector2(Game.SCREEN_WIDTH*wtb, Game.SCREEN_HALF_HEIGHT*wtb)); // Right wall
		wallBodyDef[2].position.set(new Vector2(Game.SCREEN_HALF_WIDTH*wtb, Game.SCREEN_HEIGHT*wtb)); // Top wall
		wallBodyDef[3].position.set(new Vector2(Game.SCREEN_HALF_WIDTH*wtb, 0));  // Bottom wall
		
		for(int i = 0; i < wallBodyDef.length; i++){
			wallBodies[i] = world.createBody(wallBodyDef[i]);
			wallBodies[i].setUserData("wall");
		}
		
		PolygonShape horizontalBox = new PolygonShape();
		horizontalBox.setAsBox(Game.SCREEN_HALF_WIDTH*wtb, 5*wtb);
		
		PolygonShape verticalBox = new PolygonShape();
		verticalBox.setAsBox(5*wtb, Game.SCREEN_HALF_HEIGHT*wtb);
		
		wallBodies[0].createFixture(verticalBox, 0);
		wallBodies[1].createFixture(verticalBox, 0);
		wallBodies[2].createFixture(horizontalBox, 0);
		wallBodies[3].createFixture(horizontalBox, 0);
		
		Filter filter = wallBodies[0].getFixtureList().get(0).getFilterData();
		filter.categoryBits = PhysicsFilter.CATEGORY_WALL;
		filter.maskBits = PhysicsFilter.MASK_HEDGEHOG;
		
		wallBodies[0].getFixtureList().get(0).setFilterData(filter);
		wallBodies[1].getFixtureList().get(0).setFilterData(filter);
		wallBodies[2].getFixtureList().get(0).setFilterData(filter);
		wallBodies[3].getFixtureList().get(0).setFilterData(filter);
		
		// Cleaning up
		horizontalBox.dispose();
		verticalBox.dispose();
		
	}
	
	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(guiCam.combined);
		drawLevel(deltaTime);
		//spriteBatch.draw(Art.px, pauseButton.x, pauseButton.y, pauseButton.width, pauseButton.height);
		switch(state){
			case FADE_IN:
				spriteBatch.setColor(0, 0, 0, transitionOpacity);
				spriteBatch.draw(Art.px, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
				spriteBatch.setColor(Color.WHITE);
				break;
			case READY:
				spriteBatch.setColor(0, 0, 0, transitionOpacity);
				spriteBatch.draw(Art.px, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
				spriteBatch.setColor(Color.WHITE);
				Art.fontKomika24Gold.draw(spriteBatch, "Ready?",Game.SCREEN_HALF_WIDTH - 50, Game.SCREEN_HALF_HEIGHT);
				break;
			case PAUSE:
				spriteBatch.setColor(0, 0, 0, transitionOpacity);
				spriteBatch.draw(Art.px, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
				spriteBatch.setColor(Color.WHITE);
				Art.fontDefault.draw(spriteBatch, "Paused",	Game.SCREEN_HALF_WIDTH, Game.SCREEN_HALF_HEIGHT);
				Art.fontDefault.draw(spriteBatch, "Touch to continue",	Game.SCREEN_HALF_WIDTH-30, Game.SCREEN_HALF_HEIGHT-50);
				break;
			case PLAYING:
				break;
			case END:
				spriteBatch.setColor(0, 0, 0, transitionOpacity);
				spriteBatch.draw(Art.px, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
				spriteBatch.setColor(Color.WHITE);
				endWindow.draw(spriteBatch, deltaTime);
				break;
			case FADE_OUT:
				spriteBatch.setColor(0, 0, 0, transitionOpacity);
				spriteBatch.draw(Art.px, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
				spriteBatch.setColor(Color.WHITE);
				break;
		}
		
		spriteBatch.end();
//		debugRenderer.render(world, debugMatrix);
	}
	
	public abstract void drawLevel(float deltaTime);
	public abstract void updateLevel(float deltaTime);
	
	@Override
	public void update(float deltaTime) {
		float originalDeltaTime = deltaTime;
		switch(state){
			case FADE_IN:
				transitionOpacity -= deltaTime * 2;
				if(targetState == READY && transitionOpacity < 0.5f){
					transitionOpacity = 0.5f;
					state = READY;
					targetState = -1;
				} else if (targetState == PLAYING && transitionOpacity < 0){
					transitionOpacity = 0;
					state = PLAYING;
					targetState = -1;
				}
				break;
			case READY:
				if(Gdx.input.justTouched()){
					targetState = PLAYING;
					state = FADE_IN;
				}
				break;
			case PAUSE:
				if(Gdx.input.justTouched()){
					targetState = PLAYING;
					state = FADE_IN;
				}
				break;
			case END:
				if(Input.isTouching(endWindow.playAgain)){
					state = FADE_OUT;
					targetState = READY;
				} else if(Input.isTouching(endWindow.submitScore)){
					state = FADE_OUT;
					this.screenToSwitchTo = new RatingScreen(this.game, score.score);
				}
				break;
			case FADE_OUT:
				if(targetState != -1){
					transitionOpacity += deltaTime * 2;
					if(transitionOpacity > 1){
						transitionOpacity = 1;
						if(targetState == READY){
							restartGame();
						}
						state = FADE_IN;
					}
				}
				break;
			case PLAYING:
				
				switch (panda.effectType) {
					case 2:
						updateLevel(deltaTime/2);
						world.step(deltaTime/2, 6, 2);
						break;
					case 4:
						updateLevel(deltaTime*2);
						world.step(deltaTime*2, 6, 2);
						break;
					default:
						updateLevel(deltaTime);
						world.step(deltaTime, 6, 2);
				}

				if(Input.isTouching(pauseButton)){
					state = PAUSE;
					transitionOpacity = 0.5f;
				}
				
				if(timer.enabled){
					timer.update(originalDeltaTime);
					if (timer.time < 0 && !timer.ascending){
						state = END;
						transitionOpacity = 0.5f;
					}
				}
				
				panda.update(deltaTime);

				// Bees
				for(int i = 0; i < bees.length; i++)
					bees[i].update(deltaTime);
				
				// Hedgehogs
				for(int i = 0; i < hedgehogs.length; i++)
					hedgehogs[i].update(deltaTime);
				
				
				// Checking input
				if(Input.isReleasing()){
				    switch (panda.effectType) {
					    case 1:
							panda.slide(-Input.touch[0].highestDx, -Input.touch[0].highestDy);
					    	break;
					    case 3:
							panda.slide(Input.touch[0].highestDx/2, Input.touch[0].highestDy/2);
							break;
					    case 5:
							panda.slide(Input.touch[0].highestDx*2, Input.touch[0].highestDy*2);
					    	break;
				    	default:
							panda.slide(Input.touch[0].highestDx, Input.touch[0].highestDy);
				    }
				    
					score.starsInSingleSlide = 0;
				}
				
				// Updating screen shake
				if(screenShakeTimer > 0){
					screenShakeTimer -= deltaTime;
					guiCam.position.x = Game.SCREEN_HALF_WIDTH - screenShakeStrength/2 + Game.random.nextInt(screenShakeStrength);
					guiCam.position.y = Game.SCREEN_HALF_HEIGHT - screenShakeStrength/2 + Game.random.nextInt(screenShakeStrength);
					
					if(screenShakeTimer < 0){
						guiCam.position.x = Game.SCREEN_HALF_WIDTH;
						guiCam.position.y = Game.SCREEN_HALF_HEIGHT;
					}
					
					guiCam.update();
				}
				
				break;
		}
	}
	
	public void restartGame(){
		panda.regenerate();
		score.reset();
		timer.reset();
		
		for(int i = 0; i < hedgehogs.length; i++ ){
			hedgehogs[i].regenerate();
		}
		
		for(int i = 0; i < bees.length; i++){
			bees[i].regenerate();
		}
		
		if(box != null)
		box.regenerationTimer = 10f;
		
	}
	
	public void drawBackground(SpriteBatch spriteBatch){
		spriteBatch.disableBlending();
		spriteBatch.draw(Art.background, 0, 0);
		spriteBatch.enableBlending();
	}
	
	@Override
	public boolean beforeScreenSwitch(float deltaTime) {
		transitionOpacity += deltaTime * 2;
		if(transitionOpacity > 1){
			return true;
		}
		return false;
	}
	
	
	
}
