package ro.spatarel.android.oogles20.engine.engine;

import android.opengl.Matrix;

public class SceneGraph {
	
	static private final float[] identityMatrix = new float[16];
	
	private final float[] tempPosition = new float[4];
	
	private float[] translationViewMatrix = new float[16];
	
	private float[] rotationViewMatrix = new float[16];
	
	private float[] projectionMatrix = new float[16];
	
	
	private float[] rotationViewXAxisMatrix = new float[16];
	
	private float[] rotationViewYAxisMatrix = new float[16];
	
	private float[] rotationViewZAxisMatrix = new float[16];
	
	private float[] tempMatrix = new float[16];
	
	
	private float[] viewMatrix = new float[16];
	
	
	private SceneObject lightSource;
	
	
	private SceneObject object;
	
	
	static {
		Matrix.setIdentityM(SceneGraph.identityMatrix, 0);
	}
	
	/**
	 * Define a projection matrix in terms of a field of view angle, an aspect ratio, and z clip planes.
	 * 
	 * @param m The float array that holds the perspective matrix.
	 * @param offset The offset into float array m where the perspective matrix data is written.
	 * @param fovy Field of view in Y direction, in degrees.
	 * @param aspect Width to height aspect ratio of the viewport.
	 * @param zNear The near Z axis clipping plane coordinate.
	 * @param zFar The far Z axis clipping plane coordinate.
	 */
	private static void perspectiveM(float[] m, int offset, float fovy, float aspect, float zNear, float zFar) {
		float f = 1.0f / (float) Math.tan(fovy * (Math.PI / 360.0));
		float rangeReciprocal = 1.0f / (zNear - zFar);
		
		m[offset + 0] = f / aspect;
		m[offset + 1] = 0.0f;
		m[offset + 2] = 0.0f;
		m[offset + 3] = 0.0f;
		
		m[offset + 4] = 0.0f;
		m[offset + 5] = f;
		m[offset + 6] = 0.0f;
		m[offset + 7] = 0.0f;
		
		m[offset + 8] = 0.0f;
		m[offset + 9] = 0.0f;
		m[offset + 10] = (zFar + zNear) * rangeReciprocal;
		m[offset + 11] = -1.0f;
		
		m[offset + 12] = 0.0f;
		m[offset + 13] = 0.0f;
		m[offset + 14] = 2.0f * zFar * zNear * rangeReciprocal;
		m[offset + 15] = 0.0f;
    }
	
	
	public SceneGraph() {
		Matrix.setIdentityM(this.translationViewMatrix, 0);
		Matrix.setIdentityM(this.rotationViewMatrix, 0);
		Matrix.setIdentityM(this.projectionMatrix, 0);
		this.computeViewMatrix();
	}
	
	private void computeViewMatrix() {
		Matrix.multiplyMM(this.viewMatrix, 0, this.rotationViewMatrix, 0, this.translationViewMatrix, 0);
	}
	
	public float[] getRotationViewMatrix() {
		return this.rotationViewMatrix;
	}
	
	public void setTranslationView(float xAxis, float yAxis, float zAxis) {
		Matrix.translateM(this.translationViewMatrix, 0, SceneGraph.identityMatrix, 0, xAxis, yAxis, zAxis);
		this.computeViewMatrix();
	}
	
	public void setRotationViewAxis(float angleDegrees, float xVector, float yVector, float zVector) {
		Matrix.setRotateM(this.rotationViewMatrix, 0, angleDegrees, xVector, yVector, zVector);
		this.computeViewMatrix();
	}
	
	public void setRotationViewEulerAngles(float xAxisAngleDegrees, float yAxisAngleDegrees, float zAxisAngleDegrees) {
		Matrix.setRotateM(this.rotationViewYAxisMatrix, 0, yAxisAngleDegrees, 0.0f, 1.0f, 0.0f);
		Matrix.setRotateM(this.rotationViewXAxisMatrix, 0, xAxisAngleDegrees, 1.0f, 0.0f, 0.0f);
		Matrix.setRotateM(this.rotationViewZAxisMatrix, 0, zAxisAngleDegrees, 0.0f, 0.0f, 1.0f);
		
		Matrix.multiplyMM(this.tempMatrix, 0, this.rotationViewZAxisMatrix, 0, this.rotationViewXAxisMatrix, 0);
		Matrix.multiplyMM(this.rotationViewMatrix, 0, this.tempMatrix, 0, this.rotationViewYAxisMatrix, 0);
		
		this.computeViewMatrix();
	}
	
	public void setFrustumProjection(float left, float right, float bottom, float top, float near, float far) {
		Matrix.frustumM(this.projectionMatrix, 0, left, right, bottom, top, near, far);
		this.computeViewMatrix();
	}
	
	public void setOrthographicProjection(float left, float right, float bottom, float top, float near, float far) {
		Matrix.orthoM(this.projectionMatrix, 0, left, right, bottom, top, near, far);
		this.computeViewMatrix();
	}
	
	public void setPerspectiveProjection(float FOVYAxisDegrees, float widthToHeight, float zNear, float zFar) {
		SceneGraph.perspectiveM(this.projectionMatrix, 0, FOVYAxisDegrees, widthToHeight, zNear, zFar);
		this.computeViewMatrix();
	}
	
	
	public void setObject(SceneObject object) {
		this.object = object;
	}
	
	
	public void loadAttributeArrayToBuffer(String attributeName) {
		this.object.loadAttributeArrayToBufferRecursively(attributeName);
	}
	
	public void loadElementArrayToBuffer(String elementArrayName) {
		this.object.loadElementArrayToBufferRecursively(elementArrayName);
	}
	
	public void draw(Program program) {
		this.object.drawRecursively(program, this.projectionMatrix, this.viewMatrix, this, null);
	}
	
	public void draw(Program program, TextureArchive textureArchive) {
		this.object.drawRecursively(program, this.projectionMatrix, this.viewMatrix, this, textureArchive);
	}
	
	public void unloadArrayFromBuffer(String attributeName) {
		this.object.unloadArrayFromBuffer(attributeName);
	}
	
	
	public void setLightSource(SceneObject lightSource) {
		this.lightSource = lightSource;
	}
	
	public SceneObject getLightSource() {
		return this.lightSource;
	}
	
	public float[] getLightSourcePosition() {
		Matrix.multiplyMV(this.tempPosition, 0, this.viewMatrix, 0,
				this.object.getLightSourcePosition(this.lightSource), 0);
		return this.tempPosition;
	}
	
	
	public void setSelectedObject(SceneObject selectedObject) {
		this.object.setSelectedObjectRecursively(selectedObject);
	}
}
