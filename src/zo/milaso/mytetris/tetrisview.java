package zo.milaso.mytetris;


import java.util.Random;

import android.R.color;
import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class tetrisview extends TileView {
	public int Screen_width;
	public int Screen_heigh;
	
    private int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;
    
	private static final int RED_STAR = 1;
	private static final int YELLOW_STAR =  2;
	private static final int BLUE_STAR =  3;
	private static final int GREEN_STAR =  4;
	private static final int PURPLE_STAR =  5;
	private int allclr =0 ;
    private long mTarget= 1000;
    private long mMoveDelay = 600;
	
    private int nowblock   ;
    private int nowcol;
    private int nowx,nowy;
    private long mLastMove;
    
    private Button mbon;
    
    private static final Random RNG = new Random();
    
  /*  public void setDependentViews(Button mbt) {
        pause = mbt;
    }*/
    
	public tetrisview(Context context,AttributeSet attrs) {
		super(context,attrs);
		init_tetris(context);
		// TODO Auto-generated constructor stub
	}
    public Bundle saveState() {
        Bundle map = new Bundle();
        map.putInt("nextblock", Integer.valueOf(nextblock));
        map.putInt("nexcol", Integer.valueOf(nexcol));
        map.putInt("nowblock", Integer.valueOf(nowblock));
        map.putInt("nowcol", Integer.valueOf(nowcol));
        map.putInt("nowx", Integer.valueOf(nowx));
        map.putInt("nowy", Integer.valueOf(nowy));
        map.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
        map.putLong("mScore", Long.valueOf(mScore));
        for(int i=0;i<mTileGrid.length;i++)
        	map.putIntArray("mTileGrid"+Integer.toString(i), mTileGrid[i]);
        return map;
    }
    
    public void restoreState(Bundle icicle) {
        setMode(PAUSE);
        for(int i=0;i<mTileGrid.length;i++)
        	mTileGrid[i] = icicle.getIntArray("mTileGrid"+Integer.toString(i));
        mMoveDelay = icicle.getLong("mMoveDelay");
        mScore = icicle.getLong("mScore");
        nextblock = icicle.getInt("nextblock");
        nexcol = icicle.getInt("nexcol");
        nowblock = icicle.getInt("nowblock");
        nowcol = icicle.getInt("nowcol");
        nowx = icicle.getInt("nowx");
        nowy = icicle.getInt("nowy");
    }
    
	public tetrisview(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init_tetris(context);
	}
	public void init_tetris(Context context){
		setFocusable(true);
		
		Resources r = this.getContext().getResources();
		resetTiles(6);
		loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));
		loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
		loadTile(BLUE_STAR, r.getDrawable(R.drawable.bluestar));
		loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
		loadTile(PURPLE_STAR, r.getDrawable(R.drawable.purplestar));
	}
	 private RefreshHandler mRedrawHandler = new RefreshHandler();

	    class RefreshHandler extends Handler {

	        @Override
	        public void handleMessage(Message msg) {
	            tetrisview.this.update();
	            tetrisview.this.invalidate();
	        }

	        public void sleep(long delayMillis) {
	            this.removeMessages(0);
	            sendMessageDelayed(obtainMessage(0), delayMillis);
	        }
	    };
	    
	    public void ininewgame(){
	    	clearTiles();
	    	newrandomblock();
	    	newrandomblock();
	        mMoveDelay = 600;
	        mScore = 0;
	        allclr = 0;
	        mbon.setVisibility(Button.GONE);
	    }
	    public void setDe(Button bon){
	    	mbon = bon;
	    }
	    private void newrandomblock( ){
	    	nowblock = nextblock;
	    	nowcol = nexcol;
	    	nowx = 3;
	    	nowy = 0;
	    	for(int j=3;j>=0;j--){
	    		boolean ok = true;
	    		for(int i=0;i<4;i++){
	    			if(TileStore.store[nowblock][i][j] != 0 ){
	    				ok = false;
	    			}
	    		}
	    		if(ok) nowy++;
    			else break;
	    	}
	    	nextblock = RNG.nextInt(124142)%28;
	    	nexcol = 1+RNG.nextInt(1000)%5;
	    }
	    
	   public  boolean move(int mx,int my){
		   if(mMode != RUNNING) return false;
		   int newx = nowx +mx;
		   int newy = nowy + my;
		   for(int i=0;i<4;i++){
			   for(int j=0;j<4;j++){
				   if(TileStore.store[nowblock][i][j] != 0  ){
					    setTile(0, nowx+i, nowy+j);
				   }
			   }
		   }
		   boolean isok = true;
		   for(int i=0;i<4;i++){
			   for(int j=0;j<4;j++){
				   if(TileStore.store[nowblock][i][j] != 0  ){
					    if(newx+i <0 || newx+i >=  mXTileCount || newy+j < 0 || newy+j >= mYTileCount+4  || mTileGrid[newx+i][newy+j] != 0)
					    	isok  = false;
				   }
			   }
		   }
		   if(isok){
			   nowx = newx;
			   nowy = newy;
		   }
		   for(int i=0;i<4;i++){
			   for(int j=0;j<4;j++){
				   if(TileStore.store[nowblock][i][j] != 0  ){
					    setTile(nowcol, nowx+i, nowy+j);
				   }
			   }
		   }
		   if(mx != 0) 
			   tetrisview.this.invalidate();
		   return isok;
		 
	   }
	   
	   public void roll(){
		   int newblock = (nowblock+1)%4 + nowblock /4 *4;
		   for(int i=0;i<4;i++){
			   for(int j=0;j<4;j++){
				   if(TileStore.store[nowblock][i][j] != 0  ){
					    setTile(0, nowx+i, nowy+j);
				   }
			   }
		   }
		   boolean isok = true; 
		   for(int i=0;i<4;i++){
			   for(int j=0;j<4;j++){
				   if(TileStore.store[newblock][i][j] != 0  ){
					   if(nowx+i <0 || nowx+i >=  mXTileCount || nowy+j < 0 || nowy+j >= mYTileCount  || mTileGrid[nowx+i][nowy+j] != 0)
					    	isok  = false;
				   }
			   }
		   }
		   if(isok){
			   nowblock = newblock;
		   }
		   for(int i=0;i<4;i++){
			   for(int j=0;j<4;j++){
				   if(TileStore.store[nowblock][i][j] != 0  ){
					    setTile(nowcol, nowx+i, nowy+j);
				   }
			   }
		   }
		   tetrisview.this.invalidate();
	   }

	   public void quickDrap(){
		   if(mMode != RUNNING)  return ;
		   while(move(0,1)){
			   tetrisview.this.invalidate();
		   }
	   }
	   public void bong(){
		   setMode(PAUSE);
		   quickDrap();
		   for(int i =0;i<mXTileCount;i++){
			   for(int j=mYTileCount-1;j>=0;j--){
				   if(mTileGrid[i][j+4] != 0) {
					   int co =   mTileGrid[i][j+4] ;
					   mTileGrid[i][j+4] = 0;
					   int now = j+4;
					   while(now+1 < mYTileCount+4 && mTileGrid[i][now+1] == 0) now++;
					   mTileGrid[i][now] = co;
				   }
			   }
			   tetrisview.this.invalidate();
		   }
		   
		   checkmap();
		   allclr =0 ;
		   newrandomblock();
		   mbon.setVisibility(Button.GONE);
		   setMode(RUNNING);
		   
	   }
	   
	   public void setMode(int newMode){
		   int oldMode = mMode;
		   mMode = newMode;
		   if (newMode == RUNNING && oldMode != RUNNING) {
	            // hide the game instructions
	            update();
	            return;
	        }

	        if (newMode == PAUSE) {
	        }
	        if (newMode == READY) {
	        }
	        if (newMode == LOSE) {
	        }
		   
	   }
	    public int getGameState() {
	        return mMode;
	    }
	    private void update(){
	        if (mMode == RUNNING) {
	            long now = System.currentTimeMillis();
	            if (now - mLastMove > mMoveDelay) {
	            	 if(!move(0, 1)){
	            		 checkmap();
	            		 newrandomblock();
	            		 checkIsOver();
	            		 move(0,1);
	            	 }
	                mLastMove = now;
	            }
	            mRedrawHandler.sleep(mMoveDelay);
	        }
	    }
	    private void checkmap(){
	    	int hangshu =0;
	    	for(int i=0;i<mYTileCount;i++){
	    		boolean ok = true;
	    		for(int j = 0;j<mXTileCount;j++){
	    			if(mTileGrid[j][4+i] == 0) {
	    				ok = false;
	    				break;
	    			}
	    		}
	    		if(ok){
	    			for(int j=i;j>0;j--)
	    				for(int k =0;k<mXTileCount;k++){
	    					mTileGrid[k][4+j] = mTileGrid[k][j+4-1];
	    				}
	    			tetrisview.this.invalidate();
	    			hangshu++;
	    		}
	    	}
	    	if(hangshu > 4) hangshu = 4;
	    	allclr += hangshu;
	    	if(allclr >= 1 ) mbon.setVisibility(Button.VISIBLE);
	    	mScore +=(long) Math.pow(2, hangshu-1) * 100;
	    	if(mScore >= mTarget){
	    		mMoveDelay *= 0.9;
	    		if(mTarget >= 10000)  mTarget +=10000;
	    		else                                 mTarget*=2;    
	    	}
	    }
	    private void checkIsOver(){
	    	boolean isok = true;
	    	for(int i=0;i<4;i++){
				   for(int j=0;j<mXTileCount;j++){
					   if(mTileGrid[j][i] != 0  ){
						    	isok = false;
					   }
				   }
			   }
	    	if(!isok)
	    	setMode(LOSE);
	    	return ;
	    }
}
