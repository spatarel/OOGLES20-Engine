package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class SolarSystemObject extends SceneObject {
	
	TiltedAndRotatedEarthObject tiltedAndRotatedEarth = new TiltedAndRotatedEarthObject();
	
	public SolarSystemObject() {
		this.addObject(this.tiltedAndRotatedEarth);
	}
}
