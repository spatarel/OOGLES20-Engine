package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class GridObject extends SceneObject {
	
	public GridObject() {
		this.addAttributeArray("aPosition", new float[] {
				-1.0f,  0.0f, -1.0f,  1.0f,
				-1.0f,  0.0f, -0.8f,  1.0f,
				-1.0f,  0.0f, -0.6f,  1.0f,
				-1.0f,  0.0f, -0.4f,  1.0f,
				-1.0f,  0.0f, -0.2f,  1.0f,
				-1.0f,  0.0f,  0.0f,  1.0f,
				-1.0f,  0.0f,  0.2f,  1.0f,
				-1.0f,  0.0f,  0.4f,  1.0f,
				-1.0f,  0.0f,  0.6f,  1.0f,
				-1.0f,  0.0f,  0.8f,  1.0f,
				-1.0f,  0.0f,  1.0f,  1.0f,
				
				 1.0f,  0.0f, -1.0f,  1.0f,
				 1.0f,  0.0f, -0.8f,  1.0f,
				 1.0f,  0.0f, -0.6f,  1.0f,
				 1.0f,  0.0f, -0.4f,  1.0f,
				 1.0f,  0.0f, -0.2f,  1.0f,
				 1.0f,  0.0f,  0.0f,  1.0f,
				 1.0f,  0.0f,  0.2f,  1.0f,
				 1.0f,  0.0f,  0.4f,  1.0f,
				 1.0f,  0.0f,  0.6f,  1.0f,
				 1.0f,  0.0f,  0.8f,  1.0f,
				 1.0f,  0.0f,  1.0f,  1.0f,
		});
		
		this.addElementArray("lines", new short[] {
				 0, 11,
				 1, 12,
				 2, 13,
				 3, 14,
				 4, 15,
				 5, 16,
				 6, 17,
				 7, 18,
				 8, 19,
				 9, 20,
				10, 21,
		});
		this.addElementCount("lines", 22);
		
		this.addUniformfv("aLinesColor", new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	}
}
