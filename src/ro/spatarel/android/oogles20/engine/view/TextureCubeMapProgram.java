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

public class TextureCubeMapProgram extends Program {
	
	public TextureCubeMapProgram() {
		this.setVertexShaderCode(""
				+ "\n" + "uniform mat4 uMVPMatrix;"
				+ "\n" + ""
				+ "\n" + "attribute vec4 aPosition;"
				+ "\n" + "attribute vec3 aTextureCubeMapping;"
				+ "\n" + ""
				+ "\n" + "varying vec3 vTextureCubeMapping;"
				+ "\n" + ""
				+ "\n" + "void main() {"
				+ "\n" + "    vTextureCubeMapping = aTextureCubeMapping;"
				+ "\n" + "    gl_Position = uMVPMatrix * aPosition;"
				+ "\n" + "}"
				);
		
		this.setFragmentShaderCode(""
				+ "\n" + "precision highp float;"
				+ "\n" + ""
				+ "\n" + "uniform samplerCube uTextureUnit;"
				+ "\n" + ""
				+ "\n" + "varying vec3 vTextureCubeMapping;"
				+ "\n" + ""
				+ "\n" + "void main() {"
				+ "\n" + "    gl_FragColor = textureCube(uTextureUnit, vTextureCubeMapping);"
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
		
		String texture0Name = object.getTextureCubeMap(0);
		if (texture0Name == null) {
			return;
		}
		
		OOBuffer aPosition = object.getOOBuffer("aPosition");
		OOBuffer aTextureCubeMapping = object.getOOBuffer("aTextureCubeMapping");
		OOBuffer triangles = object.getOOBuffer("triangles");
		Integer trianglesCount = object.getElementCount("triangles");
		OOTexture texture0 = textureArchive.getTexture(texture0Name);
		if (aPosition == null || aTextureCubeMapping == null || triangles == null || trianglesCount == null ||
				texture0 == null) {
			return;
		}
		
		// Load data.
		OOGLES20.arrayBuffer.bind(aPosition);
        this.getVectorAttribute("aPosition").enableArray();
        this.getVectorAttribute("aPosition").setBuffer4fv();
        this.getVectorAttribute("aPosition").disableArray();
		OOGLES20.arrayBuffer.bind(aTextureCubeMapping);
        this.getVectorAttribute("aTextureCubeMapping").enableArray();
        this.getVectorAttribute("aTextureCubeMapping").setBuffer3fv();
        this.getVectorAttribute("aTextureCubeMapping").disableArray();
		OOGLES20.arrayBuffer.unbind();
		
		// Bind textures.
		OOGLES20.textureUnit(0).texCubeMap.bind(texture0);
		
		// Load uniforms.
		Matrix.multiplyMM(Program.uMVPMatrix, 0, projectionMatrix, 0, MVMatrix, 0);
		this.getUniform("uMVPMatrix").setMatrix4fv(1, Program.uMVPMatrix, 0);
		this.getUniform("uTextureUnit").set1i(0);
		
		// Draw from data.
		this.getVectorAttribute("aPosition").enableArray();
        this.getVectorAttribute("aTextureCubeMapping").enableArray();
		OOGLES20.elementArrayBuffer.bind(triangles);
        OOGLES20.framebuffer.drawElements(OODrawPrimitive.TRIANGLES, trianglesCount, OOBufferDataType.UNSIGNED_SHORT,
        		0);
		OOGLES20.elementArrayBuffer.unbind();
        this.getVectorAttribute("aPosition").disableArray();
        this.getVectorAttribute("aTextureCubeMapping").disableArray();
        
        // Unbind textures.
        OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
}
