package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Surface {

	public Rectangle hitBox;
	
	public abstract void draw(SpriteBatch spriteBatch, float deltaTime);
}
