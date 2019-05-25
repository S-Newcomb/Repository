package com.superglue.toweroffense.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.superglue.toweroffense.GDXRoot;
import com.superglue.toweroffense.util.Constants;

/**
 * The main class of the game.
 *
 * This class sets the window size and launches the game.  Aside from modifying
 * the window size, you should almost never need to modify this class.
 */
public class DesktopLauncher {
	/**
	 * Classic main method that all Java programmers know.
	 *
	 * This method simply exists to start a new LwjglApplication.  For desktop games,
	 * LibGDX is built on top of LWJGL (this is not the case for Android).
	 *
	 * @param arg Command line arguments
	 */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width  = Constants.RES_X;
		config.height = Constants.RES_Y;
		config.resizable = false;
		new LwjglApplication(new GDXRoot(), config);
	}
}
