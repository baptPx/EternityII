package com.bapt.eternity.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bapt.eternity.Eternity;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1400;
		config.height = 900;
		
//		config.vSyncEnabled = false;
//		config.foregroundFPS = 600;
		new LwjglApplication(new Eternity(), config);
	}
}

