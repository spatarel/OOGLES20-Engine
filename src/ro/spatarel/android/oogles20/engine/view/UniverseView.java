package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneGraph;
import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class UniverseView {
	
	private float zoom = 0.0f;
	
	private int windowWidth = 0;
	private int windowHeight = 0;
	
	public SimpleScene simpleScene = new SimpleScene();
	public SceneGraph landscapeScene = new LandscapeScene();
	
	public void setZoom(float zoom) {
		this.zoom = zoom;
		this.updateProjectionMatrix();
	}
	
	public void setWindowSize(int width, int height) {
		this.windowWidth = width;
		this.windowHeight = height;
		this.updateProjectionMatrix();
	}
	
	private void updateProjectionMatrix() {
		if (this.zoom == 0.0f || this.windowWidth == 0 || this.windowHeight == 0) {
			return;
		}
        //this.simpleScene.setOrthographicProjection(-10.0f, 10.0f, -10.0f, 10.0f, 0.0f, 1000.0f);
		//this.landscapeScene.setOrthographicProjection(-10.0f, 10.0f, -10.0f, 10.0f, 0.0f, 1000.0f);
        
        this.simpleScene.setPerspectiveProjection(Math.min(179.9f, Math.max(0.1f, 15.0f / this.zoom)),
        		(float)this.windowWidth / this.windowHeight, 0.01f, 10000.0f);
        this.landscapeScene.setPerspectiveProjection(Math.min(179.9f, Math.max(0.1f, 15.0f / this.zoom)),
        		(float)this.windowWidth / this.windowHeight, 0.01f, 10000.0f);
        
        //this.simpleScene.setFrustumProjection(-1.0f / this.zoom, 1.0f / this.zoom,
        //		-1.0f / this.zoom, 1.0f / this.zoom, 5.0f, 15.0f);
        //this.landscapeScene.setFrustumProjection(-1.0f / this.zoom, 1.0f / this.zoom,
        //		-1.0f / this.zoom, 1.0f / this.zoom, 5.0f, 15.0f);
	}
	
	public void setPosition(float xPosition, float yPosition, float zPosition) {
    	this.simpleScene.setTranslationView(-xPosition, -yPosition, -zPosition);
	}
	
	public void setRotationViewEulerAngles(float pitchAngle, float yawAngle, float rollAngle) {
        this.simpleScene.setRotationViewEulerAngles(-pitchAngle, -yawAngle, -rollAngle);
        this.landscapeScene.setRotationViewEulerAngles(-pitchAngle, -yawAngle, -rollAngle);
	}
	
	public void setEarthYearDate(float yearDate) {
		this.simpleScene.solarSystem.tiltedAndRotatedEarth.tiltedEarth.earth.setScaling(1.5f, 1.5f, 1.5f);
		this.simpleScene.solarSystem.tiltedAndRotatedEarth.tiltedEarth.earth.setRotationEulerAngles(23.4f, 360.0f * (yearDate * 100), 0.0f);
		this.simpleScene.solarSystem.tiltedAndRotatedEarth.tiltedEarth.setRotationEulerAngles(0.0f, 360.0f * (yearDate * 10), 0.0f);
		this.simpleScene.solarSystem.tiltedAndRotatedEarth.setTranslation(0.0f, 0.0f, -5.0f);
		this.simpleScene.solarSystem.setRotationEulerAngles(0.0f, 360.0f * (yearDate * -10), 0.0f);
	}

	public void setSelectedObject(SceneObject selectedObject) {
		this.simpleScene.setSelectedObject(selectedObject);
	}
}
