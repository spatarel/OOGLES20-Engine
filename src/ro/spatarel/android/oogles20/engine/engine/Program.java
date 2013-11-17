package ro.spatarel.android.oogles20.engine.engine;

import ro.spatarel.android.oogles20.OOProgram;
import ro.spatarel.android.oogles20.OOShader;
import ro.spatarel.android.oogles20.OOShaderType;

public abstract class Program extends OOProgram {

	private String vertexShaderCode;
	
	private String fragmentShaderCode;
	
	public Program() {
		// Nothing here.
	}
	
	public void setVertexShaderCode(String vertexShaderCode) {
		this.vertexShaderCode = vertexShaderCode;
	}
	
	public void setFragmentShaderCode(String fragmentShaderCode) {
		this.fragmentShaderCode = fragmentShaderCode;
	}
	
	private OOShader loadShader(OOShaderType type, String shaderCode) {
	    OOShader shader = new OOShader(type);
	    shader.setSource(shaderCode);
	    shader.compile();
	    if (!shader.getCompileStatus()) {
	    	System.out.println(shader.getInfoLog());
        	throw new RuntimeException("Shader compiling error!");
	    }
	    
	    return shader;
	}
    
	public void linkAndValidate() {
		OOShader vertexShader = this.loadShader(OOShaderType.VERTEX, this.vertexShaderCode);
		OOShader fragmentShader = this.loadShader(OOShaderType.FRAGMENT, this.fragmentShaderCode);

        this.attachShader(vertexShader);
        this.attachShader(fragmentShader);
        
        this.validate();
        this.link();
        if (!super.getLinkStatus()) {
            System.out.println(super.getInfoLog());
        	throw new RuntimeException("Program linking error!");
        }
        this.validate();
        if (!super.getValidateStatus()) {
            System.out.println(super.getInfoLog());
        	throw new RuntimeException("Program validation error!");
        }
	}
	
	protected static float[] uMVPMatrix = new float[16];
	
	public abstract void draw(SceneObject object, float[] projectionMatrix, float[] MVMatrix, SceneGraph graph,
			TextureArchive textureArchive);
}
