package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class CubeObject extends SceneObject {
	
	public CubeObject() {
		this.addAttributeArray("aPosition", new float[] {
				-1.0f, -1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f,  1.0f, 1.0f,
				-1.0f,  1.0f, -1.0f, 1.0f,
				-1.0f,  1.0f,  1.0f, 1.0f,
				 1.0f, -1.0f, -1.0f, 1.0f,
				 1.0f, -1.0f,  1.0f, 1.0f,
				 1.0f,  1.0f, -1.0f, 1.0f,
				 1.0f,  1.0f,  1.0f, 1.0f,
		});
		
		this.addAttributeArray("aTrianglesColor", new float[] {
				 1.0f,  0.0f,  0.0f, 1.0f,
				 0.0f,  1.0f,  0.0f, 1.0f,
				 0.0f,  0.0f,  1.0f, 1.0f,
				 1.0f,  1.0f,  0.0f, 1.0f,
				 1.0f,  1.0f,  0.0f, 1.0f,
				 0.0f,  0.0f,  1.0f, 1.0f,
				 0.0f,  1.0f,  0.0f, 1.0f,
				 1.0f,  0.0f,  0.0f, 1.0f,
		});
		
		this.addUniformfv("aLinesColor", new float[] {1.0f, 1.0f, 1.0f, 1.0f});
		
		this.addAttributeArray("aTextureCubeMapping", new float[] {
				-1.0f, -1.0f, -1.0f,
				-1.0f, -1.0f,  1.0f,
				-1.0f,  1.0f, -1.0f,
				-1.0f,  1.0f,  1.0f,
				 1.0f, -1.0f, -1.0f,
				 1.0f, -1.0f,  1.0f,
				 1.0f,  1.0f, -1.0f,
				 1.0f,  1.0f,  1.0f,
		});
		
		this.addElementArray("triangles", new short[] {
				 0,  1,  2,
				 1,  2,  3,
				 4,  5,  6,
				 5,  6,  7,
				 0,  1,  4,
				 1,  4,  5,
				 2,  3,  6,
				 3,  6,  7,
				 0,  2,  4,
				 2,  4,  6,
				 1,  3,  5,
				 3,  5,  7,
		});
		this.addElementCount("triangles", 36);
		
		this.addElementArray("lines", new short[] {
				 0,  4,
				 1,  5,
				 2,  6,
				 3,  7,
				 0,  2,
				 1,  3,
				 4,  6,
				 5,  7,
				 0,  1,
				 2,  3,
				 4,  5,
				 6,  7,
		});
		this.addElementCount("lines", 24);
	}
}
