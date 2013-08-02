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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pandabounce.MyGame;
import com.pandabounce.controls.Input;
import com.pandabounce.entities.Achievements;
import com.pandabounce.entities.Bee;
import com.pandabounce.entities.Dust;
import com.pandabounce.entities.GuiEffect;
import com.pandabounce.entities.GuiEndWindow;
import com.pandabounce.entities.GuiHealthBar;
import com.pandabounce.entities.GuiLargeNotifications;
import com.pandabounce.entities.GuiLiveNotification;
import com.pandabounce.entities.GuiScore;
import com.pandabounce.entities.GuiTimer;
import com.pandabounce.entities.Hedgehog;
import com.pandabounce.entities.Panda;
import com.pandabounce.entities.PhysicsFilter;
import com.pandabounce.entities.Star;
import com.pandabounce.entities.SurpriseBox;
import com.pandabounce.resources.Art;
import com.pandabounce.resources.Sounds;

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
	protected Star[] stars; // Star entities
	protected Hedgehog[] hedgehogs;
	protected Bee[] bees;
	protected SurpriseBox box;

	protected GuiLiveNotification[] notifications;
	protected GuiLargeNotifications largeNotifications;
	protected GuiEndWindow endWindow;

	/*-------------------------------------
	 * GUI elements
	 */
	protected GuiScore score;
	protected GuiHealthBar healthBar;
	protected Rectangle pauseButton;
	protected GuiTimer timer;
	protected GuiEffect effect;

	private final boolean movementRegistered = false;
	private final Rectangle buttonMainMenu;
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

	public GameScreen(MyGame game) {
		super(game);

		timer = new GuiTimer(MyGame.SCREEN_WIDTH - 100, 30);

		pauseButton = new Rectangle(MyGame.SCREEN_WIDTH - 105, 20, 90, 30);
		targetState = READY;

		endWindow = new GuiEndWindow();

		// Box2D
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		debugMatrix = new Matrix4(guiCam.combined);
		debugMatrix.scale(MyGame.BOX_TO_WORLD, MyGame.BOX_TO_WORLD, 1f);
		createWalls(MyGame.WORLD_TO_BOX);
		createContactListener();

		// Entities
		panda = new Panda(MyGame.SCREEN_HALF_WIDTH,
				MyGame.SCREEN_HALF_HEIGHT - 100, world);
		stars = new Star[3];

		// GUI
		healthBar = new GuiHealthBar(panda);
		largeNotifications = new GuiLargeNotifications();
		score = new GuiScore(largeNotifications);
		effect = new GuiEffect(panda);

		notifications = new GuiLiveNotification[10];
		for (int i = 0; i < notifications.length; i++) {
			notifications[i] = new GuiLiveNotification();
		}

		buttonMainMenu = new Rectangle(MyGame.SCREEN_HALF_WIDTH
				- Art.guiButtonSmall.getRegionWidth() / 2,
				MyGame.SCREEN_HALF_HEIGHT / 2,
				Art.guiButtonSmall.getRegionWidth(),
				Art.guiButtonSmall.getRegionHeight());

		// Graphic enhancements
		dust = new Dust();
	}

	public float screenShakeTimer = 0;
	public int screenShakeStrength = 1;

	public void shakeScreen(int strength) {
		screenShakeTimer = 0.3f;
		screenShakeStrength = Math.max(1, Math.min(strength / 2, 15));
	}

	private void createContactListener() {
		world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Body bA = contact.getFixtureA().getBody();
				Body bB = contact.getFixtureB().getBody();

				if (bA.getUserData().equals("panda")
						|| bB.getUserData().equals("panda")) {

					/*
					 * Collision with Wall
					 */
					if (bA.getUserData().equals("wall")
							|| bB.getUserData().equals("wall")) {

						// TODO: sound volume
						Sounds.playSound(Sounds.wallHit, false);

						panda.refreshAnimation = true;
						shakeScreen(score.multiplier);
						dust.startCloud(5, contact.getWorldManifold()
								.getPoints()[0].x * MyGame.BOX_TO_WORLD,
								contact.getWorldManifold().getPoints()[0].y
										* MyGame.BOX_TO_WORLD);
						return;
					}

					/*
					 * Collision with a Star
					 */
					if (bA.getUserData().equals("star")
							|| bB.getUserData().equals("star")) {

						int newX = MyGame.random.nextInt(MyGame.SCREEN_WIDTH
								- (int) stars[0].hitBox.width);
						int newY = MyGame.random.nextInt(MyGame.SCREEN_HEIGHT
								- (int) stars[0].hitBox.height);

						Sounds.playSound(Sounds.starPickedUp, false);

						// TODO: Regenerate a star in a given position
						if (bB.getUserData().equals("star")) {
							((Star) contact.getFixtureB().getUserData()).pickedUp = true;
						}

						if (bA.getUserData().equals("star")) {
							((Star) contact.getFixtureA().getUserData()).pickedUp = true;
						}

						score.onStarPickedUp();
						switch (panda.effectType) {
						case 6:
							score.add(2 * 10);
							break;
						default:
							score.add(10);
						}

						// Pushing a notification to the buffer, where there's
						// open space
						for (int z = 0; z < notifications.length; z++) {
							if (!notifications[z].display) {
								switch (panda.effectType) {
								case 6:
									notifications[z].generate(
											"+"
													+ (2 * 10 * score
															.getMultiplier()),
											(int) (panda.hitBox.x + panda.hitBoxCenterX),
											(int) (panda.hitBox.y + panda.hitBox.height / 2));
									break;
								default:
									notifications[z].generate(
											"+" + (10 * score.getMultiplier()),
											(int) (panda.hitBox.x + panda.hitBoxCenterX),
											(int) (panda.hitBox.y + panda.hitBox.height / 2));
								}

								break;
							}
						}
					}

					/*
					 * Collision with Hedgehog
					 */
					if (!panda.damaged
							&& (bA.getUserData().equals("hedgehog") || bB
									.getUserData().equals("hedgehog"))) {
						switch (panda.effectType) {
						case 6:
							panda.damage(2 * 5);
							panda.continueSlide();
							score.resetMultiplier();
							break;
						case 8:
							// doesn't take any damage
							break;
						default:
							panda.damage(5);
							panda.continueSlide();
							score.resetMultiplier();
						}
						return;
					}

					/*
					 * Collision with Bee
					 */
					if (!panda.damaged
							&& (bA.getUserData().equals("bee") || bB
									.getUserData().equals("bee"))) {
						switch (panda.effectType) {
						case 6:
							panda.damage(2 * 10);
							score.resetMultiplier();
							break;
						case 8:
							// doesn't take any damage
							break;
						default:
							panda.damage(10);
							score.resetMultiplier();
							if (panda.health <= 0) {
								/*
								 * ACHIEVEMENT: Allergic to bees
								 */
								Achievements
										.unlockAchievement(Achievements.allergicToBees);
							}
						}
						return;
					}

					if (box != null
							&& box.regenerationTimer < 0
							&& (bA.getUserData().equals("surprise_box") || bB
									.getUserData().equals("surprise_box"))) {
						Sounds.playSound(Sounds.box, true);

						SurpriseBox tempBox = null;
						if (bA.getUserData().equals("surprise_box")) {
							tempBox = ((SurpriseBox) contact.getFixtureA()
									.getUserData());
						}

						if (bB.getUserData().equals("surprise_box")) {
							tempBox = ((SurpriseBox) contact.getFixtureB()
									.getUserData());
						}

						panda.effectType = tempBox.type;
						// It's your lucky day
						if (tempBox.type == 7) {
							panda.effectTimer = 0;

							for (int z = 0; z < notifications.length; z++) {
								if (!notifications[z].display) {
									notifications[z]
											.generate(
													"+" + 10 + " HP",
													(int) (panda.hitBox.x + panda.hitBoxCenterX),
													(int) (panda.hitBox.y + panda.hitBox.height / 2));

									break;
								}
							}

							panda.health += 10;
						} else {
							panda.effectTimer = tempBox.effectTime;
						}

						tempBox.regenerate = true;

						/*
						 * ACHIEVEMENT: Risk taker
						 */
						Achievements.unlockAchievement(Achievements.riskTaker);

						/*
						 * ACHIEVEMENT: Double the fun
						 */
						if (panda.effectType == 6) {
							Achievements
									.unlockAchievement(Achievements.doubleTheFun);
						}

						return;
					}
				}

			}

			@Override
			public void endContact(Contact contact) {
				if (panda.health <= 0) {
					state = END;
					endWindow.show(score.score);
					transitionOpacity = 0.5f;
				}

				if (panda.refreshAnimation) {
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

	private void createWalls(float wtb) {
		BodyDef wallBodyDef[] = new BodyDef[4];
		wallBodies = new Body[4];
		for (int i = 0; i < wallBodyDef.length; i++) {
			wallBodyDef[i] = new BodyDef();
			wallBodyDef[i].type = BodyType.StaticBody;
		}

		wallBodyDef[0].position.set(new Vector2(0, MyGame.SCREEN_HALF_HEIGHT
				* wtb)); // Left wall
		wallBodyDef[1].position.set(new Vector2(MyGame.SCREEN_WIDTH * wtb,
				MyGame.SCREEN_HALF_HEIGHT * wtb)); // Right wall
		wallBodyDef[2].position.set(new Vector2(MyGame.SCREEN_HALF_WIDTH * wtb,
				MyGame.SCREEN_HEIGHT * wtb)); // Top wall
		wallBodyDef[3].position.set(new Vector2(MyGame.SCREEN_HALF_WIDTH * wtb,
				0)); // Bottom wall

		for (int i = 0; i < wallBodyDef.length; i++) {
			wallBodies[i] = world.createBody(wallBodyDef[i]);
			wallBodies[i].setUserData("wall");
		}

		PolygonShape horizontalBox = new PolygonShape();
		horizontalBox.setAsBox(MyGame.SCREEN_HALF_WIDTH * wtb, 5 * wtb);

		PolygonShape verticalBox = new PolygonShape();
		verticalBox.setAsBox(5 * wtb, MyGame.SCREEN_HALF_HEIGHT * wtb);

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
		// spriteBatch.draw(Art.px, pauseButton.x, pauseButton.y,
		// pauseButton.width, pauseButton.height);
		switch (state) {
		case FADE_IN:
			spriteBatch.setColor(0, 0, 0, transitionOpacity);
			spriteBatch.draw(Art.px, 0, 0, MyGame.SCREEN_WIDTH,
					MyGame.SCREEN_HEIGHT);
			spriteBatch.setColor(Color.WHITE);
			break;
		case READY:
			spriteBatch.setColor(0, 0, 0, transitionOpacity);
			spriteBatch.draw(Art.px, 0, 0, MyGame.SCREEN_WIDTH,
					MyGame.SCREEN_HEIGHT);
			spriteBatch.setColor(Color.WHITE);
			Art.fontKomika24Gold.draw(spriteBatch, "Ready?",
					MyGame.SCREEN_HALF_WIDTH - 50, MyGame.SCREEN_HALF_HEIGHT);
			break;
		case PAUSE:
			spriteBatch.setColor(0, 0, 0, transitionOpacity);
			spriteBatch.draw(Art.px, 0, 0, MyGame.SCREEN_WIDTH,
					MyGame.SCREEN_HEIGHT);
			spriteBatch.setColor(Color.WHITE);
			Art.fontKomika24Gold.draw(spriteBatch, "Paused",
					MyGame.SCREEN_HALF_WIDTH - 46, MyGame.SCREEN_HALF_HEIGHT);
			Art.fontKomika24Gold.draw(spriteBatch, "Touch to continue",
					MyGame.SCREEN_HALF_WIDTH - 135,
					MyGame.SCREEN_HALF_HEIGHT - 50);

			spriteBatch.draw(Art.guiButtonSmall, buttonMainMenu.x,
					buttonMainMenu.y);
			Art.fontKomika24Gold.draw(spriteBatch, "Main Menu",
					buttonMainMenu.x, buttonMainMenu.y + buttonMainMenu.height
							- 16);
			break;
		case PLAYING:
			break;
		case END:
			spriteBatch.setColor(0, 0, 0, transitionOpacity);
			spriteBatch.draw(Art.px, 0, 0, MyGame.SCREEN_WIDTH,
					MyGame.SCREEN_HEIGHT);
			spriteBatch.setColor(Color.WHITE);
			endWindow.draw(spriteBatch, deltaTime);
			break;
		case FADE_OUT:
			spriteBatch.setColor(0, 0, 0, transitionOpacity);
			spriteBatch.draw(Art.px, 0, 0, MyGame.SCREEN_WIDTH,
					MyGame.SCREEN_HEIGHT);
			spriteBatch.setColor(Color.WHITE);
			break;
		}

		spriteBatch.end();
		// debugRenderer.render(world, debugMatrix);
	}

	public abstract void drawLevel(float deltaTime);

	public abstract void updateLevel(float deltaTime);

	@Override
	public void update(float deltaTime) {
		float originalDeltaTime = deltaTime;
		switch (state) {
		case FADE_IN:
			if (!MyGame.mute && !Sounds.music.isPlaying()) {
				Sounds.music.play();
			}
			transitionOpacity -= deltaTime * 2;
			if (targetState == READY && transitionOpacity < 0.5f) {
				transitionOpacity = 0.5f;
				state = READY;
				targetState = -1;
			} else if (targetState == PLAYING && transitionOpacity < 0) {
				transitionOpacity = 0;
				state = PLAYING;
				targetState = -1;
			}
			break;
		case READY:
			if (Gdx.input.justTouched()) {
				targetState = PLAYING;
				state = FADE_IN;
			}
			break;
		case PAUSE:
			if (Input.isReleasing(buttonMainMenu)) {
				this.switchScreenTo(game.screenTitle);
				state = FADE_OUT;
				game.ads.hideAds();
			} else if (Input.isReleasing()) {
				targetState = PLAYING;
				state = FADE_IN;
				game.ads.hideAds();
			}
			break;
		case END:
			if (endWindow.isWaitingForInput()) {
				if (Input.isTouching(endWindow.playAgain)) {
					state = FADE_OUT;
					targetState = READY;
					MyGame.ads.hideAds();
				} else if (Input.isReleasing(endWindow.submitScore)) {
					MyGame.google.submitScore(score.score);
				}
			}
			break;
		case FADE_OUT:
			if (targetState != -1) {
				transitionOpacity += deltaTime * 2;
				if (transitionOpacity > 1) {
					transitionOpacity = 1;
					if (targetState == READY) {
						restartGame();
					}
					state = FADE_IN;
				}
			}
			break;
		case PLAYING:

			switch (panda.effectType) {
			case 2:
				updateLevel(deltaTime / 2);
				world.step(deltaTime / 2, 6, 2);
				break;
			case 4:
				updateLevel(deltaTime * 2);
				world.step(deltaTime * 2, 6, 2);
				break;
			default:
				updateLevel(deltaTime);
				world.step(deltaTime, 6, 2);
			}

			if (Input.isReleasing(pauseButton)) {
				state = PAUSE;
				transitionOpacity = 0.5f;
				game.ads.showAds();
			}

			if (timer.enabled) {
				timer.update(originalDeltaTime);
				if (timer.time < 0 && !timer.ascending) {
					state = END;
					endWindow.show(score.score);
					transitionOpacity = 0.5f;
				}
			}

			panda.update(deltaTime);

			// Bees
			for (int i = 0; i < bees.length; i++) {
				if (bees[i] != null) {
					bees[i].update(deltaTime);
				}
			}

			// Hedgehogs
			for (int i = 0; i < hedgehogs.length; i++) {
				if (hedgehogs[i] != null) {
					hedgehogs[i].update(deltaTime);
				}
			}

			// Checking input
			if (Input.isReleasing()) {
				switch (panda.effectType) {
				case 1:
					panda.slide(-Input.touch[0].highestDx,
							-Input.touch[0].highestDy);
					break;
				case 3:
					panda.slide(Input.touch[0].highestDx / 2,
							Input.touch[0].highestDy / 2);
					break;
				case 5:
					panda.slide(Input.touch[0].highestDx * 2,
							Input.touch[0].highestDy * 2);
					break;
				default:
					panda.slide(Input.touch[0].highestDx,
							Input.touch[0].highestDy);
				}

				if (!MyGame.mute) {
					Sounds.whoosh.stop();
					Sounds.whoosh.play();
				}

				score.starsInSingleSlide = 0;
			}

			// Updating screen shake
			if (screenShakeTimer > 0) {
				screenShakeTimer -= deltaTime;
				guiCam.position.x = MyGame.SCREEN_HALF_WIDTH
						- screenShakeStrength / 2
						+ MyGame.random.nextInt(screenShakeStrength);
				guiCam.position.y = MyGame.SCREEN_HALF_HEIGHT
						- screenShakeStrength / 2
						+ MyGame.random.nextInt(screenShakeStrength);

				if (screenShakeTimer < 0) {
					guiCam.position.x = MyGame.SCREEN_HALF_WIDTH;
					guiCam.position.y = MyGame.SCREEN_HALF_HEIGHT;
				}

				guiCam.update();
			}

			break;
		}
	}

	public void restartGame() {
		state = FADE_OUT;
		targetState = READY;

		panda.regenerate();
		score.reset();
		timer.reset();

		// Bees
		for (int i = 0; i < bees.length; i++) {
			if (bees[i] != null) {
				world.destroyBody(bees[i].body);
				bees[i] = null;
			}
		}

		// Hedgehogs
		for (int i = 0; i < hedgehogs.length; i++) {
			if (hedgehogs[i] != null) {
				world.destroyBody(hedgehogs[i].body);
				hedgehogs[i] = null;
			}
		}

		if (box != null)
			box.regenerationTimer = 10f;

		if (MyGame.preferences.getInteger("games played") == 1) {
			/*
			 * ACHIEVEMENT: I've been expecting you
			 */
			Achievements.unlockAchievement(Achievements.iveBeenExpectingYou);
		}

	}

	public void drawBackground(SpriteBatch spriteBatch) {
		spriteBatch.disableBlending();
		spriteBatch.draw(Art.background, 0, 0);
		spriteBatch.enableBlending();
	}

	@Override
	public boolean beforeScreenSwitch(float deltaTime) {
		transitionOpacity += deltaTime * 2;
		if (transitionOpacity > 1) {
			return true;
		}
		return false;
	}

}
