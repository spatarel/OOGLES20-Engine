package ro.spatarel.android.oogles20.engine.engine;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ro.spatarel.android.oogles20.OOBuffer;
import ro.spatarel.android.oogles20.OOBufferUsage;
import ro.spatarel.android.oogles20.OOGLES20;

import android.opengl.Matrix;
import android.util.SparseArray;

public class SceneObject implements Comparable<SceneObject> {
	
	static private final float[] identityMatrix = new float[16];
	
	static private final float[] originPosition = new float[4];
	
	private final float[] tempPosition = new float[4];
	
	static private final SparseArray<SceneObject> objects = new SparseArray<SceneObject>();
	
	static private int lastIdentifier;
	
	private int identifier;
	
	
	private float[] scalingMatrix = new float[16];
	
	private float[] rotationXAxisMatrix = new float[16];
	
	private float[] rotationYAxisMatrix = new float[16];
	
	private float[] rotationZAxisMatrix = new float[16];
	
	private float[] rotationMatrix = new float[16];
	
	private float[] translationMatrix = new float[16];
	
	private float[] tempMatrix = new float[16];
	
	private float[] transformationMatrix = new float[16];
	
	
	private Set<SceneObject> containedObjects = new TreeSet<SceneObject>();
	
	
	private Map<String, float[]> attributeArrays = new HashMap<String, float[]>();
	
	private Map<String, short[]> elementArrays = new HashMap<String, short[]>();
	
	private Map<String, Integer> elementCounts = new HashMap<String, Integer>();
	
	private Map<String, OOBuffer> buffers = new HashMap<String, OOBuffer>();
	
	
	private SparseArray<String> textures2D = new SparseArray<String>();
	
	private SparseArray<String> texturesCubeMap = new SparseArray<String>();
	
	
	private Map<String, Integer> uniformsi = new HashMap<String, Integer>();
	
	private Map<String, Float> uniformsf = new HashMap<String, Float>();
	
	private Map<String, int[]> uniformsiv = new HashMap<String, int[]>();
	
	private Map<String, float[]> uniformsfv = new HashMap<String, float[]>();
	
	
	private boolean selected = false;
	
	
	static {
		Matrix.setIdentityM(SceneObject.identityMatrix, 0);
		SceneObject.originPosition[0] = 0.0f;
		SceneObject.originPosition[1] = 0.0f;
		SceneObject.originPosition[2] = 0.0f;
		SceneObject.originPosition[3] = 1.0f;
		SceneObject.lastIdentifier = 0;
	}
	
	static public SceneObject getSceneObject(int identifier) {
		return SceneObject.objects.get(identifier);
	}
	
	public SceneObject() {
		++SceneObject.lastIdentifier;
		this.identifier = SceneObject.lastIdentifier;
		SceneObject.objects.put(this.identifier, this);
		
		Matrix.setIdentityM(this.scalingMatrix, 0);
		Matrix.setIdentityM(this.rotationMatrix, 0);
		Matrix.setIdentityM(this.translationMatrix, 0);
		this.computeTransformationMatrix();
	}
	
	private void computeTransformationMatrix() {
		Matrix.multiplyMM(this.tempMatrix, 0, this.translationMatrix, 0, this.rotationMatrix, 0);
		Matrix.multiplyMM(this.transformationMatrix, 0, this.tempMatrix, 0, this.scalingMatrix, 0);
	}
	
	public void setScaling(float xAxis, float yAxis, float zAxis) {
		Matrix.scaleM(this.scalingMatrix, 0, SceneObject.identityMatrix, 0, xAxis, yAxis, zAxis);
		this.computeTransformationMatrix();
	}
	
	public void setRotationAxis(float angleDegrees, float xVector, float yVector, float zVector) {
		Matrix.setRotateM(this.rotationMatrix, 0, angleDegrees, xVector, yVector, zVector);
		this.computeTransformationMatrix();
	}
	
	public void setRotationEulerAngles(float xAxisAngleDegrees, float yAxisAngleDegrees, float zAxisAngleDegrees) {
		Matrix.setRotateM(this.rotationYAxisMatrix, 0, yAxisAngleDegrees, 0.0f, 1.0f, 0.0f);
		Matrix.setRotateM(this.rotationXAxisMatrix, 0, xAxisAngleDegrees, 1.0f, 0.0f, 0.0f);
		Matrix.setRotateM(this.rotationZAxisMatrix, 0, zAxisAngleDegrees, 0.0f, 0.0f, 1.0f);
		
		Matrix.multiplyMM(this.tempMatrix, 0, this.rotationZAxisMatrix, 0, this.rotationXAxisMatrix, 0);
		Matrix.multiplyMM(this.rotationMatrix, 0, this.tempMatrix, 0, this.rotationYAxisMatrix, 0);
		this.computeTransformationMatrix();
	}
	
