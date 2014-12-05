package visualization.platynereis;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class ModelScreen implements Screen{
	protected PlatynereisGEAtlasRenderer renderer=null;
	protected Game game;
	protected MainMenuScreen mainMenu = null;
		
	public ModelScreen(Game g, MainMenuScreen m, PlatynereisGEAtlasRenderer r){		
		game = g;
		mainMenu = m;
		renderer = r;
    
		// Gdx.input.setCatchBackKey(true);    
	}
	
	
	@Override
	public void render(float delta) {
		renderer.render();	
		if(Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)){
			if (mainMenu==null) {				
				mainMenu=new MainMenuScreen(game);
			}
            game.setScreen(mainMenu);
		}
	}
	
	@Override
	public void show () {
		Gdx.input.setCatchBackKey(true);
		if (renderer!=null) {
			Gdx.input.setInputProcessor(renderer.cameraController);
		}
		renderer.setOpacity();
		
	}	
	@Override
	public void hide () {
    	Gdx.input.setCatchBackKey(false);
    
	}	
    
    @Override
    public void dispose () {    	
    	renderer.dispose();
    }
    @Override
    public void resize (int width, int height) {
    	renderer.resize(width, height);
    }
    @Override
    public void pause () {
    }
    @Override
    public void resume () {
    }

}
