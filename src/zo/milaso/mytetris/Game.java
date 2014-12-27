package zo.milaso.mytetris;



import zo.milaso.mytetris.tetrisview.RefreshHandler;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Game extends Activity implements OnGestureListener{
	
	private tetrisview mtetrisview;
	private GestureDetector detector;
	 private static String ICICLE_KEY = "tetris-view";
	public void onCreate(Bundle saved){
		super.onCreate(saved);
		detector = new GestureDetector(this);
		setContentView(R.layout.tetris_layout);
		mtetrisview = (tetrisview)findViewById(R.id.tetris);
		//mtetrisview.setDependentViews((Button)findViewById(R.id.pause));
		mtetrisview.ininewgame();
		mtetrisview.setMode(tetrisview.RUNNING);
		 if (saved != null) {
	            Bundle map = saved.getBundle(ICICLE_KEY);
	            if (map != null) {
	                mtetrisview.restoreState(map);
	            }
		 }
		 mtetrisview.setMode(tetrisview.RUNNING);
		mRedrawHandler.sleep();
	}
	public void onPause(){
		super.onPause();
		poppause();
	}
	
	private void poppause(){
		if(mtetrisview.getGameState() != tetrisview.RUNNING) return ;
		mtetrisview.setMode(tetrisview.PAUSE);
		final AlertDialog  builder = new AlertDialog.Builder(Game.this).create();
		 builder.setCancelable(false);
       builder.show();
       Window window = builder.getWindow();
       window.setContentView(R.layout.pausedialog);
       ImageButton menu = (ImageButton) window.findViewById(R.id.menu);
       menu.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        	  Intent intent=new Intent();   
              intent.setClass(Game.this, MainActivity.class);   
              startActivity(intent);   
              Game.this.finish();   
        }
       });
       ImageButton restart =  (ImageButton) window.findViewById(R.id.restart);
       restart.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
           	  	mtetrisview.ininewgame();
           	  	mtetrisview.setMode(tetrisview.RUNNING);
           	  	builder.cancel();
           }
          });
       ImageButton keep =  (ImageButton) window.findViewById(R.id.keep);
       keep.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
        	   mtetrisview.setMode(tetrisview.RUNNING);
        	   builder.cancel();
           }
          });
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
		if(mtetrisview.getGameState() != tetrisview.RUNNING) return false;
		double dx = e2.getX()-e1.getX();
		double dy = e2.getY()-e1.getY();
		if(dy > 180 && dy > dx ){
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
	public void pause_bt(View view){
		
		poppause();
	}
	 private RefreshHandler mRedrawHandler = new RefreshHandler();

	    class RefreshHandler extends Handler {

	        @Override
	        public void handleMessage(Message msg) {
	        	if(mtetrisview.getGameState() == tetrisview.LOSE){
	        		final AlertDialog  builder = new AlertDialog.Builder(Game.this).create();
	       		 	builder.setCancelable(false);
	       		 	builder.show();
	       		 	Window window = builder.getWindow();
	       		 	window.setContentView(R.layout.gameoverdialog);
	              ImageButton menu = (ImageButton) window.findViewById(R.id.menu);
	              menu.setOnClickListener(new View.OnClickListener() {
	               public void onClick(View v) {
	               	  Intent intent=new Intent();   
	                     intent.setClass(Game.this, MainActivity.class);   
	                     startActivity(intent);   
	                     Game.this.finish();   
	               }
	              });
	              ImageButton restart =  (ImageButton) window.findViewById(R.id.restart);
	              restart.setOnClickListener(new View.OnClickListener() {
	                  public void onClick(View v) {
	                  	  	mtetrisview.ininewgame();
	                  	  	mtetrisview.setMode(tetrisview.RUNNING);
	                  	  mRedrawHandler.sleep();
	                  	  	builder.cancel();
	                  }
	                 });
	        	}
	        	if(mtetrisview.getGameState()  != tetrisview.LOSE ) mRedrawHandler.sleep();
	        }

	        public void sleep() {
	            this.removeMessages(10);
	            sendMessageDelayed(obtainMessage(10), 100);
	        }
	    };
}
