package visualization.platynereis;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen implements Screen {
	// Gene atlas
	private String sheader = "Genes";
	//private String sheader = "Groups";
	// !!! Change application name when publishing	

	ModelScreen modelScreen=null;
	MainMenuScreen selfLink;
	PlatynereisGEAtlasRenderer renderer;
	
	Game game;
	
	//boolean buttonPressed;
	
	// ui
	private TextButton button;
	private Stage stage;
	private Table table;
	private CheckBox[] groupChecks;
	private Label statusLabel;
	private Image image;
	private Skin skin;
	private boolean finishedInterface = false;
		
	public MainMenuScreen (Game g) {
		//super(game);
		game = g;
		selfLink = this;
		
		initStage();
		
	}
	
	//  init stage with first image
	private void initStage() {
		stage = new Stage();
		
		// UI
		skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
		
		//table
		table = new Table(skin);		
		table.setFillParent(true);
		table.defaults().left();
		stage.addActor(table);
		
		// label		
		/*statusLabel = new Label("Loading the model", skin);
		// statusLabel.setText("Loading the model");
		table.row();
		table.add(statusLabel);*/
						
				
		// show first picture		
		/*TextureRegion texture = new TextureRegion(new Texture(Gdx.files.internal("data/front_mosaic_male.png"))); //front_mosaic_male.jpg
		Image image = new Image(texture);
		image.setScaling(Scaling.fillX);	
		table.row();
		table.addActor(image);//.width(texture.getRegionWidth()).height(texture.getRegionHeight());
		*/			
		
		
		renderer = new PlatynereisGEAtlasRenderer(); //modelPath, tranform, camPos);		
	}
	
	// functional interface	 
	private void initButtons() {
		// clear interface
		//table.removeActor(image);
		//table.removeActor(statusLabel);		
		table.clear();
		table.row();
				
		// Header
		table.add(sheader, "title-font", new Color(0f,0f,0f,1));
		
		
		// Here the model is already downloaded
		ArrayList<Material> materials = renderer.getMaterials();
		
        // Checkbox group
		int n=materials.size();
        groupChecks = new CheckBox[n];
        
        for(int i=0; i<n; i++) {
        	Material mat = materials.get(i);
        	groupChecks[i] = new CheckBox( "", skin );
        	groupChecks[i].setChecked(true);
        	table.row();
			table.add(groupChecks[i]);
			
        	if (mat.has(ColorAttribute.Diffuse)){
        		ColorAttribute cAttr = (ColorAttribute) mat.get(ColorAttribute.Diffuse);
        		table.add(mat.id, "title-font", cAttr.color);
        	}
			
        }

		
        // button		
 		button = new TextButton("Apply", skin);		
 		button.addListener(new ClickListener() {             
 		    @Override
 		    public void clicked(InputEvent event, float x, float y) {
 		    	// the first time apply is pressed
 		    	if (modelScreen==null){ 		    		
 		    		renderer.setChecks(groupChecks);
 		    		modelScreen = new ModelScreen(game, selfLink, renderer); 		    		
 		    	}
 		    	game.setScreen( modelScreen ); 		         		        
 		    };
 		});
 			
 		
 		table.row();
 		table.add(button);     		
     		
		finishedInterface = true;
	}
	
	
	@Override
	public void show () {	
		Gdx.input.setInputProcessor(stage);		 		
	}
	
	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0.7f, 0.7f, 1f, 1); // 0.9, 0.7, 1
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		
		if (!finishedInterface && renderer.assets.update()) {			
			initButtons();			
		}
		
	}
	
	@Override
	public void hide () {			
				
	}
	
    public void resize (int width, int height) {
    	//stage.setViewport(width, height, true);
    }

    public void pause () {
    }

    public void resume () {
    }
    @Override
    public void dispose () {
    	System.out.println("ModelScreen: dispose");
    	stage.dispose();
    	// dipose texture
    }


}
