package ro.spatarel.android.oogles20.engine.engine;

import android.content.Context;
import ro.spatarel.android.oogles20.OOGLES20;
import ro.spatarel.android.oogles20.OOTextureMagnificationFilter;
import ro.spatarel.android.oogles20.OOTextureMinificationFilter;
import ro.spatarel.android.oogles20.OOTextureWrapMode;

public class TextureCubeMap extends Texture {
	
	public void loadPositiveXImage(Context context, int resourceId) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
        super.loadImage(OOGLES20.textureUnit(0).texCubeMap.texPositiveX, context, resourceId);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void loadPositiveYImage(Context context, int resourceId) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
        super.loadImage(OOGLES20.textureUnit(0).texCubeMap.texPositiveY, context, resourceId);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void loadPositiveZImage(Context context, int resourceId) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
        super.loadImage(OOGLES20.textureUnit(0).texCubeMap.texPositiveZ, context, resourceId);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void loadNegativeXImage(Context context, int resourceId) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
        super.loadImage(OOGLES20.textureUnit(0).texCubeMap.texNegativeX, context, resourceId);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void loadNegativeYImage(Context context, int resourceId) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
        super.loadImage(OOGLES20.textureUnit(0).texCubeMap.texNegativeY, context, resourceId);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void loadNegativeZImage(Context context, int resourceId) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
        super.loadImage(OOGLES20.textureUnit(0).texCubeMap.texNegativeZ, context, resourceId);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void generateMipmaps() {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
		OOGLES20.textureUnit(0).texCubeMap.generateMipmaps();
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void setMagnificationFilter(OOTextureMagnificationFilter magnificationFilter) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
		OOGLES20.textureUnit(0).texCubeMap.setMagnificationFilter(magnificationFilter);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void setMinificationFilter(OOTextureMinificationFilter minificationFilter) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
		OOGLES20.textureUnit(0).texCubeMap.setMinificationFilter(minificationFilter);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void setWrapS(OOTextureWrapMode wrapS) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
		OOGLES20.textureUnit(0).texCubeMap.setWrapS(wrapS);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
	
	public void setWrapT(OOTextureWrapMode wrapT) {
		OOGLES20.textureUnit(0).texCubeMap.bind(this);
		OOGLES20.textureUnit(0).texCubeMap.setWrapT(wrapT);
		OOGLES20.textureUnit(0).texCubeMap.unbind();
	}
}
