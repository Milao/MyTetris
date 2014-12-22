/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zo.milaso.mytetris;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

/**
 * TileView: a View-variant designed for handling arrays of "icons" or other drawables.
 * 
 */
public class TileView extends View {

    /**
     * Parameters controlling the size of the tiles and their range within view. Width/Height are in
     * pixels, and Drawables will be scaled to fit to these dimensions. X/Y Tile Counts are the
     * number of tiles that will be drawn.
     */

    protected static int mTileSizeX;
    protected static int mTileSizeY;
    protected long mScore = 0;
    protected static int mXTileCount =10;
    protected static int mYTileCount = 18;
    protected int nextblock ,nexcol ;
    protected static int mXOffset;
    protected static int mYOffset;
    protected static int tXOffset;
    protected static int tYOffset;
    private final Paint mPaint = new Paint();

    /**
     * A hash that maps integer handles specified by the subclasser to the drawable that will be
     * used for that reference
     */
    private Bitmap[] mTileArray;

    /**
     * A two-dimensional array of integers in which the number represents the index of the tile that
     * should be drawn at that locations
     */
    protected int[][] mTileGrid;

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniview();
    }

    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        iniview();
    }
    private void iniview(){
	  WindowManager wm = (WindowManager) getContext()
              .getSystemService(Context.WINDOW_SERVICE);
	  
      int w = wm.getDefaultDisplay().getWidth();
      int h = wm.getDefaultDisplay().getHeight();
      mTileSizeX = (int)Math.floor((double)w/960*60);
      mTileSizeY = (int)Math.floor((double)h/1600*60);
      mXOffset =(int)(63*(double) w/960);
      mYOffset =(int)( (double)h-(144*(double)h/1600+(double)mTileSizeY*18));
      tXOffset = (int)(760*(double) w/960);
      tYOffset =(int)(430*(double) h/1600);;
      mTileGrid = new int[mXTileCount][mYTileCount];
}
    /**
     * Resets all tiles to 0 (empty)
     * 
     */
    public void clearTiles() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                setTile(0, x, y);
            }
        }
    }

    /**
     * Function to set the specified Drawable as the tile for a particular integer key.
     *
     * @param key
     * @param tile
     */
    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(mTileSizeX, mTileSizeY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, mTileSizeX, mTileSizeY);
        tile.draw(canvas);

        mTileArray[key] = bitmap;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
   //    Bitmap mBackGround1  = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.background)).getBitmap();
        Bitmap mBackGround2  = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.bg)).getBitmap();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
     
        int w = wm.getDefaultDisplay().getWidth();
        int h = wm.getDefaultDisplay().getHeight();
        //canvas.drawBitmap(mBackGround1,null, new Rect(0, 0, w, h),mPaint);
        canvas.drawBitmap(mBackGround2,null, new Rect(0, 0, w, h),mPaint);
        for (int x = 0; x < mXTileCount; x += 1) {
            for (int y = 0; y < mYTileCount; y += 1) {
                if (mTileGrid[x][y] > 0) {
                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]], mXOffset + x * mTileSizeX,
                            mYOffset + y * mTileSizeY, mPaint);
                }
            }
        }
        for(int i=0;i<4;i++){
        	for(int j=0;j<4;j++){
        		if(TileStore.store[nextblock][i][j] !=0){
        			 canvas.drawBitmap(mTileArray[nexcol], tXOffset + i * mTileSizeX,
                             tYOffset + j * mTileSizeY, mPaint);
        		}
        	}
        }
        Rect targetRect = new Rect(0, (int)(180*(float)h/1600), w,    (int)(300*(float)h/1600) );  
        mPaint.setTextSize(100*(float)h/1600);
        FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();  
        int baseline = targetRect.top + (targetRect.bottom - targetRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  
        mPaint.setTextAlign(Paint.Align.CENTER);  
        canvas.drawText(Long.toString(mScore), targetRect.centerX(), baseline, mPaint);  
    }

    /**
     * Rests the internal array of Bitmaps used for drawing tiles, and sets the maximum index of
     * tiles to be inserted
     *
     * @param tilecount
     */

    public void resetTiles(int tilecount) {
        mTileArray = new Bitmap[tilecount];
    }

    /**
     * Used to indicate that a particular tile (set with loadTile and referenced by an integer)
     * should be drawn at the given x/y coordinates during the next invalidate/draw cycle.
     * 
     * @param tileindex
     * @param x
     * @param y
     */
    public void setTile(int tileindex, int x, int y) {
        mTileGrid[x][y] = tileindex;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
       iniview();
        clearTiles();
    }

}
