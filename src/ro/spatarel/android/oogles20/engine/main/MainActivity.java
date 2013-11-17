package ro.spatarel.android.oogles20.engine.main;

import ro.spatarel.android.oogles20.OOGLES20;
import ro.spatarel.android.oogles20.backends.LibGDXGLES20;
import ro.spatarel.android.oogles20.engine.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity {

    public static Context context;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OOGLES20.setBackEnd(new LibGDXGLES20());
        MainActivity.context = this.getApplicationContext();
        setContentView(R.layout.activity_main);
    }
}