	public void setTranslation(float xAxis, float yAxis, float zAxis) {
		Matrix.translateM(this.translationMatrix, 0, SceneObject.identityMatrix, 0, xAxis, yAxis, zAxis);
		this.computeTransformationMatrix();
	}
	
	
	public void addObject(SceneObject object) {
		this.containedObjects.add(object);
	}
	
	public void removeObject(SceneObject object) {
		this.containedObjects.remove(object);
	}
	
	
	public void addAttributeArray(String attributeName, float[] array) {
		this.attributeArrays.put(attributeName, array);
	}
	
	public void removeAttributeArray(String attributeName) {
		this.attributeArrays.remove(attributeName);
	}
	
	
	public void addElementArray(String elementArrayName, short[] array) {
		this.elementArrays.put(elementArrayName, array);
	}
	
	public void removeElementArray(String elementArrayName) {
		this.elementArrays.remove(elementArrayName);
	}
	
	
	public void addElementCount(String elementCountName, int elementCount) {
		this.elementCounts.put(elementCountName, elementCount);
	}
	
	public void removeElementCount(String elementCountName) {
		this.elementCounts.remove(elementCountName);
	}
	
	
	public void loadAttributeArrayToBuffer(String attributeName) {
		float[] attributeArray = this.attributeArrays.get(attributeName);
		if (attributeArray == null) {
			return;
		}
		
		FloatBuffer attributeArrayBuffer = OOGLES20.createFloatBuffer(attributeArray);
		
		OOBuffer attributeArrayOOBuffer = new OOBuffer();
		OOGLES20.arrayBuffer.bind(attributeArrayOOBuffer);
		OOGLES20.arrayBuffer.setData(attributeArrayBuffer, OOBufferUsage.STATIC_DRAW);
		OOGLES20.arrayBuffer.unbind();
		
		this.buffers.put(attributeName, attributeArrayOOBuffer);
	}
	
	public void loadAttributeArrayToBufferRecursively(String attributeName) {
		this.loadAttributeArrayToBuffer(attributeName);
		
		for (SceneObject object : this.containedObjects) {
			object.loadAttributeArrayToBufferRecursively(attributeName);
		}
	}
	
	
	public void loadElementArrayToBuffer(String elementArrayName) {
		short[] elementArray = this.elementArrays.get(elementArrayName);
		if (elementArray == null) {
			return;
		}
		
		ShortBuffer elementArrayBuffer = OOGLES20.createShortBuffer(elementArray);
		
		OOBuffer elementArrayOOBuffer = new OOBuffer();
		OOGLES20.elementArrayBuffer.bind(elementArrayOOBuffer);
		OOGLES20.elementArrayBuffer.setData(elementArrayBuffer, OOBufferUsage.STATIC_DRAW);
		OOGLES20.elementArrayBuffer.unbind();
		
		this.buffers.put(elementArrayName, elementArrayOOBuffer);
	}
	
	public void loadElementArrayToBufferRecursively(String elementArrayName) {
		this.loadElementArrayToBuffer(elementArrayName);
		
		for (SceneObject object : this.containedObjects) {
			object.loadElementArrayToBufferRecursively(elementArrayName);
		}
	}
	
	
	public OOBuffer getOOBuffer(String bufferName) {
		return this.buffers.get(bufferName);
	}
	
	
	public Integer getElementCount(String bufferName) {
		return this.elementCounts.get(bufferName);
	}
	
	
	public void drawRecursively(Program program, float[] projectionMatrix, float[] MVMatrix, SceneGraph graph,
			TextureArchive textureArchive) {
		float[] fullMVMatrix = new float[16];
		Matrix.multiplyMM(fullMVMatrix, 0, MVMatrix, 0, this.transformationMatrix, 0);
		
		program.draw(this, projectionMatrix, fullMVMatrix, graph, textureArchive);
		
		for (SceneObject object : this.containedObjects) {
			object.drawRecursively(program, projectionMatrix, fullMVMatrix, graph, textureArchive);
		}
	}
	
	
	public void unloadArrayFromBuffer(String attributeName) {
		OOBuffer attributeArrayOOBuffer = this.buffers.get(attributeName);
		if (attributeArrayOOBuffer == null) {
			return;
		}
		this.buffers.remove(attributeName);
		attributeArrayOOBuffer.delete();
	}
	
	public void unloadArrayFromBufferRecursively(String attributeName) {
		this.unloadArrayFromBuffer(attributeName);
		
		for (SceneObject object : this.containedObjects) {
			object.unloadArrayFromBufferRecursively(attributeName);
		}
	}
	
	
	public void setTexture2D(int textureUnit, String textureName) {
		this.textures2D.put(textureUnit, textureName);
	}
	
