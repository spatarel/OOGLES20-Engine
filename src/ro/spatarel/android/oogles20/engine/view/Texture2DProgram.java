package ro.spatarel.android.oogles20.engine.view;

import android.opengl.Matrix;
import ro.spatarel.android.oogles20.OOBuffer;
import ro.spatarel.android.oogles20.OOBufferDataType;
import ro.spatarel.android.oogles20.OODrawPrimitive;
import ro.spatarel.android.oogles20.OOGLES20;
import ro.spatarel.android.oogles20.OOTexture;
import ro.spatarel.android.oogles20.engine.engine.Program;
import ro.spatarel.android.oogles20.engine.engine.SceneGraph;
import ro.spatarel.android.oogles20.engine.engine.SceneObject;
import ro.spatarel.android.oogles20.engine.engine.TextureArchive;

public class Texture2DProgram extends Program {
	
	public Texture2DProgram() {
		this.setVertexShaderCode(""
				+ "\n" + "uniform mat4 uMVPMatrix;"
				+ "\n" + ""
				+ "\n" + "attribute vec4 aPosition;"
				+ "\n" + "attribute vec2 aTexture2DMapping;"
				+ "\n" + ""
				+ "\n" + "varying vec2 vTexture2DMapping;"
				+ "\n" + ""
				+ "\n" + "void main() {"
				+ "\n" + "    vTexture2DMapping = aTexture2DMapping;"
				+ "\n" + "    gl_Position = uMVPMatrix * aPosition;"
				+ "\n" + "}"
				);
		
		this.setFragmentShaderCode(""
				+ "\n" + "precision highp float;"
				+ "\n" + ""
				+ "\n" + "uniform sampler2D uTextureUnit;"
				+ "\n" + ""
				+ "\n" + "varying vec2 vTexture2DMapping;"
				+ "\n" + ""
				+ "\n" + "void main() {"
				+ "\n" + "    gl_FragColor = texture2D(uTextureUnit, vTexture2DMapping);"
				+ "\n" + "}"
				);
		
		this.linkAndValidate();
	}
	
	@Override
	public void draw(SceneObject object, float[] projectionMatrix, float[] MVMatrix, SceneGraph graph,
			TextureArchive textureArchive) {
		// Test if the object can be rendered.
		if (textureArchive == null) {
			return;
		}
		
		String texture0Name = object.getTexture2D(0);
		if (texture0Name == null) {
			return;
		}
		
		OOBuffer aPosition = object.getOOBuffer("aPosition");
		OOBuffer aTexture2DMapping = object.getOOBuffer("aTexture2DMapping");
		OOBuffer triangles = object.getOOBuffer("triangles");
		Integer trianglesCount = object.getElementCount("triangles");
		OOTexture texture0 = textureArchive.getTexture(texture0Name);
		if (aPosition == null || aTexture2DMapping == null || triangles == null || trianglesCount == null ||
				texture0 == null) {
			return;
		}
		
		// Bind textures.
		OOGLES20.textureUnit(0).tex2D.bind(texture0);
		
		// Load data.
		OOGLES20.arrayBuffer.bind(aPosition);
        this.getVectorAttribute("aPosition").enableArray();
        this.getVectorAttribute("aPosition").setBuffer4fv();
        this.getVectorAttribute("aPosition").disableArray();
		OOGLES20.arrayBuffer.bind(aTexture2DMapping);
        this.getVectorAttribute("aTexture2DMapping").enableArray();
        this.getVectorAttribute("aTexture2DMapping").setBuffer2fv();
        this.getVectorAttribute("aTexture2DMapping").disableArray();
		OOGLES20.arrayBuffer.unbind();
		
		// Load uniforms.
		Matrix.multiplyMM(Program.uMVPMatrix, 0, projectionMatrix, 0, MVMatrix, 0);
		this.getUniform("uMVPMatrix").setMatrix4fv(1, Program.uMVPMatrix, 0);
		this.getUniform("uTextureUnit").set1i(0);
		
		// Draw from data.
		this.getVectorAttribute("aPosition").enableArray();
        this.getVectorAttribute("aTexture2DMapping").enableArray();
		OOGLES20.elementArrayBuffer.bind(triangles);
        OOGLES20.framebuffer.drawElements(OODrawPrimitive.TRIANGLES, trianglesCount, OOBufferDataType.UNSIGNED_SHORT,
        		0);
		OOGLES20.elementArrayBuffer.unbind();
        this.getVectorAttribute("aPosition").disableArray();
        this.getVectorAttribute("aTexture2DMapping").disableArray();
        
        // Unbind textures.
        OOGLES20.textureUnit(0).tex2D.unbind();
	}
}
