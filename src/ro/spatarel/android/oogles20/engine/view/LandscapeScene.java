package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneGraph;
import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class LandscapeScene extends SceneGraph {
	
	private SceneObject landscape = new LandscapeObject();
	
	public LandscapeScene() {
		this.landscape.setScaling(1000.0f, 1000.0f, 1000.0f);
		
		this.setObject(this.landscape);
	}
}
