package com.pandabounce;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopGame {
	public static void main (String[] args) {
        new LwjglApplication(new Game(), "Game", (int) (540*.8f), (int) (960*.8f), false);
	}
}
