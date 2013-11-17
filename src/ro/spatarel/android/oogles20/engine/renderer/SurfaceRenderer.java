package ro.spatarel.android.oogles20.engine.renderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import ro.spatarel.android.oogles20.engine.engine.FrameRateCounter;
import ro.spatarel.android.oogles20.engine.engine.Program;
import ro.spatarel.android.oogles20.engine.engine.SceneObject;
import ro.spatarel.android.oogles20.engine.engine.TextureArchive;
import ro.spatarel.android.oogles20.engine.model.UniverseModel;
import ro.spatarel.android.oogles20.engine.view.IdentificationProgram;
import ro.spatarel.android.oogles20.engine.view.SimpleLinesProgram;
import ro.spatarel.android.oogles20.engine.view.SimpleTrianglesProgram;
import ro.spatarel.android.oogles20.engine.view.SmallTextureArchive;
import ro.spatarel.android.oogles20.engine.view.Texture2DProgram;
import ro.spatarel.android.oogles20.engine.view.TextureCubeMapMultitexturingProgram;
import ro.spatarel.android.oogles20.engine.view.TextureCubeMapProgram;
import ro.spatarel.android.oogles20.engine.view.UniverseView;
import ro.spatarel.android.oogles20.engine.BuildConfig;
import ro.spatarel.android.oogles20.OOException;
import ro.spatarel.android.oogles20.OOFramebuffer;
import ro.spatarel.android.oogles20.OOFramebufferBuffer;
import ro.spatarel.android.oogles20.OOGLES20;
import ro.spatarel.android.oogles20.OOHintMode;
import ro.spatarel.android.oogles20.OOPixelDataType;
import ro.spatarel.android.oogles20.OOPixelFormat;
import ro.spatarel.android.oogles20.OOPrecisionFormat;
import ro.spatarel.android.oogles20.OORegion;
import ro.spatarel.android.oogles20.OORenderbuffer;
import ro.spatarel.android.oogles20.OORenderbufferFormat;
import ro.spatarel.android.oogles20.OOShaderPrecisionType;
import ro.spatarel.android.oogles20.OOShaderType;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Environment;
import android.util.Log;

public class SurfaceRenderer implements Renderer {
	
    private static final String TAG = SurfaceRenderer.class.getName();
    
	private FrameRateCounter frameFateCounter = new FrameRateCounter();
	
	private TextureArchive smallTextureArchive;
	
	private UniverseModel universeModel;
	private UniverseView universeView;
	
	private Program simpleLinesProgram;
	private Program simpleTrianglesProgram;
	private Program texture2DProgram;
	private Program textureCubeMapProgram;
	private Program textureCubeMapMultitexturingProgram;
	private Program identificationProgram;
	
	private OOFramebuffer identificationFramebuffer;
	private OORenderbuffer colorRenderbuffer;
	private OORenderbuffer depthRenderbuffer;
	private OORenderbuffer stencilRenderbuffer;
	
	private boolean computeSelection = false;
	private int selectedX;
	private int selectedY;
	private SceneObject selectedObject = null;
	
	private int width;
	private int height;
	
	private boolean screenshot = true;
	
	public SurfaceRenderer(UniverseModel universeModel, UniverseView universeView) {
		this.universeModel = universeModel;
		this.universeView = universeView;
	}
	
