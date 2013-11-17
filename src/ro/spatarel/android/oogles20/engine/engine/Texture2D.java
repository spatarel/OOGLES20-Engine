package ro.spatarel.android.oogles20.engine.engine;

import android.content.Context;
import ro.spatarel.android.oogles20.OOGLES20;
import ro.spatarel.android.oogles20.OOTextureMagnificationFilter;
import ro.spatarel.android.oogles20.OOTextureMinificationFilter;
import ro.spatarel.android.oogles20.OOTextureWrapMode;

public class Texture2D extends Texture {
	
	public void loadImage(Context context, int resourceId) {
		OOGLES20.textureUnit(0).tex2D.bind(this);
        super.loadImage(OOGLES20.textureUnit(0).tex2D.tex2D, context, resourceId);
		OOGLES20.textureUnit(0).tex2D.unbind();
	}
	
	public void generateMipmaps() {
		OOGLES20.textureUnit(0).tex2D.bind(this);
		OOGLES20.textureUnit(0).tex2D.generateMipmaps();
		OOGLES20.textureUnit(0).tex2D.unbind();
	}
	
	public void setMagnificationFilter(OOTextureMagnificationFilter magnificationFilter) {
		OOGLES20.textureUnit(0).tex2D.bind(this);
		OOGLES20.textureUnit(0).tex2D.setMagnificationFilter(magnificationFilter);
		OOGLES20.textureUnit(0).tex2D.unbind();
	}
	
	public void setMinificationFilter(OOTextureMinificationFilter minificationFilter) {
		OOGLES20.textureUnit(0).tex2D.bind(this);
		OOGLES20.textureUnit(0).tex2D.setMinificationFilter(minificationFilter);
		OOGLES20.textureUnit(0).tex2D.unbind();
	}
	
	public void setWrapS(OOTextureWrapMode wrapS) {
		OOGLES20.textureUnit(0).tex2D.bind(this);
		OOGLES20.textureUnit(0).tex2D.setWrapS(wrapS);
		OOGLES20.textureUnit(0).tex2D.unbind();
	}
	
	public void setWrapT(OOTextureWrapMode wrapT) {
		OOGLES20.textureUnit(0).tex2D.bind(this);
		OOGLES20.textureUnit(0).tex2D.setWrapT(wrapT);
		OOGLES20.textureUnit(0).tex2D.unbind();
	}
}
