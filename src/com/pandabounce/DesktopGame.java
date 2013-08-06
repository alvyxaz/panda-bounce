package com.pandabounce;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopGame {
	public static void main (String[] args) {
		ActionResolver actionResolver = new ActionResolverDesktop();
		MyGame game = new MyGame(new GoogleDesktop(), actionResolver);
		game.ads = new AdsDesktop();
		((GoogleDesktop)game.google).game = game;
        new LwjglApplication(game, "Game", (int) 540, 960, false);
	}
}
