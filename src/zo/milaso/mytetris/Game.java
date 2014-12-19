package zo.milaso.mytetris;

import android.app.Activity;
import android.os.Bundle;

public class Game extends Activity {
	
	public void onCreate(Bundle saved){
		super.onCreate(saved);
		tetrisview tetris =new tetrisview(this);
		setContentView(tetris);
	}
	/*public void onDestroy(){
		super.onDestroy();
		tetris.surfaceDestroyed(null);
	}*/

}
