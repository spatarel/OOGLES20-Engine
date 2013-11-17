package ro.spatarel.android.oogles20.engine.engine;

import java.util.LinkedList;
import java.util.Queue;

public class FrameRateCounter {
	
	static final long oneSecond = 1000000000L;
	
	private long lastFrameTime;
	
	private Queue<Long> lastFrameRates = new LinkedList<Long>();
	
	public float newFrame() {
		this.lastFrameTime = System.nanoTime();
		this.lastFrameRates.add(lastFrameTime);
		while(lastFrameTime - this.lastFrameRates.element() >= FrameRateCounter.oneSecond) {
			this.lastFrameRates.remove();
		}
		
		return getFrameRate();
	}
	
	public float getFrameRate() {
		if (this.lastFrameRates.size() >= 2) {
			return FrameRateCounter.oneSecond /
					((float)(lastFrameTime - this.lastFrameRates.peek()) / (this.lastFrameRates.size() - 1));
		} else {
			return 1.0f;
		}
	}
}
