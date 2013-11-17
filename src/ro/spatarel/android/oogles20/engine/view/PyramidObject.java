package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class PyramidObject extends SceneObject {
	
	public PyramidObject() {
		this.addAttributeArray("aPosition", new float[] {
				-1.0f,  0.0f, -1.0f,  1.0f,
				-1.0f,  0.0f,  1.0f,  1.0f,
				 0.0f,  1.0f,  0.0f,  1.0f,
				
				-1.0f,  0.0f,  1.0f,  1.0f,
				 1.0f,  0.0f,  1.0f,  1.0f,
				 0.0f,  1.0f,  0.0f,  1.0f,
				
				 1.0f,  0.0f,  1.0f,  1.0f,
				 1.0f,  0.0f, -1.0f,  1.0f,
				 0.0f,  1.0f,  0.0f,  1.0f,
				
				 1.0f,  0.0f, -1.0f,  1.0f,
				-1.0f,  0.0f, -1.0f,  1.0f,
				 0.0f,  1.0f,  0.0f,  1.0f,
		});
		
		this.addAttributeArray("aTrianglesColor", new float[] {
				 1.0f,  1.0f,  0.0f, 1.0f,
				 1.0f,  1.0f,  0.0f, 1.0f,
				 1.0f,  1.0f,  0.0f, 1.0f,
				
				 0.0f,  1.0f,  1.0f, 1.0f,
				 0.0f,  1.0f,  1.0f, 1.0f,
				 0.0f,  1.0f,  1.0f, 1.0f,
				
				 1.0f,  0.0f,  1.0f, 1.0f,
				 1.0f,  0.0f,  1.0f, 1.0f,
				 1.0f,  0.0f,  1.0f, 1.0f,
				
				 0.0f,  0.0f,  1.0f, 1.0f,
				 0.0f,  0.0f,  1.0f, 1.0f,
				 0.0f,  0.0f,  1.0f, 1.0f,
		});
		
		this.addElementArray("triangles", new short[] {
				 0,  1,  2,
				 3,  4,  5,
				 6,  7,  8,
				 9, 10, 11,
		});
		this.addElementCount("triangles", 12);
		
		this.addElementArray("lines", new short[] {
				 0,  1,
				 3,  4,
				 6,  7,
				 9, 10,
				 0,  2,
				 3,  2,
				 6,  2,
				 9,  2,
		});
		this.addElementCount("lines", 16);
		
		this.addUniformfv("aLinesColor", new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	}
}
