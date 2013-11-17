package ro.spatarel.android.oogles20.engine.view;

public class LandscapeObject extends CubeObject {
	
	public LandscapeObject() {
		this.removeAttributeArray("aLinesColor");
		this.setTextureCubeMap(0, "landscape");
	}
}
