package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class GroundObject extends SceneObject {
	
	public GroundObject() {
		this.addAttributeArray("aPosition", new float[] {
				-1000.0f,  0.0f, -1000.0f,  1.0f,
				-1000.0f,  0.0f,  1000.0f,  1.0f,
				 1000.0f,  0.0f, -1000.0f,  1.0f,
				 1000.0f,  0.0f,  1000.0f,  1.0f,
		});
		
		this.addAttributeArray("aTexture2DMapping", new float[] {
				-1000.0f, -1000.0f,
				-1000.0f,  1000.0f,
				 1000.0f, -1000.0f,
				 1000.0f,  1000.0f,
		});
		
		this.addElementArray("triangles", new short[] {
				 0,  1,  2,
				 1,  2,  3,
		});
		this.addElementCount("triangles", 6);
		
		this.setTexture2D(0, "ground");
	}
}
