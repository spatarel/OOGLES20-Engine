package ro.spatarel.android.oogles20.engine.engine;

import java.util.HashMap;
import java.util.Map;

public class TextureArchive {
	
	private Map<String, Texture> textures = new HashMap<String, Texture>();
	
	public void addTexture(String textureName, Texture texture) {
		this.textures.put(textureName, texture);
	}
	
	public Texture getTexture(String textureName) {
		return this.textures.get(textureName);
	}
	
	public void removeTexture(String textureName) {
		this.textures.remove(textureName);
	}
}
