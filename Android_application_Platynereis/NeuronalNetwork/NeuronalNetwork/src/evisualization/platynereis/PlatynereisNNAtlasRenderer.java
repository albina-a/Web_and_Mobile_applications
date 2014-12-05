package evisualization.platynereis;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;

public class PlatynereisNNAtlasRenderer {
	// Gene atlas
	/*private String modelPath = "data/GeneExpressionAtlas72.g3db";
	//private String modelPath = "data/atlas_lm9_for_export_pkd.g3db";
	private float[] tranform = new float[]{-2.2f, 2.4f, 2.0f};
	private Vector3 camposition = new Vector3(0f, 0f, 5.5f);	
	private float near = 0.1f, far = 20f;*/
			
	// EM model
	private String modelPath = "data/NeuronalNetworkBlender.g3db";	
	private float[] tranform = new float[]{-2.0f, -0.5f, 3.0f};
	private Vector3 camposition = new Vector3(0f, 0f, 20f);
	private float near = 1f, far = 50f;
	
	CheckBox[] groupChecks; 
	ArrayList<Material> materials;	
	// String[] prefixes;
	
	Environment envir;
	//Vector3 camposition;	
	PerspectiveCamera camera; 
	CameraInputController cameraController;
	
	ModelBatch modelBatch;
	Model model;
	ModelInstance instance;
	public AssetManager assets;
	boolean loading;
	
	public int lightZ = 5;
		

	public PlatynereisNNAtlasRenderer(){ //String m, float[] t, Vector3 camposition) { //String[] p,CheckBox[] gChecks

		modelBatch = new ModelBatch();
		envir = new Environment();
		
		//light
        envir.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		envir.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, 0f, 0f, -1f)); // key
        envir.add(new DirectionalLight().set(0.5f, 0.5f, 0.5f, 0f, 0f, 1f)); // back
            
        // camera
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());		
		// camposition = new Vector3(0f, 0f, 5.5f);
		camera.position.set(camposition); 
		camera.lookAt(0,0,0);
		camera.near = near; //0.1f;
		camera.far = far; //20f;
		camera.update();
				
		cameraController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(cameraController); 
        
        
        // load the model
		System.out.println("Start loading");
		assets = new AssetManager();
        assets.load(modelPath, Model.class); // starts running in parallel atlas_lm8_forfbx.g3db "data/model_lm72.g3db"
        
        // if you want to start loading at once
        assets.update();
        loading = true;
        assets.finishLoading();
        
        doneLoading();
	}
	
	private void doneLoading() {
		System.out.println("Done loading");
		
		model = assets.get(modelPath, Model.class);		 
	    instance = new ModelInstance(model);	    
	    instance.transform.setToTranslation(tranform[0],tranform[1],tranform[2]);  //-2.2f, 2.7f, 2.0f / -2.2f, 2.4f, 2.0f
	    	    
	    // instance.transform.scale(10, 10, 10); 
	    instance.transform.rotate(1, 0, 0, 180);
	    //loading = false;
	    
	}

	public void render() {
		cameraController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());	        
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f,1); //0.8 - paper
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
       
        
        // set the position of the light according to the camera
        DirectionalLight dLight = envir.directionalLights.get(0);
        DirectionalLight bLight = envir.directionalLights.get(1);
        if (!camera.position.equals(camposition)) {        	// remember prev camera pos
        	Vector3 pos = camera.position;
        	dLight.set(dLight.color, -pos.x, -pos.y, -pos.z);
        	bLight.set(dLight.color, pos.x, pos.y, pos.z);
        	camposition.set(pos);	        	
        }
        
        modelBatch.begin(camera);
        modelBatch.render(instance, envir);
        modelBatch.end();	        
	}
	/**
	 * It is supposed that the order of the checkgroups corresponds to the order of materials
	 * if mat has blendAttr then it is transparent, if not, then it should have opacity 0
	 * @param groupChecks
	 * @param prefixes
	 */
	public void setOpacity()
	{	
		// still is loading the model, cannot access the instance
		if (instance == null) return;
		
		ArrayList<Material> mats = getMaterials(); // maybe getMaterials hasnt been called yet		
		for (int i=0; i<groupChecks.length; i++){
			Material mat = mats.get(i);
			if (mat == null) continue;
			
			// set alpha to 1
			if (groupChecks[i].isChecked() && mat.has(BlendingAttribute.Type) ) {
					// System.out.println("Remove blending: " + mat.id);
					mat.remove(BlendingAttribute.Type);					
			}
			// set alpha to 0
			if (!groupChecks[i].isChecked() && !mat.has(BlendingAttribute.Type)) {								
				// System.out.println("Set opacity to 0: " + mat.id);
				BlendingAttribute battr = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE);// GL10.GL_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				battr.opacity = 0f;	
				//battr.blended = false;
				mat.set(battr);
			}
		}
		
	}
	
	public ArrayList<Material> getMaterials()
	{
		if (instance == null || instance.materials == null){
			return null;
		}
		else if (materials!=null){
			return materials;
		}
		else {
			materials = new ArrayList<Material>();		
			for (Material mat : instance.materials) {
	        	//System.out.println(mat.id);
				if (!materials.contains(mat)){ 
					materials.add(mat);	
				}
			}
        }
		return materials;				
	}
	
	public void setChecks(CheckBox[] c){
		groupChecks = c;
		//setOpacity();
	}	

	public void resize(int width, int height) {
		camera.viewportWidth = width;
	    camera.viewportHeight = height;
	    camera.update(true);				
	}	
	
	public void dispose() {
		modelBatch.dispose();
		model.dispose();		
	}

	
}

