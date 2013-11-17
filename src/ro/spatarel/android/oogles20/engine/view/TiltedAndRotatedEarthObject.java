package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class TiltedAndRotatedEarthObject extends SceneObject {
	
	TiltedEarthObject tiltedEarth = new TiltedEarthObject();
	
	public TiltedAndRotatedEarthObject() {
		this.addObject(this.tiltedEarth);
	}
}
