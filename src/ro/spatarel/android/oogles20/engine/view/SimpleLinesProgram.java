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

public class SimpleLinesProgram extends SimpleProgram {
	
	static final float[] aSelectedColor = new float[] {1.0f, 0.0f, 0.0f, 1.0f};
	
	public SimpleLinesProgram() {
		super();
	}
	
	@Override
	public void draw(SceneObject object, float[] projectionMatrix, float[] MVMatrix, SceneGraph graph,
			TextureArchive textureArchive) {
		// Test the OOBuffer objects.
		OOBuffer aPosition = object.getOOBuffer("aPosition");
		float[] aColor;
		if (object.isSelected()) {
			aColor = SimpleLinesProgram.aSelectedColor;
		} else {
			aColor = object.getUniformfv("aLinesColor");
		}
		OOBuffer aColorBuffer = object.getOOBuffer("aLinesColor");
		OOBuffer lines = object.getOOBuffer("lines");
		Integer linesCount = object.getElementCount("lines");
		if (aPosition == null || (aColor != null && aColorBuffer != null) || lines == null || linesCount == null) {
			return;
		}
		
		// Load data.
		OOGLES20.arrayBuffer.bind(aPosition);
        this.getVectorAttribute("aPosition").enableArray();
        this.getVectorAttribute("aPosition").setBuffer4fv();
        this.getVectorAttribute("aPosition").disableArray();
        if (aColorBuffer != null) {
    		OOGLES20.arrayBuffer.bind(aColorBuffer);
            this.getVectorAttribute("aColor").enableArray();
            this.getVectorAttribute("aColor").setBuffer4fv();
            this.getVectorAttribute("aColor").disableArray();
        }
		OOGLES20.arrayBuffer.unbind();
		
		// Load uniforms.
		Matrix.multiplyMM(Program.uMVPMatrix, 0, projectionMatrix, 0, MVMatrix, 0);
		this.getUniform("uMVPMatrix").setMatrix4fv(1, Program.uMVPMatrix, 0);
		
		// Draw from data.
		this.getVectorAttribute("aPosition").enableArray();
        if (aColorBuffer != null) {
        	this.getVectorAttribute("aColor").enableArray();
        } else {
        	this.getVectorAttribute("aColor").setForAll4fv(aColor, 0);
        }
		OOGLES20.elementArrayBuffer.bind(lines);
        OOGLES20.framebuffer.drawElements(OODrawPrimitive.LINES, linesCount, OOBufferDataType.UNSIGNED_SHORT, 0);
		OOGLES20.elementArrayBuffer.unbind();
        this.getVectorAttribute("aPosition").disableArray();
        if (aColorBuffer != null) {
        	this.getVectorAttribute("aColor").disableArray();
        }
	}
}
