package com.gikdew.swingcopters.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gikdew.swingcopters.SwingCopters;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Mad Copter";
		config.width = 272;
		config.height = 408;
		new LwjglApplication(new SwingCopters(new ActionResolverDesktop()),
				config);
	}
}
