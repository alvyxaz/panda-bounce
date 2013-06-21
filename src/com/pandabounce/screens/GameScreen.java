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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pandabounce.Game;
import com.pandabounce.controls.Input;
import com.pandabounce.entities.GuiHealthBar;
import com.pandabounce.entities.GuiLiveNotification;
import com.pandabounce.entities.Panda;
import com.pandabounce.entities.Hedgehog;
import com.pandabounce.entities.Bamboo;
import com.pandabounce.entities.GuiScore;
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
	 * Transitions
	 */
	private int transitionState = 0;
	private static final int FADE_IN = 0;
	private static final int LIVE = 1;
	private static final int FADE_OUT = 2;
	private float opacity = 1f;
	
	/*-------------------------------------
	 * Data
	 */
	protected Bamboo [] stars;	// Star entities
	protected Hedgehog [] people;
	
	protected GuiLiveNotification [] notifications;
	
	/*-------------------------------------
	 * GUI elements
	 */
	protected GuiScore score;
	protected GuiHealthBar healthBar;
	
	private boolean movementRegistered = false;
	
	/*-------------------------------------
	 * BOX2D stuff
	 */
	World world;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	
	Body wallBodies[];
	
	public GameScreen(Game game) {
		super(game);
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		debugMatrix = new Matrix4(guiCam.combined);
		debugMatrix.scale(Game.BOX_TO_WORLD, Game.BOX_TO_WORLD, 1f);
		
		panda = new Panda(200, Game.SCREEN_HALF_WIDTH, world);
		stars = new Bamboo[3];
		people = new Hedgehog[3];
		
		
		// Initializing GUI
		score = new GuiScore();
		healthBar = new GuiHealthBar(panda);
		
		notifications = new GuiLiveNotification [10];
		for(int i = 0; i < notifications.length; i++){
			notifications[i] = new GuiLiveNotification();
		}

		createWalls(Game.WORLD_TO_BOX);
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
		}
		
		PolygonShape horizontalBox = new PolygonShape();
		horizontalBox.setAsBox(Game.SCREEN_HALF_WIDTH*wtb, 5*wtb);
		
		PolygonShape verticalBox = new PolygonShape();
		verticalBox.setAsBox(5*wtb, Game.SCREEN_HALF_HEIGHT*wtb);
		
		wallBodies[0].createFixture(verticalBox, 10*wtb);
		wallBodies[1].createFixture(verticalBox, 10*wtb);
		wallBodies[2].createFixture(horizontalBox, 10*wtb);
		wallBodies[3].createFixture(horizontalBox, 10*wtb);
		
		// Cleaning up
		horizontalBox.dispose();
		verticalBox.dispose();
		
	}
	
	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();
		drawLevel(deltaTime);
		switch(transitionState){
			case FADE_IN:
				spriteBatch.setColor(0, 0, 0, opacity);
				spriteBatch.draw(Art.px, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
				spriteBatch.setColor(Color.WHITE);
				break;
			case LIVE:
				break;
			case FADE_OUT:
				spriteBatch.setColor(0, 0, 0, opacity);
				spriteBatch.draw(Art.px, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
				spriteBatch.setColor(Color.WHITE);
				break;
		}
		spriteBatch.end();
		debugRenderer.render(world, debugMatrix);
	}
	
	public abstract void drawLevel(float deltaTime);
	public abstract void updateLevel(float deltaTime);

	@Override
	public void update(float deltaTime) {
		switch(transitionState){
			case FADE_IN:
				opacity -= deltaTime * 2;
				if(opacity < 0){
					opacity = 0;
					transitionState = LIVE;
				}
				break;
			case LIVE:
				updateLevel(deltaTime);
				world.step(deltaTime, 6, 2);
				
				// INPUT
				if(Gdx.input.isTouched()){
					if(Input.isTouching(panda.hitBox)){
						if(!panda.touched && !movementRegistered){
							panda.slide(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
							movementRegistered = true;
						} else {
							movementRegistered = false;
						}
						panda.touched = true;
					} else {
						if(panda.touched && !movementRegistered){
							panda.slide(Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
							movementRegistered = true;
						} else {
							movementRegistered = false;
						}
						panda.touched = false;
					}
				}
				
				// Updating hedgehogs
				for(int i = 0; i < people.length; i++){
					if(panda.hitBox.overlaps(people[i].hitBox)){
						panda.health -= 1;
						if(panda.health <= 0){
							panda.health = 100;
						}
					}
				}
				
				// Updating stars
				for(int i = 0; i < stars.length; i++){
					if(stars[i].hitBox.overlaps(panda.hitBox)){
						
						// Pushing a notification to the buffer, where there's open space
						for(int z = 0; z < notifications.length; z++){
							if(!notifications[z].display){
								notifications[z].generate("+10", 
										(int) (panda.hitBox.x + panda.hitBoxCenterX) , 
										(int) (panda.hitBox.y + panda.hitBox.height/2));
								break;
							}
						}
						int newX = Game.random.nextInt(Game.SCREEN_WIDTH-(int)stars[i].hitBox.width);
						int newY = Game.random.nextInt(Game.SCREEN_HEIGHT-(int)stars[i].hitBox.height);
						stars[i].regenerate(newX, newY);
						score.add(10);
					}
				}
				
				break;
		}
	}

	public void drawBackground(SpriteBatch spriteBatch){
		spriteBatch.draw(Art.background, 0, 0);
	}
	
	@Override
	public boolean beforeScreenSwitch(float deltaTime) {
		opacity += deltaTime * 2;
		if(opacity > 1){
			return true;
		}
		return false;
	}
	
	
	
}
