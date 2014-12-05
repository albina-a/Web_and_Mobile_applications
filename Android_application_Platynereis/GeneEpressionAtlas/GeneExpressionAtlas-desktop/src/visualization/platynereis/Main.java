package visualization.platynereis;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "GeneSeeker";
		//cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 680;
		cfg.depth = 1;
		new LwjglApplication(new ModelGame(), cfg);
		//new LwjglApplication(new TestTestDepth(), cfg);
	}
}