	public String getTexture2D(int textureUnit) {
		return this.textures2D.get(textureUnit);
	}
	
	public void setTextureCubeMap(int textureUnit, String textureName) {
		this.texturesCubeMap.put(textureUnit, textureName);
	}
	
	public String getTextureCubeMap(int textureUnit) {
		return this.texturesCubeMap.get(textureUnit);
	}
	
	
	public void addUniformi(String name, int value) {
		this.uniformsi.put(name, value);
	}
	
	public void addUniformf(String name, float value) {
		this.uniformsf.put(name, value);
	}
	
	public void addUniformiv(String name, int[] value) {
		this.uniformsiv.put(name, value);
	}
	
	public void addUniformfv(String name, float[] value) {
		this.uniformsfv.put(name, value);
	}
	
	public Integer getUniformi(String name) {
		return this.uniformsi.get(name);
	}
	
	public Float getUniformf(String name) {
		return this.uniformsf.get(name);
	}
	
	public int[] getUniformiv(String name) {
		return this.uniformsiv.get(name);
	}
	
	public float[] getUniformfv(String name) {
		return this.uniformsfv.get(name);
	}
	
	public void removeUniformi(String name) {
		this.uniformsi.remove(name);
	}
	
	public void removeUniformf(String name) {
		this.uniformsf.remove(name);
	}
	
	public void removeUniformiv(String name) {
		this.uniformsiv.remove(name);
	}
	
	public void removeUniformfv(String name) {
		this.uniformsfv.remove(name);
	}
	
	
	public float[] getLightSourcePosition(SceneObject lightSource) {
		if (this == lightSource) {
			Matrix.multiplyMV(this.tempPosition, 0, this.transformationMatrix, 0, SceneObject.originPosition, 0);
			return this.tempPosition;
		}
		float[] answer = null;
		for (SceneObject object : this.containedObjects) {
			answer = object.getLightSourcePosition(lightSource);
			if (answer != null) {
				Matrix.multiplyMV(this.tempPosition, 0, this.transformationMatrix, 0, answer, 0);
				answer = this.tempPosition;
				break;
			}
		}
		return answer;
	}
	
	
	public void setSelectedObject(SceneObject selectedObject) {
		if (selectedObject == this) {
			this.selected = true;
		} else {
			this.selected = false;
		}
	}
	
	public void setSelectedObjectRecursively(SceneObject selectedObject) {
		this.setSelectedObject(selectedObject);
		
		for (SceneObject object : this.containedObjects) {
			object.setSelectedObjectRecursively(selectedObject);
		}
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	
	public int getIdentifier() {
		return this.identifier;
	}
	
	@Override
	public int compareTo(SceneObject another) {
		return this.identifier - another.identifier;
	}
	
	/*
	public List<SceneObject> getCompositeObjects() {
		List<SceneObject> compositeObjects = new ArrayList<SceneObject>();
		compositeObjects.add(this);
		
		for (SceneObject object : this.containedObjects) {
			compositeObjects.addAll(object.getCompositeObjects());
		}
		
		return compositeObjects;
	}
	
	
	public int getAttributeArraySize() {
		return this.attributeArraySize;
	}
	
	public int getCompositeAttributeArraySize() {
		int compositeAttributeArraySize = this.getAttributeArraySize();
		
		for (SceneObject object : this.containedObjects) {
			compositeAttributeArraySize += object.getCompositeAttributeArraySize();
		}
		
		return compositeAttributeArraySize;
	}
	
	public List<Float> getAttributeArray(String attributeName) {
		return this.attributeArrays.get(attributeName);
	}
	
	public List<Float> getCompositeAttributeArray(String attributeName) {
		List<Float> compositeAttributeArray = new ArrayList<Float>();
		compositeAttributeArray.addAll(this.getCompositeAttributeArray(attributeName));
		
		for (SceneObject object : this.containedObjects) {
			compositeAttributeArray.addAll(object.getCompositeAttributeArray(attributeName));
		}
		
		return compositeAttributeArray;
	}
	
	public List<Integer> getElementArray(String elementArrayName) {
		return this.elementArrays.get(elementArrayName);
	}
	
	public List<Integer> getCompositeElementArray(String elementArrayName) {
		List<Integer> compositeElementArray = new ArrayList<Integer>();
		compositeElementArray.addAll(this.getElementArray(elementArrayName));
		
		for (SceneObject object : this.containedObjects) {
			compositeElementArray.addAll(object.getCompositeElementArray(elementArrayName));
		}
		
		return compositeElementArray;
	}//*/
}
