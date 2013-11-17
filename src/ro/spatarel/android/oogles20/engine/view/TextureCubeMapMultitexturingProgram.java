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

public class TextureCubeMapMultitexturingProgram extends Program {
	
	public TextureCubeMapMultitexturingProgram() {
		/*
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
				+ "\n" + "uniform samplerCube uTextureUnitLight;"
				+ "\n" + "uniform samplerCube uTextureUnitDark;"
				+ "\n" + ""
				+ "\n" + "varying vec3 vTextureCubeMapping;"
				+ "\n" + ""
				+ "\n" + "void main() {"
				+ "\n" + "    float light = max(0.0, min(1.0, vTextureCubeMapping.x + 0.5));"
				+ "\n" + "    vec4 lightColor = textureCube(uTextureUnitLight, vTextureCubeMapping);"
				+ "\n" + "    vec4 darkColor = textureCube(uTextureUnitDark, vTextureCubeMapping);"
				+ "\n" + "    gl_FragColor = light * lightColor + (1.0 - light) * darkColor;"
				+ "\n" + "}"
				);//*/
		this.setVertexShaderCode(""
				+ "\n" + "uniform mat4 uMVPMatrix;"
				+ "\n" + "uniform mat4 uMVMatrix;"
				+ "\n" + ""
				+ "\n" + "attribute vec4 aPosition;"
				+ "\n" + "attribute vec3 aTextureCubeMapping;"
				+ "\n" + "attribute vec3 aNormal;"
				+ "\n" + ""
				+ "\n" + "varying vec3 vPosition;"
				+ "\n" + "varying vec3 vTextureCubeMapping;"
				+ "\n" + "varying vec3 vNormal;"
				+ "\n" + ""
				+ "\n" + "void main() {"
				+ "\n" + "    vPosition = vec3(uMVMatrix * aPosition);"
				+ "\n" + "    vTextureCubeMapping = aTextureCubeMapping;"
				+ "\n" + "    vNormal = vec3(uMVMatrix * vec4(aNormal, 0.0));"
				+ "\n" + "    gl_Position = uMVPMatrix * aPosition;"
				+ "\n" + "}"
				);
		
		this.setFragmentShaderCode(""
				+ "\n" + "precision highp float;"
				+ "\n" + ""
				+ "\n" + "uniform vec4 uLightPosition;"
				+ "\n" + ""
				+ "\n" + "uniform samplerCube uTextureUnitLight;"
				+ "\n" + "uniform samplerCube uTextureUnitDark;"
				+ "\n" + ""
				+ "\n" + "varying vec3 vPosition;"
				+ "\n" + "varying vec3 vTextureCubeMapping;"
				+ "\n" + "varying vec3 vNormal;"
				+ "\n" + ""
				+ "\n" + "void main() {"
				+ "\n" + "    float distance = length(vec3(uLightPosition) - vPosition);"
				+ "\n" + "    vec3 lightVector = normalize(vec3(uLightPosition) - vPosition);"
				+ "\n" + "    float lightIntensity = dot(vNormal, lightVector);"
				+ "\n" + "    lightIntensity = lightIntensity * (1.0 / (1.0 + (0.025 * distance * distance)));"
				+ "\n" + "    lightIntensity = max(0.0, min(1.0, lightIntensity));"
				+ "\n" + "    vec4 lightColor = textureCube(uTextureUnitLight, vTextureCubeMapping);"
				+ "\n" + "    vec4 darkColor = textureCube(uTextureUnitDark, vTextureCubeMapping);"
				+ "\n" + "    gl_FragColor = lightIntensity * lightColor + (1.0 - lightIntensity) * darkColor;"
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
		String texture1Name = object.getTextureCubeMap(1);
		if (texture0Name == null || texture1Name == null) {
			return;
		}
		
		OOBuffer aPosition = object.getOOBuffer("aPosition");
		OOBuffer aTextureCubeMapping = object.getOOBuffer("aTextureCubeMapping");
		OOBuffer aNormal = object.getOOBuffer("aNormal");
		OOBuffer triangles = object.getOOBuffer("triangles");
		Integer trianglesCount = object.getElementCount("triangles");
		OOTexture texture0 = textureArchive.getTexture(texture0Name);
		OOTexture texture1 = textureArchive.getTexture(texture1Name);
		float[] uLightPosition = graph.getLightSourcePosition();
		if (aPosition == null || aTextureCubeMapping == null || aNormal == null || triangles == null ||
				trianglesCount == null || texture0 == null || texture1 == null || uLightPosition == null) {
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
		OOGLES20.arrayBuffer.bind(aNormal);
        this.getVectorAttribute("aNormal").enableArray();
        this.getVectorAttribute("aNormal").setBuffer3fv();
        this.getVectorAttribute("aNormal").disableArray();
		OOGLES20.arrayBuffer.unbind();
		
		// Bind textures.
		OOGLES20.textureUnit(0).texCubeMap.bind(texture0);
		OOGLES20.textureUnit(1).texCubeMap.bind(texture1);
		
		// Load uniforms.
		Matrix.multiplyMM(Program.uMVPMatrix, 0, projectionMatrix, 0, MVMatrix, 0);
		this.getUniform("uMVMatrix").setMatrix4fv(1, MVMatrix, 0);
		this.getUniform("uMVPMatrix").setMatrix4fv(1, Program.uMVPMatrix, 0);
		this.getUniform("uTextureUnitLight").set1i(0);
		this.getUniform("uTextureUnitDark").set1i(1);
		this.getUniform("uLightPosition").set4fv(1, uLightPosition, 0);
		
		// Draw from data.
		this.getVectorAttribute("aPosition").enableArray();
        this.getVectorAttribute("aTextureCubeMapping").enableArray();
        this.getVectorAttribute("aNormal").enableArray();
		OOGLES20.elementArrayBuffer.bind(triangles);
        OOGLES20.framebuffer.drawElements(OODrawPrimitive.TRIANGLES, trianglesCount, OOBufferDataType.UNSIGNED_SHORT,
        		0);
		OOGLES20.elementArrayBuffer.unbind();
        this.getVectorAttribute("aPosition").disableArray();
        this.getVectorAttribute("aTextureCubeMapping").disableArray();
        this.getVectorAttribute("aNormal").disableArray();
        
        // Unbind textures.
        OOGLES20.textureUnit(0).texCubeMap.unbind();
        OOGLES20.textureUnit(1).texCubeMap.unbind();
	}
}
