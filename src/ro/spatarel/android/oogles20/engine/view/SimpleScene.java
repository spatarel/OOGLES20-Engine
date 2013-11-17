package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneGraph;
import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class SimpleScene extends SceneGraph {
	
	private SceneObject universe = new SceneObject();
	
	private SceneObject lightSource = new LightSourceObject();
	private SceneObject referenceSystem = new ReferenceSystemObject();
	private SceneObject gridMiniHorizontaly = new GridObject();
	private SceneObject gridMiniVertcaly = new GridObject();
	private SceneObject gridHorizontaly = new GridObject();
	private SceneObject gridVertcaly = new GridObject();
	private SceneObject[] pyramids = new SceneObject[5];
	private SceneObject ground = new GroundObject();
	
	SolarSystemObject solarSystem = new SolarSystemObject();
	
	
	public SimpleScene() {
		this.lightSource.setTranslation(0.0f, 3.0f, 0.0f);
		
		this.referenceSystem.setScaling(10000.0f, 10000.0f, 10000.0f);
		
		this.gridMiniHorizontaly.setScaling(0.5f, 1.0f, 0.5f);
		this.gridMiniVertcaly.setScaling(0.5f, 1.0f, 0.5f);
		this.gridMiniVertcaly.setRotationEulerAngles(0.0f, 90.0f, 0.0f);
		this.gridHorizontaly.setScaling(5.0f, 1.0f, 5.0f);
		this.gridVertcaly.setScaling(5.0f, 1.0f, 5.0f);
		this.gridVertcaly.setRotationEulerAngles(0.0f, 90.0f, 0.0f);
		
		int i;
		for (i = 0; i < this.pyramids.length; ++i) {
			this.pyramids[i] = new PyramidObject();
			this.pyramids[i].setScaling(0.5f, (float)(i + 1), 0.5f);
			this.pyramids[i].setTranslation((float)(2 * (i - 2)), 0.0f, 0.0f);
			this.pyramids[i].setRotationEulerAngles(0.0f, (float)(60 * (i - 2)), 0.0f);
		}
		
		this.solarSystem.setTranslation(0.0f, 3.0f, 0.0f);
		
		this.universe.addObject(this.lightSource);
		this.universe.addObject(this.referenceSystem);
		this.universe.addObject(this.gridMiniHorizontaly);
		this.universe.addObject(this.gridMiniVertcaly);
		this.universe.addObject(this.gridHorizontaly);
		this.universe.addObject(this.gridVertcaly);
		for (i = 0; i < this.pyramids.length; ++i) {
			this.universe.addObject(this.pyramids[i]);
		}
		this.universe.addObject(this.ground);
		this.universe.addObject(this.solarSystem);
		
		this.setObject(this.universe);
		this.setLightSource(this.lightSource);
	}
}
