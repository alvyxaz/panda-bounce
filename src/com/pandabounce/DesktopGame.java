package com.pandabounce;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopGame {
	public static void main (String[] args) {
        new LwjglApplication(new MyGame(new GoogleDesktop()), "Game", (int) 480, 800, false);
	}
}
