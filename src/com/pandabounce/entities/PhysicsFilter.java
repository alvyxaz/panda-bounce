package com.pandabounce.entities;

public class PhysicsFilter {
	public final static short CATEGORY_PLAYER = 0x0001;
	public final static short CATEGORY_HEDGEHOG = 0x0002;
	public final static short CATEGORY_WALL = 0x0004;
	public final static short CATEGORY_BEE = 0x0008;
	
	public final static short MASK_PLAYER = ~CATEGORY_PLAYER;
	public final static short MASK_HEDGEHOG = ~CATEGORY_HEDGEHOG;
	public final static short MASK_WALL = ~CATEGORY_WALL;
}
