package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class ReferenceSystemObject extends SceneObject {
	
	public ReferenceSystemObject() {
		this.addAttributeArray("aPosition", new float[] {
				 0.0f,  0.0f,  0.0f, 1.0f,
				 1.0f,  0.0f,  0.0f, 1.0f,
				 0.0f,  0.0f,  0.0f, 1.0f,
				 0.0f,  1.0f,  0.0f, 1.0f,
				 0.0f,  0.0f,  0.0f, 1.0f,
				 0.0f,  0.0f,  1.0f, 1.0f,
				 0.0f,  0.0f,  0.0f, 1.0f,
				-1.0f,  0.0f,  0.0f, 1.0f,
				 0.0f,  0.0f,  0.0f, 1.0f,
				 0.0f, -1.0f,  0.0f, 1.0f,
				 0.0f,  0.0f,  0.0f, 1.0f,
				 0.0f,  0.0f, -1.0f, 1.0f,
		});
		
		this.addAttributeArray("aLinesColor", new float[] {
				 1.0f,  0.0f,  0.0f, 1.0f,
				 1.0f,  0.0f,  0.0f, 1.0f,
				 0.0f,  1.0f,  0.0f, 1.0f,
				 0.0f,  1.0f,  0.0f, 1.0f,
				 0.0f,  0.0f,  1.0f, 1.0f,
				 0.0f,  0.0f,  1.0f, 1.0f,
				 0.5f,  0.5f,  0.5f, 1.0f,
				 0.5f,  0.5f,  0.5f, 1.0f,
				 0.5f,  0.5f,  0.5f, 1.0f,
				 0.5f,  0.5f,  0.5f, 1.0f,
				 0.5f,  0.5f,  0.5f, 1.0f,
				 0.5f,  0.5f,  0.5f, 1.0f,
		});
		
		this.addElementArray("lines", new short[] {
				 0,  1,
				 2,  3,
				 4,  5,
				 6,  7,
				 8,  9,
				10, 11,
		});
		this.addElementCount("lines", 12);
	}
}
