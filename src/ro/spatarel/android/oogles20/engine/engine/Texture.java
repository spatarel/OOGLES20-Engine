package ro.spatarel.android.oogles20.engine.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ro.spatarel.android.oogles20.OOGLES20TextureImage;
import ro.spatarel.android.oogles20.OOTexture;

public abstract class Texture extends OOTexture {
	
	void loadImage(OOGLES20TextureImage textureImageTarget, Context context, int resourceId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // No pre-scaling
        
        // Read in the resource
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        
        // Load the bitmap into the bound texture.
        textureImageTarget.setImage2D(0, bitmap);
        
        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle();
	}
}
