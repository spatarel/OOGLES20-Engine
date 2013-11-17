package ro.spatarel.android.oogles20.engine.renderer;

import ro.spatarel.android.oogles20.engine.model.UniverseModel;
import ro.spatarel.android.oogles20.engine.view.UniverseView;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class SurfaceView extends GLSurfaceView {
	
    private static final String TAG = SurfaceView.class.getName();
	
	UniverseView universeView = new UniverseView();
	UniverseModel universeModel = new UniverseModel();
    SurfaceRenderer universeController;
	
	public SurfaceView(Context context) {
    	super(context);
    	setEGLContextClientVersion(2);
    	//setEGLConfigChooser(new MultisampleConfigChooser());
    	setEGLConfigChooser(8, 8, 8, 8, 16, 8);
    	
        // Set the Renderer for drawing on the GLSurfaceView
    	this.universeController = new SurfaceRenderer(this.universeModel, this.universeView);
    	this.refreshUniverse();
    	this.setRenderer(this.universeController);
    }
	
    public SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    	setEGLContextClientVersion(2);
    	//setEGLConfigChooser(new MultisampleConfigChooser());
    	setEGLConfigChooser(8, 8, 8, 8, 16, 8);
    	
        // Set the Renderer for drawing on the GLSurfaceView
    	this.universeController = new SurfaceRenderer(this.universeModel, this.universeView);
    	this.refreshUniverse();
    	this.setRenderer(this.universeController);
    }
    
    private long lastTimeActionDown;
    private float lastXActionDown;
    private float lastYActionDown;
    
    private boolean isMoving = false;
    private boolean isMovingForward = false;
    private boolean isMovingBackwards = false;
    private boolean isMovingSidewaysLeft = false;
    private boolean isMovingSidewaysRight = false;
    
    private float zoom = 0.25f;
    private float pitchAngle = -25.0f;
    private float yawAngle = 0.0f;
    private float rollAngle = 0.0f;
    
    private float oldZoom;
    private float oldPitchAngle;
    private float oldYawAngle;
    private float oldRollAngle;
    
    private float referenceX, referenceY;
    private float referenceLength, referenceRollAngle;
    
    private void refreshUniverse() {
    	synchronized (this.universeModel) {
	    	this.universeModel.setMoving(this.isMovingForward, this.isMovingBackwards,
	    			this.isMovingSidewaysLeft, this.isMovingSidewaysRight);
	    	this.universeModel.setRotationViewEulerAngles(this.pitchAngle, this.yawAngle, this.rollAngle);
	    	this.universeModel.setZoom(this.zoom);
    	}
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (event.getPointerCount() == 1) {
			float newX = event.getX(0);
			float newY = event.getY(0);
    		if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
    			long newTimeActionDown = System.nanoTime();
    			if (newTimeActionDown - this.lastTimeActionDown < 500000000L &&
    					Math.abs((this.lastXActionDown - newX) / this.getWidth()) < 0.04 &&
    					Math.abs((this.lastYActionDown - newY) / this.getHeight()) < 0.04) {
        			this.lastTimeActionDown = 0;
        			
    				this.universeController.setSelectionAt((int)newX, this.getHeight() - (int)newY);
    			} else {
        			this.lastTimeActionDown = newTimeActionDown;
        			this.lastXActionDown = newX;
        			this.lastYActionDown = newY;
    			}
    			
    			this.isMoving = true;
    			this.referenceX = newX;
    			this.referenceY = newY;
    			this.oldPitchAngle = this.pitchAngle;
    			this.oldYawAngle = this.yawAngle;
    		} else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
    			this.pitchAngle = this.oldPitchAngle + 22.0f * (newY - this.referenceY) / this.getHeight() / this.zoom;
    			this.yawAngle = this.oldYawAngle + 22.0f * (newX - this.referenceX) / this.getWidth() / this.zoom;
    		} else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
    			this.isMoving = false;
        		Log.d(TAG, "Pressure: " + event.getPressure());
    		}
    	}
    	if (event.getPointerCount() == 2) {
			float newDeltaX = event.getX(1) - event.getX(0);
			float newDeltaY = event.getY(1) - event.getY(0);
			float newLength = (float)Math.sqrt(newDeltaX * newDeltaX + newDeltaY * newDeltaY);
			float newAngle = (float)(Math.atan2(newDeltaY, newDeltaX) / (2.0f * Math.PI) * 360.0f);
	    	if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
				this.isMoving = false;
    			this.referenceLength = newLength;
    			this.referenceRollAngle = newAngle;
    			this.oldRollAngle = this.rollAngle;
    			this.oldZoom = this.zoom;
	    	} else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
    			this.zoom = this.oldZoom * newLength / this.referenceLength;
    			this.rollAngle = this.oldRollAngle + (newAngle - this.referenceRollAngle);
    		} else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
    			this.isMoving = true;
    			float newX, newY;
    			if (event.getActionIndex() == 1) {
	    			newX = event.getX(0);
	    			newY = event.getY(0);
    			} else {
	    			newX = event.getX(1);
	    			newY = event.getY(1);
    			}
    			this.referenceX = newX;
    			this.referenceY = newY;
    			this.oldPitchAngle = this.pitchAngle;
    			this.oldYawAngle = this.yawAngle;
    		}
    		Log.d(TAG,
        			"Pos0: " + event.getX(0) + ", " + event.getY(0) + " " +
        			"Pos1: " + event.getX(1) + ", " + event.getY(1) + " " +
    				"Pitch Angle: " + this.pitchAngle + " " +
    				"Yaw Angle: " + this.yawAngle + " " +
    				"Roll Angle: " + this.rollAngle);
    	}
    	if (this.isMoving) {
    		if ((float)event.getY(0) / this.getHeight() >= 0.9f) {
    			this.isMovingForward = false;
    			this.isMovingBackwards = true;
    		} else if ((float)event.getY(0) / this.getHeight() <= 0.1f) {
    			this.isMovingForward = true;
    			this.isMovingBackwards = false;
    		} else {
    			this.isMovingForward = false;
    			this.isMovingBackwards = false;
    		}
    		if ((float)event.getX(0) / this.getWidth() >= 0.9f) {
    			this.isMovingSidewaysLeft = false;
    			this.isMovingSidewaysRight = true;
    		} else if ((float)event.getX(0) / this.getWidth() <= 0.1f) {
    			this.isMovingSidewaysLeft = true;
    			this.isMovingSidewaysRight = false;
    		} else {
    			this.isMovingSidewaysLeft = false;
    			this.isMovingSidewaysRight = false;
    		}
    	} else {
			this.isMovingForward = false;
			this.isMovingBackwards = false;
			this.isMovingSidewaysLeft = false;
			this.isMovingSidewaysRight = false;
    	}
    	this.refreshUniverse();
    	
    	return true;
    }
}
