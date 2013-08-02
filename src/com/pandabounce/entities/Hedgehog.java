package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pandabounce.MyGame;
import com.pandabounce.resources.Art;

public class Hedgehog {

	public Rectangle hitBox;

	// movement related vars
	private final int moveSpeed = 200; // Pixels per second
	private final int restlessness = 2;
	private final float moveAngle;
	private float dontRegenerateFor = 0;
	private boolean seen = false;

	public Body body;

	public Hedgehog(World world) {
		hitBox = new Rectangle(0, 0, Art.hedgehog.getRegionWidth(),
				Art.hedgehog.getRegionHeight());
		moveAngle = (float) (Math.random() * Math.PI);

		// Creating body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2((0 + hitBox.width / 2)
				* MyGame.WORLD_TO_BOX, (0 + hitBox.height / 2)
				* MyGame.WORLD_TO_BOX));
		bodyDef.angle = moveAngle;
		bodyDef.fixedRotation = true;

		// Creating body
		body = world.createBody(bodyDef);
		body.setLinearDamping(0);
		body.setAngularDamping(1f);

		// Creating a shape
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(hitBox.width / 2.4f * MyGame.WORLD_TO_BOX, hitBox.height
				/ 2.8f * MyGame.WORLD_TO_BOX);

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

	public void draw(SpriteBatch spriteBatch) {
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
			if (hitBox.x >= MyGame.SCREEN_WIDTH) {
				spriteBatch.draw(Art.exclamation, MyGame.SCREEN_WIDTH
						- Art.exclamation.getRegionWidth(), hitBox.y);
			}

			// top
			if (hitBox.y >= MyGame.SCREEN_HEIGHT) {
				spriteBatch.draw(
						Art.exclamation,
						hitBox.x,
						MyGame.SCREEN_HEIGHT
								- Art.exclamation.getRegionHeight());
			}
		} else {
			spriteBatch.draw(Art.hedgehog, body.getPosition().x
					* MyGame.BOX_TO_WORLD - hitBox.width / 2, // x
					body.getPosition().y * MyGame.BOX_TO_WORLD - hitBox.height
							/ 2, // y
					hitBox.width / 2, // OriginX
					hitBox.height / 2, // OriginY
					hitBox.width, // Width
					hitBox.height, // Height
					1f, // ScaleX
					1f, // ScaleY
					(float) Math.toDegrees(body.getAngle())); // Rotation

		}

	}

	public void update(float deltaTime) {
		hitBox.x = body.getPosition().x * MyGame.BOX_TO_WORLD - hitBox.width
				/ 2;
		hitBox.y = body.getPosition().y * MyGame.BOX_TO_WORLD - hitBox.height
				/ 2;
		dontRegenerateFor -= deltaTime;

		if (!seen) {
			if (MyGame.SCREEN_RECTANGLE.contains(hitBox)) {
				seen = true;

				/*
				 * ACHIEVEMENT: Hedgehog encounter
				 */
				Achievements.unlockAchievement(Achievements.hedgehogEncounter);
			}
		}

		if (dontRegenerateFor < 0) {
			// Checking if hedgehog is out of the screen
			if (!MyGame.SCREEN_RECTANGLE.contains(hitBox)) {
				regenerate();
				seen = false;
			}
		}
	}

	public void regenerate() {
		int x = 0, y = 0;
		float angle = 0;
		dontRegenerateFor = 3f; // Don't regenerate for 3 seconds
		int wall = MyGame.random.nextInt(4);

		// Checking where it will come from
		switch (wall) {
		case 0: // Top
			x = MyGame.random.nextInt(MyGame.SCREEN_WIDTH);
			y = MyGame.SCREEN_HEIGHT + 250;
			angle = (float) Math.atan2(MyGame.SCREEN_HEIGHT / 2 - y,
					MyGame.SCREEN_WIDTH / 2 - x + MyGame.SCREEN_WIDTH / 4
							- MyGame.random.nextInt(MyGame.SCREEN_WIDTH / 2));
			break;
		case 1: // Right
			x = MyGame.SCREEN_WIDTH + 250;
			y = MyGame.random.nextInt(MyGame.SCREEN_HEIGHT);
			angle = (float) Math.atan2(
					MyGame.SCREEN_HEIGHT / 2 - y + MyGame.SCREEN_HEIGHT / 4
							- MyGame.random.nextInt(MyGame.SCREEN_HEIGHT / 2),
					MyGame.SCREEN_WIDTH / 2 - x);
			break;
		case 2: // Bottom
			x = MyGame.random.nextInt(MyGame.SCREEN_WIDTH);
			y = (int) (-hitBox.height * 2) - 250;
			angle = (float) Math.atan2(MyGame.SCREEN_HEIGHT / 2 - y,
					MyGame.SCREEN_WIDTH / 2 - x + MyGame.SCREEN_WIDTH / 4
							- MyGame.random.nextInt(MyGame.SCREEN_WIDTH / 2));
			break;
		case 3: // Left
			x = (int) (-hitBox.width * 2) - 250;
			y = MyGame.random.nextInt(MyGame.SCREEN_HEIGHT);
			angle = (float) Math.atan2(
					MyGame.SCREEN_HEIGHT / 2 - y + MyGame.SCREEN_HEIGHT / 4
							- MyGame.random.nextInt(MyGame.SCREEN_HEIGHT / 2),
					MyGame.SCREEN_WIDTH / 2 - x);
			break;
		}

		float newVelocityX = (float) Math.cos(angle) * moveSpeed
				* MyGame.WORLD_TO_BOX;
		float newVelocityY = (float) Math.sin(angle) * moveSpeed
				* MyGame.WORLD_TO_BOX;
		body.setTransform(x * MyGame.WORLD_TO_BOX, y * MyGame.WORLD_TO_BOX,
				angle - (float) Math.PI / 2);
		body.setLinearVelocity(newVelocityX, newVelocityY);

		hitBox.x = body.getPosition().x * MyGame.BOX_TO_WORLD - hitBox.width
				/ 2;
		hitBox.y = body.getPosition().y * MyGame.BOX_TO_WORLD - hitBox.height
				/ 2;
	}
}
