package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.engine.engine.SceneObject;

public class SphereObject extends SceneObject {
	
	public SphereObject() {
    	int i, j, k;
    	short k00, k01, k10, k11;
    	int latitudeResolution = 20;
    	int longitudeResolution = 20;
    	double latitudeStart = -Math.PI / 2;
    	double latitudeStop = Math.PI / 2;
    	double longitudeStart = 0;
    	double longitudeStop = 2 * Math.PI;
    	double latitudeStep = (latitudeStop - latitudeStart) / latitudeResolution;
    	double longitudeStep = (longitudeStop - longitudeStart) / longitudeResolution;
    	double latitude;
    	double longitude;
    	float x, y, z;
    	
    	int points = (latitudeResolution + 1) * (longitudeResolution + 1);
    	float[] aPosition = new float[4 * points];
    	float[] aColor = new float[4 * points];
    	float[] aTextureCubeMapping = new float[3 * points];
    	float[] aNormal = new float[3 * points];
    	short[] triangles = new short[2 * 3 * points];
    	
    	for (latitude = latitudeStart, i = 0; i <= latitudeResolution; latitude += latitudeStep, ++i) {
        	for (longitude = longitudeStart, j = 0; j <= longitudeResolution; longitude += longitudeStep, ++j) {
                k = i * (longitudeResolution + 1) + j;
        		
                x = (float)(Math.sin(longitude) * Math.cos(latitude));
        		y = (float)Math.sin(latitude);
        		z = (float)(Math.cos(longitude) * Math.cos(latitude));
                
        		aPosition[4 * k + 0] = x;
                aPosition[4 * k + 1] = y;
                aPosition[4 * k + 2] = z;
                aPosition[4 * k + 3] = 1.0f;
                
                aColor[4 * k + 0] = x;
                aColor[4 * k + 1] = y;
                aColor[4 * k + 2] = z;
                aColor[4 * k + 3] = 1.0f;
                
                aTextureCubeMapping[3 * k + 0] = x;
                aTextureCubeMapping[3 * k + 1] = y;
                aTextureCubeMapping[3 * k + 2] = z;
                
                aNormal[3 * k + 0] = x;
                aNormal[3 * k + 1] = y;
                aNormal[3 * k + 2] = z;
        	}
    	}
    	for (latitude = latitudeStart, i = 0; i < latitudeResolution; latitude += latitudeStep, ++i) {
        	for (longitude = longitudeStart, j = 0; j < longitudeResolution; longitude += longitudeStep, ++j) {
        		k = i * longitudeResolution + j;
        		k00 = (short)( i      * (longitudeResolution + 1) +  j     );
        		k01 = (short)( i      * (longitudeResolution + 1) + (j + 1));
        		k10 = (short)((i + 1) * (longitudeResolution + 1) +  j     );
        		k11 = (short)((i + 1) * (longitudeResolution + 1) + (j + 1));
        		triangles[6 * k + 0] = k00;
        		triangles[6 * k + 1] = k01;
        		triangles[6 * k + 2] = k10;
        		triangles[6 * k + 3] = k01;
        		triangles[6 * k + 4] = k10;
        		triangles[6 * k + 5] = k11;
        	}
    	}
    	
		this.addAttributeArray("aPosition", aPosition);
		this.addAttributeArray("aTrianglesColor", aColor);
		this.addAttributeArray("aTextureCubeMapping", aTextureCubeMapping);
		this.addAttributeArray("aNormal", aNormal);
		
		this.addElementArray("triangles", triangles);
		this.addElementCount("triangles", triangles.length);
		
		this.addElementArray("lines", new short[] {
				 0, (short)(points - 1)
		});
		this.addElementCount("lines", 2);
		this.addUniformfv("aLinesColor", new float[] {1.0f, 1.0f, 1.0f, 1.0f});
	}
}
