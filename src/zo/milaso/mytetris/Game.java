package zo.milaso.mytetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class Game extends Activity implements OnGestureListener{
	
	private tetrisview mtetrisview;
	private GestureDetector detector;
	public void onCreate(Bundle saved){
		super.onCreate(saved);
		detector = new GestureDetector(this);
		setContentView(R.layout.tetris_layout);
		mtetrisview = (tetrisview)findViewById(R.id.tetris);
		mtetrisview.ininewgame();
		mtetrisview.setMode(tetrisview.RUNNING);
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return this.detector.onTouchEvent(event);
    }
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		double dx = e2.getX()-e1.getX();
		double dy = e2.getY()-e1.getY();
		if(dy > 120 && dy > dx ){
			mtetrisview.quickDrap();
		}
		else if(dx > 120){
			mtetrisview.move(1,0);
		}
		else if(dx <-120){
			mtetrisview.move(-1,00);
		}
		return false;
	}
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		mtetrisview.roll();
		return false;
	}

}
