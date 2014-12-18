package zo.milaso.mytetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class tetrisview extends View {
	public int Screen_width;
	public int Screen_heigh;

	public tetrisview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	protected void onDraw(Canvas canvas) {
		Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		canvas.drawBitmap(bg, 0, 0, null);
	}

}
