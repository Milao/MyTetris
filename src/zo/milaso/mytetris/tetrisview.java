package zo.milaso.mytetris;


import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

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
    private long mScore = 0;
    private long mMoveDelay = 600;
	
    private int nowblock   ;
    private int nextblock ;
    private int nowcol ,nexcol;
    private int nowx,nowy;
    private long mLastMove;
    
    private static final Random RNG = new Random();
    
	public tetrisview(Context context,AttributeSet attrs) {
		super(context,attrs);
		init_tetris(context);
		// TODO Auto-generated constructor stub
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
	    	newrandomblock();
	    	newrandomblock();
	        mMoveDelay = 600;
	        mScore = 0;
	    }
	    
	    private void newrandomblock( ){
	    	nowblock = nextblock;
	    	nowcol = nexcol;
	    	nowx = 6;
	    	nowy = 0;
	    	nextblock = RNG.nextInt(28);
	    	nexcol = 1+RNG.nextInt(5);
	    }
	    
	   public  boolean move(int mx,int my){
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
					    if(newx+i <0 || newx+i >=  mXTileCount || newy+j < 0 || newy+j >= mYTileCount  || mTileGrid[newx+i][newy+j] != 0)
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
		   return isok;
	   }
	   
	   public void roll(){
		   int newblock = (nowblock+1)%4 + nowblock /4 *4;
		   for(int i=0;i<4;i++){
			   for(int j=0;j<4;j++){
				   if(TileStore.store[nowblock][i][j] != 0  ){
					    setTile(0, nowx+i, nowx+j);
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
					    setTile(nowcol, nowx+i, nowx+j);
				   }
			   }
		   }
	   }
	   


	   
	   public void setMode(int newMode){
		   int oldMode = mMode;
		   mMode = newMode;
		   if (newMode == RUNNING && oldMode != RUNNING) {
	            // hide the game instructions
	            update();
	            return;
	        }

	        Resources res = getContext().getResources();
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
	            	 }
	                mLastMove = now;
	            }
	            mRedrawHandler.sleep(mMoveDelay);
	        }
	    }
	    private void checkmap(){
	    	for(int i=0;i<mYTileCount;i++){
	    		boolean ok = true;
	    		for(int j = 0;j<mXTileCount;j++){
	    			if(mTileGrid[j][i] == 0) {
	    				ok = false;
	    				break;
	    			}
	    		}
	    		if(ok){
	    			for(int j=i;j>0;j--)
	    				for(int k =0;k<mXTileCount;k++){
	    					mTileGrid[j][k] = mTileGrid[j-1][k];
	    				}
	    			tetrisview.this.invalidate();
	    		}
	    	}
	    }
	    private void checkIsOver(){
	    	if(!move(0,0))
	    		setMode(LOSE);
	    }
}
