package ro.spatarel.android.oogles20.engine.model;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;
import android.util.Log;

public class UniverseModel {
	
    private static final String TAG = UniverseModel.class.getName();
    
	private boolean isMovingForward;
	private boolean isMovingBackwards;
	private boolean isMovingSidewaysLeft;
	private boolean isMovingSidewaysRight;
	
	
	private float xPosition =   1.0f;
	private float yPosition =   5.0f;
	private float zPosition =  10.0f;
	
	private float pitchAngle;
	private float yawAngle;
	private float rollAngle;
	
	private float zoom;
	
	
	private float earthYearDate;
	
	
	private SceneObject selectedObject = null;
	
	
	private long lastDrawFrameTime = 0;
	
	public void update() {
    	long currentTime = System.nanoTime();
    	long deltaTime = currentTime - this.lastDrawFrameTime;
    	if (deltaTime > 1000000000L) {
    		deltaTime = 0L;
    	}
    	float distance = 0.0f;
    	if (this.isMovingForward) {
    		distance = (float)deltaTime / 1000000000.0f;
    	} else if (this.isMovingBackwards) {
    		distance = (float)-deltaTime / 1000000000.0f;
    	}
    	if (distance != 0.0f) {
    		this.zPosition += distance * (float)-Math.cos(this.yawAngle / 360.0f * (2.0f * Math.PI));
    		this.xPosition += distance * (float)-Math.sin(this.yawAngle / 360.0f * (2.0f * Math.PI));
    		Log.d(TAG, "Current time: " + currentTime);
    	}
    	distance = 0.0f;
    	if (this.isMovingSidewaysLeft) {
    		distance = (float)-deltaTime / 1000000000.0f;
    	} else if (this.isMovingSidewaysRight) {
    		distance = (float)deltaTime / 1000000000.0f;
    	}
    	if (distance != 0.0f) {
    		this.zPosition += distance * (float)-Math.cos((this.yawAngle - 90.0f) / 360.0f * (2.0f * Math.PI));
    		this.xPosition += distance * (float)-Math.sin((this.yawAngle - 90.0f) / 360.0f * (2.0f * Math.PI));
    		Log.d(TAG, "Current time: " + currentTime);
    	}
    	
    	
    	this.earthYearDate = (float)((double)(currentTime % 100000000000L) / 100000000000.0);
    	
    	
    	this.lastDrawFrameTime = currentTime;
	}
	
	public void setMoving(boolean isMovingForward, boolean isMovingBackwards,
			boolean isMovingSidewaysLeft, boolean isMovingSidewaysRight) {
		this.isMovingForward = isMovingForward;
		this.isMovingBackwards = isMovingBackwards;
		this.isMovingSidewaysLeft = isMovingSidewaysLeft;
		this.isMovingSidewaysRight = isMovingSidewaysRight;
	}
	
	public void setRotationViewEulerAngles(float pitchAngle, float yawAngle, float rollAngle) {
		this.pitchAngle = pitchAngle;
		this.yawAngle = yawAngle;
		this.rollAngle = rollAngle;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
	
	public float getXPosition() {
		return this.xPosition;
	}
	
	public float getYPosition() {
		return this.yPosition;
	}
	
	public float getZPosition() {
		return this.zPosition;
	}
	
	public float getPitchAngle() {
		return this.pitchAngle;
	}
	
	public float getYawAngle() {
		return this.yawAngle;
	}
	
	public float getRollAngle() {
		return this.rollAngle;
	}
	
	public float getZoom() {
		return this.zoom;
	}
	
	
	public float getEarthYearDate() {
		return earthYearDate;
	}
	
	
	public void setSelectedObject(SceneObject object) {
		this.selectedObject = object;
	}
	
	public SceneObject getSelectedObject() {
		return this.selectedObject;
	}
}
