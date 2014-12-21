package zo.milaso.mytetris;

import android.app.Activity;
import android.os.Bundle;

public class Game extends Activity {
	
	private tetrisview mtetrisview;
	
	public void onCreate(Bundle saved){
		super.onCreate(saved);
		setContentView(R.layout.tetris_layout);
		mtetrisview = (tetrisview)findViewById(R.id.tetris);
		mtetrisview.ininewgame();
		mtetrisview.setMode(tetrisview.RUNNING);
	}

}
