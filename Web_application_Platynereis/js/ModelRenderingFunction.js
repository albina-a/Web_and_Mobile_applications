/**
* @ author albina
* Loading a Blender model using THREE.js
*
*/


function RenderModel( modelPath, divID, mPos, lPos ) {

	// WebGL check
	if (!Detector.webgl) 
	{
		Detector.addGetWebGLMessage();
	}
	
	var container, zmesh;
	var camera, scene, renderer, directionalLight, controls;
	var lightPos, meshPos;
	var mats;
	var cboxName;
	
				
	// Load the mesh exported from blender
	var loader = new THREE.JSONLoader();
	// 'js/model_em_obj.js'
	loader.load(modelPath, function (geometry, materials) 
	{
		init();
	
		zmesh = new THREE.Mesh( geometry, new THREE.MeshFaceMaterial( materials ) );
		if (mPos) {
			zmesh.position.set( mPos[0], mPos[1], mPos[2] );
		}
		else {
			zmesh.position.set(0,0,0); //( -2,1,3 );
		}
		zmesh.rotation.x=180*Math.PI/180;
		//zmesh.rotation.y=-50*Math.PI/180;
		scene.add( zmesh );
	
		mats = materials;
		cboxName = divID + "-vis";
    	// init checkboxes        
		initObjectList(mats);
		animate();
	} );
	
	function initObjectList(materials)
	{
		var node = document.getElementById(divID + "-cbox");
		//node.innerHTML = "<p>" + "Hello World!" + "</p>";
		var matNames=[];
		
		var len = materials.length;
		for (var i=0; i<len; ++i) {
			if (i in materials) {
				var mat = materials[i];
				// mat.opacity = 0.8; //{'r':0.9,'g':0.9,'b':0.9};
				var matName = mat.name;
				// create a checkbox for a material
				if (matNames.indexOf(matName) < 0) {					
					var colst = mat.color.getHexString();
					//var result = matName.fontcolor('#'+colst);
					node.innerHTML += "<p style=\"color:white; background-color:#" + colst + ";\"><input type=\"checkbox\" checked = \"True\" name=\"" + cboxName + "\" value=\"" + matName + "\">" + matName + "</p>"

					matNames.push(matName)
				}
			  
			}
		  
		}
		node.innerHTML += "<button type=\"button\" id=\"apply_alpha-" + divID + "\">Apply</button> <br>"
		document.getElementById("apply_alpha-"+ divID).onclick = applyAlpha;

	}
	
	function applyAlpha()
	{
	   var inputElements = document.getElementsByTagName('input');
		for(var i=0; inputElements[i]; ++i){
			if(inputElements[i].name===cboxName) 
			{
				// value stores mat name
				var matName = inputElements[i].value;
				// get all materials with this name
				var nmats = matsByName(matName);
				if (nmats && nmats.length>0) // if there are results
				{
					var len = nmats.length;
					for (var j=0; j<len; ++j)  // loop
					{
						if (j in nmats) 
						{
							// if visibility is different, change it
							if (nmats[j].visible != inputElements[i].checked) {
								nmats[j].visible = inputElements[i].checked;
							}
						}
					}
				}
			}
		}
	}
	
	function matsByName(matName)
	{
		var len = mats.length;
		var nmats = [];
		for (var i=0; i<len; ++i) {
			if (i in mats) {
				var mName = mats[i].name;
				if ( mName == matName) 
				{
					nmats.push(mats[i]);
				}
			}
		  
		}
		return nmats;
	}
	
	function init() 
	{
		container = document.createElement( 'div' );
		//document.body.appendChild( container );
		var div = document.getElementById(divID)
		div.appendChild(container); //'em-model'
		
		scene = new THREE.Scene();
		// CAMERA
		camera = new THREE.PerspectiveCamera( 45, 1, 1, 50 ); //window.innerWidth / window.innerHeight
		if (lPos) {
			camera.position.set( lPos[0], lPos[1], lPos[2] );
		}
		else {
			camera.position.set( 0, 0, 21 );
		}
		camera.lookAt( scene.position );
	
		// LIGHTS
		scene.add( new THREE.AmbientLight( 0x666666 ) ); 
		
		directionalLight = new THREE.DirectionalLight(0x555555, 3)
		directionalLight.position.set(0, 0, 21);
		//directionalLight.target.set(0,0,0);
		scene.add( directionalLight );
	
		// RENDERER
		renderer = new THREE.WebGLRenderer();//{ clearColor: 0x999999, clearAlpha: 1 } );
		renderer.setClearColor(0xcccccc,1); // 999999 - thesis
		renderer.setSize(window.innerWidth * 0.4, window.innerWidth*0.4) ; //window.innerWidth, window.innerHeight );    	        
		container.appendChild( renderer.domElement );
		
		// CONTROLS
		controls = new THREE.OrbitControls( camera, div );
		controls.addEventListener( 'change', render );
	
		
		//window.addEventListener( 'resize', onWindowResize, false );
	}
	
	// might never be used, layout is fixed for the home page
	function onWindowResize() 
	{    	    	
		renderer.setSize( window.innerWidth*0.4, window.innerWidth()*0.4 );
		camera.aspect = 1.0; //window.innerWidth / window.innerHeight;
		camera.updateProjectionMatrix();
		
	}
	
	function animate() 
	{			
		requestAnimationFrame( animate );
		controls.update();
		
		render();
	}
	
	function render() // on framerequest
	{	
		directionalLight.position = camera.position;
		//pointLight.position = camera.position;
		
		renderer.render( scene, camera );
				
	}

};
