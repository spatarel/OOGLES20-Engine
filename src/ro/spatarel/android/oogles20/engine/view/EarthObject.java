package ro.spatarel.android.oogles20.engine.view;

public class EarthObject extends SphereObject {
	
	public EarthObject() {
		this.setTextureCubeMap(0, "earth");
		this.setTextureCubeMap(1, "earthDark");
		this.removeAttributeArray("aTrianglesColor");
	}
}
