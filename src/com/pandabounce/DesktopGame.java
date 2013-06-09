package com.pandabounce;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopGame {
	public static void main (String[] args) {
        new LwjglApplication(new Game(), "Game", 540, 960, false);
	}
}
