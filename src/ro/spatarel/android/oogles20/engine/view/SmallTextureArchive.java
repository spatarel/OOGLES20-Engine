package ro.spatarel.android.oogles20.engine.view;

import ro.spatarel.android.oogles20.OOTextureMagnificationFilter;
import ro.spatarel.android.oogles20.OOTextureMinificationFilter;
import ro.spatarel.android.oogles20.OOTextureWrapMode;
import ro.spatarel.android.oogles20.engine.R;
import ro.spatarel.android.oogles20.engine.engine.Texture2D;
import ro.spatarel.android.oogles20.engine.engine.TextureArchive;
import ro.spatarel.android.oogles20.engine.engine.TextureCubeMap;
import ro.spatarel.android.oogles20.engine.main.MainActivity;

public class SmallTextureArchive extends TextureArchive {
	
	public SmallTextureArchive() {
		TextureCubeMap landscape = new TextureCubeMap();
		landscape.loadNegativeXImage(MainActivity.context, R.drawable.nissi_beach_negx);
		landscape.loadNegativeYImage(MainActivity.context, R.drawable.nissi_beach_negy);
		landscape.loadNegativeZImage(MainActivity.context, R.drawable.nissi_beach_negz);
		landscape.loadPositiveXImage(MainActivity.context, R.drawable.nissi_beach_posx);
		landscape.loadPositiveYImage(MainActivity.context, R.drawable.nissi_beach_posy);
		landscape.loadPositiveZImage(MainActivity.context, R.drawable.nissi_beach_posz);
		landscape.setMinificationFilter(OOTextureMinificationFilter.LINEAR_MIPMAP_LINEAR);
		landscape.setMagnificationFilter(OOTextureMagnificationFilter.LINEAR);
		landscape.generateMipmaps();
		
		TextureCubeMap earth = new TextureCubeMap();
		earth.loadNegativeXImage(MainActivity.context, R.drawable.earth_negx);
		earth.loadNegativeYImage(MainActivity.context, R.drawable.earth_negy);
		earth.loadNegativeZImage(MainActivity.context, R.drawable.earth_negz);
		earth.loadPositiveXImage(MainActivity.context, R.drawable.earth_posx);
		earth.loadPositiveYImage(MainActivity.context, R.drawable.earth_posy);
		earth.loadPositiveZImage(MainActivity.context, R.drawable.earth_posz);
		earth.setMinificationFilter(OOTextureMinificationFilter.LINEAR_MIPMAP_LINEAR);
		earth.setMagnificationFilter(OOTextureMagnificationFilter.LINEAR);
		earth.generateMipmaps();
		
		TextureCubeMap earthDark = new TextureCubeMap();
		earthDark.loadNegativeXImage(MainActivity.context, R.drawable.earth_dark_negx);
		earthDark.loadNegativeYImage(MainActivity.context, R.drawable.earth_dark_negy);
		earthDark.loadNegativeZImage(MainActivity.context, R.drawable.earth_dark_negz);
		earthDark.loadPositiveXImage(MainActivity.context, R.drawable.earth_dark_posx);
		earthDark.loadPositiveYImage(MainActivity.context, R.drawable.earth_dark_posy);
		earthDark.loadPositiveZImage(MainActivity.context, R.drawable.earth_dark_posz);
		earthDark.setMinificationFilter(OOTextureMinificationFilter.LINEAR_MIPMAP_LINEAR);
		earthDark.setMagnificationFilter(OOTextureMagnificationFilter.LINEAR);
		earthDark.generateMipmaps();
		
		Texture2D ground = new Texture2D();
		ground.loadImage(MainActivity.context, R.drawable.bg2);
		ground.setWrapS(OOTextureWrapMode.REPEAT);
		ground.setWrapT(OOTextureWrapMode.REPEAT);
		ground.setMinificationFilter(OOTextureMinificationFilter.LINEAR_MIPMAP_LINEAR);
		ground.setMagnificationFilter(OOTextureMagnificationFilter.LINEAR);
		ground.generateMipmaps();
		
		this.addTexture("landscape", landscape);
		this.addTexture("earth", earth);
		this.addTexture("earthDark", earthDark);
		this.addTexture("ground", ground);
	}
}
