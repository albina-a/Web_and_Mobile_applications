package visualization.platynereis;

import com.badlogic.gdx.Game;

public class ModelGame extends Game {
	@Override
	public void create () {
		setScreen(new MainMenuScreen(this));
	}

}
