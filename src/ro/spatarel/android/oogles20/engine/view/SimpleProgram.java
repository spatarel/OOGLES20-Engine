package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.Program;

public abstract class SimpleProgram extends Program {
	
	public SimpleProgram() {
		this.setVertexShaderCode(""
				+ "\n" + "uniform mat4 uMVPMatrix;"
				+ "\n" + ""
				+ "\n" + "attribute vec4 aPosition;"
				+ "\n" + "attribute vec4 aColor;"
				+ "\n" + ""
				+ "\n" + "varying vec4 vColor;"
				+ "\n" + ""
				+ "\n" + "void main() {"
				+ "\n" + "    vColor = aColor;"
				+ "\n" + "    gl_Position = uMVPMatrix * aPosition;"
				+ "\n" + "}"
				);
		
		this.setFragmentShaderCode(""
				+ "\n" + "precision highp float;"
				+ "\n" + ""
				+ "\n" + "varying vec4 vColor;"
				+ "\n" + ""
				+ "\n" + "void main() {"
				+ "\n" + "    gl_FragColor = vColor;"
				+ "\n" + "}"
				);
		
		this.linkAndValidate();
	}
}