	@Override
	public synchronized void onDrawFrame(GL10 gl) {
		try {
    		this.frameFateCounter.newFrame();
	    	if (Math.random() < 0.01) {
	    		Log.i(TAG, "FrameRate: " + this.frameFateCounter.getFrameRate());
	    	}
	    	
	    	if (this.computeSelection) {
    			OOGLES20.framebuffer.bind(this.identificationFramebuffer);
    			
    			// Clear the framebuffer.
    			// Set the background frame color
    	        OOGLES20.framebuffer.setColorClearValue(0.0f, 0.0f, 0.0f, 0.0f);
    	        OOGLES20.framebuffer.setDepthClearValue(1.0f);
    	        OOGLES20.framebuffer.clear(OOFramebufferBuffer.COLOR);
    	        OOGLES20.framebuffer.clear(OOFramebufferBuffer.DEPTH);
    	        
    			// Draw the surfaces with the identification program.
    	        OOGLES20.fragmentProcessing.enableDepthTest();
    	        OOGLES20.pixelProcessing.setDepthBufferWriteMask(true);
    	        OOGLES20.useProgram(this.identificationProgram);
    	        this.universeView.simpleScene.draw(this.identificationProgram);
    	        
                // Read the selected pixel.
    	        ByteBuffer byteBuffer = OOGLES20.createByteBuffer(1 * 1 * 4);
                OOGLES20.framebuffer.readPixels(this.selectedX, this.selectedY, 1, 1, OOPixelFormat.RGBA,
                		OOPixelDataType.UNSIGNED_BYTE, byteBuffer);
                byte[] pixels = OOGLES20.extract(byteBuffer);
                int identifier =
                		((pixels[0] & 0x0000000f) <<  0) |
                		((pixels[1] & 0x0000000f) <<  4) |
                		((pixels[2] & 0x0000000f) <<  8) |
                		((pixels[3] & 0x0000000f) << 12);
    	        OOGLES20.framebuffer.bindDefault();
    	        
    	        Log.d(TAG, "Selection: " + identifier);
    	        
    	        this.selectedObject = SceneObject.getSceneObject(identifier);
	    	}
	    	
	    	synchronized (this.universeModel) {
		    	if (this.computeSelection) {
		    		this.universeModel.setSelectedObject(this.selectedObject);
		    		this.computeSelection = false;
		    	}
		    	
	    		this.universeModel.update();
		    	this.universeView.setPosition(
		    			this.universeModel.getXPosition(),
		    			this.universeModel.getYPosition(),
		    			this.universeModel.getZPosition());
		    	this.universeView.setRotationViewEulerAngles(
		    			this.universeModel.getPitchAngle(),
		    			this.universeModel.getYawAngle(),
		    			this.universeModel.getRollAngle());
				this.universeView.setZoom(
						this.universeModel.getZoom());
		    	this.universeView.setEarthYearDate(
						this.universeModel.getEarthYearDate());
				this.universeView.setSelectedObject(
						this.universeModel.getSelectedObject());
			}
	    	
	        OOGLES20.rasterization.enablePolygonOffset();
	    	OOGLES20.rasterization.setPolygonOffset(1.0f, 2.0f);
			// Clear the framebuffer.
			// Set the background frame color
	        OOGLES20.framebuffer.setColorClearValue(0.5f, 0.5f, 0.5f, 0.0f);
	        OOGLES20.framebuffer.setDepthClearValue(1.0f);
	        OOGLES20.framebuffer.clear(OOFramebufferBuffer.COLOR);
	        OOGLES20.framebuffer.clear(OOFramebufferBuffer.DEPTH);
	        
	        // Draw the landscape with the texture cube mapping program.
	        OOGLES20.fragmentProcessing.disableDepthTest();
	        OOGLES20.pixelProcessing.setDepthBufferWriteMask(false);
	        OOGLES20.useProgram(this.textureCubeMapProgram);
	        this.universeView.landscapeScene.draw(this.textureCubeMapProgram, this.smallTextureArchive);
	        
	        // Draw the surfaces with the texture 2D mapping program.
	        OOGLES20.fragmentProcessing.enableDepthTest();
	        OOGLES20.pixelProcessing.setDepthBufferWriteMask(true);
	        OOGLES20.useProgram(this.texture2DProgram);
	        this.universeView.simpleScene.draw(this.texture2DProgram, this.smallTextureArchive);
	        
	        // Draw the surfaces with the texture cube mapping multitexturing program.
	        OOGLES20.fragmentProcessing.enableDepthTest();
	        OOGLES20.pixelProcessing.setDepthBufferWriteMask(true);
	        OOGLES20.useProgram(this.textureCubeMapMultitexturingProgram);
	        this.universeView.simpleScene.draw(this.textureCubeMapMultitexturingProgram, this.smallTextureArchive);
	        
	        // Draw the surfaces with the simple program.
	        OOGLES20.fragmentProcessing.enableDepthTest();
	        OOGLES20.pixelProcessing.setDepthBufferWriteMask(true);
	        OOGLES20.useProgram(this.simpleTrianglesProgram);
	        this.universeView.simpleScene.draw(this.simpleTrianglesProgram);
	        
	        // Draw the meshes with the simple program.
	        //OOGLES20.fragmentProcessing.disableDepthTest();
	        //OOGLES20.pixelProcessing.setDepthBufferWriteMask(false);
	        OOGLES20.fragmentProcessing.enableDepthTest();
	        OOGLES20.pixelProcessing.setDepthBufferWriteMask(true);
	        OOGLES20.useProgram(this.simpleLinesProgram);
	        this.universeView.simpleScene.draw(this.simpleLinesProgram);
	    	
	        //OOGLES20.primitiveProcessing
	        //OOGLES20.rasterization
	        //OOGLES20.fragmentProcessing
	        //OOGLES20.pixelProcessing
	        
	        if (this.screenshot) {
                int screenshotSize = this.width * this.height;
                ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
                bb.order(ByteOrder.nativeOrder());
                OOGLES20.framebuffer.readPixels(0, 0, this.width, this.height, OOPixelFormat.RGBA,
                		OOPixelDataType.UNSIGNED_BYTE, bb);
                int pixelsBuffer[] = new int[screenshotSize];
                bb.asIntBuffer().get(pixelsBuffer);
                bb = null;
                
                for (int i = 0; i < screenshotSize; ++i) {
                	pixelsBuffer[i] = ((pixelsBuffer[i] & 0xff00ff00)) | ((pixelsBuffer[i] & 0x000000ff) << 16) | ((pixelsBuffer[i] & 0x00ff0000) >> 16);
                }
                
                Bitmap bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixelsBuffer, screenshotSize - this.width, -this.width, 0, 0, this.width, this.height);
                this.screenshot = false;
                this.saveBitmap(bitmap);
            }
	        
		} catch (OOException e) {
	        if (BuildConfig.DEBUG) {
	        	e.printStackTrace();
	        	Log.d(TAG, "Exception caught!");
	        }
		}
	}
	
	private void saveBitmap(Bitmap _bitmapScaled) {
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			_bitmapScaled.compress(Bitmap.CompressFormat.PNG, 100, bytes);
	
			//you can create a new file name "test.jpg" in sdcard folder.
			File f = new File(Environment.getExternalStorageDirectory()
			                        + File.separator + "test.png");
			f.createNewFile();
			//write the bytes in file
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
	
			// remember close de FileOutput
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void setSelectionAt(int x, int y) {
		this.computeSelection = true;
		this.selectedX = x;
		this.selectedY = y;
	}
	
	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
        try {
        	OOGLES20.primitiveProcessing.setViewport(width * 1 / 10, height * 1 / 10, width * 8 / 10, height * 8 / 10);
        	OORegion region = OOGLES20.primitiveProcessing.getViewport();
        	this.width = region.width;
        	this.height = region.height;
        	
        	this.universeView.setWindowSize(this.width, this.height);
	        
	        // Adjust the framebuffer and the renderbuffers to the window size.
        	OOGLES20.renderbuffer.bind(this.colorRenderbuffer);
	        OOGLES20.renderbuffer.setStorage(OORenderbufferFormat.RGBA4, this.width, this.height);
	        OOGLES20.renderbuffer.bind(this.depthRenderbuffer);
	        OOGLES20.renderbuffer.setStorage(OORenderbufferFormat.DEPTH_COMPONENT16, this.width, this.height);
	        OOGLES20.renderbuffer.bind(this.stencilRenderbuffer);
	        OOGLES20.renderbuffer.setStorage(OORenderbufferFormat.STENCIL_INDEX8, this.width, this.height);
	        OOGLES20.renderbuffer.unbind();
	        
	        OOGLES20.framebuffer.bind(this.identificationFramebuffer);
	        OOGLES20.framebuffer.color.attachRenderbuffer(this.colorRenderbuffer);
	        OOGLES20.framebuffer.depth.attachRenderbuffer(this.depthRenderbuffer);
	        OOGLES20.framebuffer.stencil.attachRenderbuffer(this.stencilRenderbuffer);
	        OOGLES20.framebuffer.bindDefault();
		} catch (OOException e) {
	        if (BuildConfig.DEBUG) {
	        	e.printStackTrace();
	        	Log.d(TAG, "Exception caught!");
	        }
		}
    }
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
        	// Set the mipmapping hint.
        	OOGLES20.setGenerateMipmapHint(OOHintMode.NICEST);
        	
        	// Load the texture archives.
	        this.smallTextureArchive = new SmallTextureArchive();
	        
	        // Load the programs.
	        this.simpleLinesProgram = new SimpleLinesProgram();
	        this.simpleTrianglesProgram = new SimpleTrianglesProgram();
	        this.texture2DProgram = new Texture2DProgram();
	        this.textureCubeMapProgram = new TextureCubeMapProgram();
	        this.textureCubeMapMultitexturingProgram = new TextureCubeMapMultitexturingProgram();
	        this.identificationProgram = new IdentificationProgram();
	        
	        // Release the shader compiler.
	        OOGLES20.releaseCompiler();
	        
	        // Load the scenes buffers.
	        this.universeView.simpleScene.loadAttributeArrayToBuffer("aPosition");
	        this.universeView.simpleScene.loadAttributeArrayToBuffer("aTrianglesColor");
	        this.universeView.simpleScene.loadAttributeArrayToBuffer("aLinesColor");
	        this.universeView.simpleScene.loadAttributeArrayToBuffer("aTexture2DMapping");
	        this.universeView.simpleScene.loadAttributeArrayToBuffer("aTextureCubeMapping");
	        this.universeView.simpleScene.loadAttributeArrayToBuffer("aNormal");
	        this.universeView.simpleScene.loadElementArrayToBuffer("triangles");
	        this.universeView.simpleScene.loadElementArrayToBuffer("lines");
	        
	        this.universeView.landscapeScene.loadAttributeArrayToBuffer("aPosition");
	        this.universeView.landscapeScene.loadAttributeArrayToBuffer("aTextureCubeMapping");
	        this.universeView.landscapeScene.loadElementArrayToBuffer("triangles");
	        
	        // Load the framebuffer and the renderbuffers.
	        this.identificationFramebuffer = new OOFramebuffer();
	        this.colorRenderbuffer = new OORenderbuffer();
	        this.depthRenderbuffer = new OORenderbuffer();
	        this.stencilRenderbuffer = new OORenderbuffer();
	        
	        // Output the implementation constants.
	        this.outputConstants();
	        
		} catch (OOException e) {
	        if (BuildConfig.DEBUG) {
	        	e.printStackTrace();
	        	Log.d(TAG, "Exception caught!");
	        }
		}
	}

	private void outputConstants() {
		Log.i(TAG, "Fundamentals:");
		Log.i(TAG, "----------");
		Log.i(TAG, "Vendor: " + OOGLES20.implementation.getVendor());
		Log.i(TAG, "Renderer: " + OOGLES20.implementation.getRenderer());
		Log.i(TAG, "Version: " + OOGLES20.implementation.getVersion());
		Log.i(TAG, "Shading Language Version: " + OOGLES20.implementation.getShadingLanguageVersion());
		
		Log.i(TAG, "Textures:");
		Log.i(TAG, "----------");
		Log.i(TAG, "Maximum 2D Texture Size: " + OOGLES20.implementation.getMaximum2DTextureSize());
		Log.i(TAG, "Maximum Cube Map Texture Size: " + OOGLES20.implementation.getMaximumCubeMapTextureSize());
		
		Log.i(TAG, "Programs:");
		Log.i(TAG, "----------");
		Log.i(TAG, "Maximum Vertex Uniform Vectors: " + OOGLES20.implementation.getMaximumVertexUniformVectors());
		Log.i(TAG, "Maximum Fragment Uniform Vectors: " + OOGLES20.implementation.getMaximumFragmentUniformVectors());
		Log.i(TAG, "Maximum Attribute Vectors: " + OOGLES20.implementation.getMaximumAttributeVectors());
		Log.i(TAG, "Maximum Varying Vectors: " + OOGLES20.implementation.getMaximumVaryingVectors());
		Log.i(TAG, "Maximum Texture Units: " + OOGLES20.implementation.getMaximumTextureUnits());
		Log.i(TAG, "Maximum Vertex Texture Units: " + OOGLES20.implementation.getMaximumVertexTextureUnits());
		Log.i(TAG, "Maximum Fragment Texture Units: " + OOGLES20.implementation.getMaximumFragmentTextureUnits());
		
		Log.i(TAG, "Shaders:");
		Log.i(TAG, "----------");
		Log.i(TAG, "Shader Compiler Supported: " + OOGLES20.implementation.isShaderCompilerSupported());
		
		Log.i(TAG, "Renderbuffers:");
		Log.i(TAG, "----------");
		Log.i(TAG, "Maximum Renderbuffer Size: " + OOGLES20.implementation.getMaximumRenderbufferSize());
		Log.i(TAG, "Color Red Bits: " + OOGLES20.implementation.getColorRedBits());
		Log.i(TAG, "Color Green Bits: " + OOGLES20.implementation.getColorGreenBits());
		Log.i(TAG, "Color Blue Bits: " + OOGLES20.implementation.getColorBlueBits());
		Log.i(TAG, "Color Alpha Bits: " + OOGLES20.implementation.getColorAlphaBits());
		Log.i(TAG, "Depth Bits: " + OOGLES20.implementation.getDepthBits());
		Log.i(TAG, "Stencil Bits: " + OOGLES20.implementation.getStencilBits());
		Log.i(TAG, "Preffered Pixel Data Type: " + OOGLES20.implementation.getPreferredPixelDataType());
		Log.i(TAG, "Preffered Pixel Format: " + OOGLES20.implementation.getPreferredPixelFormat());
		
		Log.i(TAG, "Primitive Processing:");
		Log.i(TAG, "----------");
		Log.i(TAG, "Maximum Viewport Dimensions: " + OOGLES20.implementation.getMaximumViewportDimensions());
		
		Log.i(TAG, "Rasterization:");
		Log.i(TAG, "----------");
		Log.i(TAG, "Subpixel Bits: " + OOGLES20.implementation.getSubpixelBits());
		Log.i(TAG, "Aliased Point Sizes Range: " + OOGLES20.implementation.getAliasedPointSizesRange());
		Log.i(TAG, "Aliased Line Widths Range: " + OOGLES20.implementation.getAliasedLineWidthsRange());
		Log.i(TAG, "Multisample Buffers Count: " + OOGLES20.implementation.getMultisampleBuffersCount());
		Log.i(TAG, "Multisample Coverage Mask Size: " + OOGLES20.implementation.getMultisampleCoverageMaskSize());
		
		Log.i(TAG, "The Shading Language:");
		Log.i(TAG, "----------");
		
        OOPrecisionFormat vli =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.VERTEX,OOShaderPrecisionType.LOW_INT);
        OOPrecisionFormat vmi =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.VERTEX, OOShaderPrecisionType.MEDIUM_INT);
        OOPrecisionFormat vhi =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.VERTEX, OOShaderPrecisionType.HIGH_INT);
        
        OOPrecisionFormat vlf =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.VERTEX, OOShaderPrecisionType.LOW_FLOAT);
        OOPrecisionFormat vmf =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.VERTEX, OOShaderPrecisionType.MEDIUM_FLOAT);
        OOPrecisionFormat vhf =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.VERTEX, OOShaderPrecisionType.HIGH_FLOAT);
        
        OOPrecisionFormat fli =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.FRAGMENT, OOShaderPrecisionType.LOW_INT);
        OOPrecisionFormat fmi =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.FRAGMENT, OOShaderPrecisionType.MEDIUM_INT);
        OOPrecisionFormat fhi =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.FRAGMENT, OOShaderPrecisionType.HIGH_INT);
        
        OOPrecisionFormat flf =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.FRAGMENT, OOShaderPrecisionType.LOW_FLOAT);
        OOPrecisionFormat fmf =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.FRAGMENT, OOShaderPrecisionType.MEDIUM_FLOAT);
        OOPrecisionFormat fhf =
        		OOGLES20.implementation.getPrecisionFormat(OOShaderType.FRAGMENT, OOShaderPrecisionType.HIGH_FLOAT);
        
        Log.i(TAG, "Vertex lowp int: " + vli.rangeMinimum + " " + vli.rangeMaximum + " " + vli.precision);
        Log.i(TAG, "Vertex mediump int: " + vmi.rangeMinimum + " " + vmi.rangeMaximum + " " + vmi.precision);
        Log.i(TAG, "Vertex highp int" + vhi.rangeMinimum + " " + vhi.rangeMaximum + " " + vhi.precision);
        
        Log.i(TAG, "Vertex lowp float: " + vlf.rangeMinimum + " " + vlf.rangeMaximum + " " + vlf.precision);
        Log.i(TAG, "Vertex mediump float: " + vmf.rangeMinimum + " " + vmf.rangeMaximum + " " + vmf.precision);
        Log.i(TAG, "Vertex highp float: " + vhf.rangeMinimum + " " + vhf.rangeMaximum + " " + vhf.precision);
        
        Log.i(TAG, "Fragment lowp int: " + fli.rangeMinimum + " " + fli.rangeMaximum + " " + fli.precision);
        Log.i(TAG, "Fragment mediump int: " + fmi.rangeMinimum + " " + fmi.rangeMaximum + " " + fmi.precision);
        Log.i(TAG, "Fragment highp int: " + fhi.rangeMinimum + " " + fhi.rangeMaximum + " " + fhi.precision);
        
        Log.i(TAG, "Fragment lowp float: " + flf.rangeMinimum + " " + flf.rangeMaximum + " " + flf.precision);
        Log.i(TAG, "Fragment mediump float: " + fmf.rangeMinimum + " " + fmf.rangeMaximum + " " + fmf.precision);
        Log.i(TAG, "Fragment highp float: " + fhf.rangeMinimum + " " + fhf.rangeMaximum + " " + fhf.precision);
	}
}
