package uk.co.jwlawson.hype;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main {
	private static final boolean PACK_TEXTURES = false;

	public static void main(String[] args) {

		if (PACK_TEXTURES) {
			Settings settings = new Settings();
			settings.maxWidth = 512;
			settings.maxHeight = 512;
			TexturePacker2.process(settings, "../images", "../firefall-android/assets",
					"firefall.pack");
		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Hype";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 680;

		new LwjglApplication(new MyGame(), cfg);
	}
}
