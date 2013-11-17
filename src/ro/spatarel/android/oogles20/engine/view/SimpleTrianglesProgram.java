package ro.spatarel.android.oogles20.engine.view;

import android.opengl.Matrix;
import ro.spatarel.android.oogles20.OOBuffer;
import ro.spatarel.android.oogles20.OOBufferDataType;
import ro.spatarel.android.oogles20.OODrawPrimitive;
import ro.spatarel.android.oogles20.OOGLES20;
import ro.spatarel.android.oogles20.engine.engine.Program;
import ro.spatarel.android.oogles20.engine.engine.SceneGraph;
import ro.spatarel.android.oogles20.engine.engine.SceneObject;
import ro.spatarel.android.oogles20.engine.engine.TextureArchive;

public class SimpleTrianglesProgram extends SimpleProgram {
	
	public SimpleTrianglesProgram() {
		super();
	}
	
	@Override
	public void draw(SceneObject object, float[] projectionMatrix, float[] MVMatrix, SceneGraph graph,
			TextureArchive textureArchive) {
		// Test the OOBuffer objects.
		OOBuffer aPosition = object.getOOBuffer("aPosition");
		OOBuffer aColor = object.getOOBuffer("aTrianglesColor");
		OOBuffer triangles = object.getOOBuffer("triangles");
		Integer trianglesCount = object.getElementCount("triangles");
		if (aPosition == null || aColor == null || triangles == null || trianglesCount == null) {
			return;
		}
		
		// Load data.
		OOGLES20.arrayBuffer.bind(aPosition);
        this.getVectorAttribute("aPosition").enableArray();
        this.getVectorAttribute("aPosition").setBuffer4fv();
        this.getVectorAttribute("aPosition").disableArray();
		OOGLES20.arrayBuffer.bind(aColor);
        this.getVectorAttribute("aColor").enableArray();
        this.getVectorAttribute("aColor").setBuffer4fv();
        this.getVectorAttribute("aColor").disableArray();
		OOGLES20.arrayBuffer.unbind();
		
		// Load uniforms.
		Matrix.multiplyMM(Program.uMVPMatrix, 0, projectionMatrix, 0, MVMatrix, 0);
		this.getUniform("uMVPMatrix").setMatrix4fv(1, Program.uMVPMatrix, 0);
		
		// Draw from data.
		this.getVectorAttribute("aPosition").enableArray();
        this.getVectorAttribute("aColor").enableArray();
		OOGLES20.elementArrayBuffer.bind(triangles);
        OOGLES20.framebuffer.drawElements(OODrawPrimitive.TRIANGLES, trianglesCount, OOBufferDataType.UNSIGNED_SHORT,
        		0);
		OOGLES20.elementArrayBuffer.unbind();
        this.getVectorAttribute("aPosition").disableArray();
        this.getVectorAttribute("aColor").disableArray();
	}
}
