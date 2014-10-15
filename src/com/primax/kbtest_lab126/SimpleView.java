package com.primax.kbtest_lab126;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SimpleView extends View {
	private final String TAG = SimpleView.this.getClass().getSimpleName();
//	private static final boolean DEBUG = true;
	private static final boolean DEBUG = false;

	private int mScreenWidth = 1280;
	private int mScreenHeight = 800;

	private Bitmap mBitmapCache;
	private Canvas mCanvasCache;
	private Paint mPaint;

	List<Point> points = new ArrayList<Point>();

	public SimpleView(Context context) {
		super(context);
		if (DEBUG) {
			Log.d(TAG, "SimpleView(Context context)");
		}
		mBitmapCache = Bitmap.createBitmap(mScreenWidth, mScreenHeight,
				Bitmap.Config.ARGB_8888);

		mCanvasCache = new Canvas(mBitmapCache);

		mPaint = new Paint();
		//mPaint.setAlpha(0x40);
		//mPaint.setColor(Color.BLUE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setPathEffect(null);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
	}

	public SimpleView(Context context, AttributeSet attr) {
		super(context, attr);
		if (DEBUG) {
			Log.d(TAG, "SimpleView(Context context, AttributeSet attr)");
		}
		mBitmapCache = Bitmap.createBitmap(mScreenWidth, mScreenHeight,
				Bitmap.Config.ARGB_8888);

		mCanvasCache = new Canvas(mBitmapCache);

		mPaint = new Paint();
		//mPaint.setAlpha(0x40);
		//mPaint.setColor(Color.BLUE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setPathEffect(null);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
	}

	public void Construct() {
		if (DEBUG) {
			Log.d(TAG, "Construct() : mScreenWidth=" + mScreenWidth
					+ ", mScreenHeight=" + mScreenHeight);
		}

		mBitmapCache = Bitmap.createBitmap(mScreenWidth, mScreenHeight,
				Bitmap.Config.ARGB_8888);

		mCanvasCache = new Canvas(mBitmapCache);

		mPaint = new Paint();
		//mPaint.setAlpha(0x40);
		//mPaint.setColor(Color.BLUE);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setPathEffect(null);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);

	}

	public void setScreenSize(int width, int height) {
		mScreenWidth = width;
		mScreenHeight = height;
		Construct();
	}

	public void DrawPoint(List<Point> points) {
		for (Point point : points) {
			mCanvasCache.drawCircle(point.x, point.y, 5, mPaint);
		}
		invalidate();
	}

	public void DrawLine(Path path) {
		mPaint.setStrokeWidth((float) 10.0);
		mCanvasCache.drawPath(path, mPaint);
		invalidate();
	}

	// basepen
	public void DrawLine(Path path, int color, int strokeWidth) {
		mPaint.setStrokeWidth((float) strokeWidth);
		mPaint.setColor(Color.argb(255, Color.red(color), Color.green(color),
				Color.blue(color)));
		mCanvasCache.drawPath(path, mPaint);
		invalidate();
	}

	public void ClearPathView() {
		mCanvasCache.drawColor(0xffd9d9d9);
		invalidate();
	}

	public void setPaintColor(int color) {
		mPaint.setAlpha(0x40);
		mPaint.setColor(color);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(mBitmapCache, 0, 0, mPaint);
		//bro+ 20140327
		invalidate();
	}

}