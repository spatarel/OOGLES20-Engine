package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class TiltedEarthObject extends SceneObject {
	
	EarthObject earth = new EarthObject();
	
	public TiltedEarthObject() {
		this.addObject(this.earth);
	}
}
