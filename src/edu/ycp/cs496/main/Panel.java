package edu.ycp.cs496.main;

import java.util.ArrayList;

import edu.ycp.cs496.asteroids.controllers.ShipController;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

public class Panel extends SurfaceView implements Callback  {
	public static float mWidth;
	public static float mHeight;
	Paint paint;
	Context c;
	float x;
	float y;
	
	Canvas Canvas;

	private ViewThread mThread;
	
    private Bitmap shipBitMap;
    private Bitmap ballBitMap;

	private ArrayList<Sprite> mSpriteList = new ArrayList<Sprite>();
	private int mNumSprites;
	private Paint mPaint;
	
	public ShipController cont;

	// TODO: Add class fields

	public Panel(Context context, ShipController cont) {
		super(context);
		// TODO: Initialize class fields

		getHolder().addCallback(this);
		mPaint = new Paint();
		mThread = new ViewThread(this);     


		c = context;
		paint = new Paint();

		paint.setAntiAlias(true);
		x = 100.0f;
		y = 100.0f;
		
		this.cont = cont;
		
		
		startLevel();

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mWidth = width;
		mHeight = height;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// Create and start new thread
		if (!mThread.isAlive()) {
			mThread = new ViewThread(this);
			mThread.setRunning(true);
			mThread.start();
		}
	}

	public void startLevel()
	{
			
		int level = 3;
		
		double x = Math.random() * (0-500);
		double y = Math.random() * (0-500);

		for (int i = 0; i < level; i++){
			synchronized (mSpriteList) {
				mSpriteList.add(new Sprite(getResources(), (int)x, (int)y, cont));
				mNumSprites = mSpriteList.size();
			}
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Stop thread
		if (mThread.isAlive()) {
			mThread.setRunning(false);
		}
	}

	public void update(long elapsedTime) {
		// TODO: Update ball (thread safe) and check end of game
		synchronized (mSpriteList) {
			for (Sprite sprite : mSpriteList) {
				sprite.update(elapsedTime);
			}
		}
	}


	public void doDraw(Canvas canvas, long elapsed) {
 
		canvas.drawColor(Color.BLACK);
		synchronized (mSpriteList) {
			for (Sprite sprite : mSpriteList) {
				sprite.doDraw(canvas);
			}
		}
		//canvas.drawText("FPS: " + Math.round(1000f / elapsed) + " Sprites: " + mNumSprites, 10, 10, mPaint);
	}


	private boolean checkGameEnd() {
		// TODO: Check if ball is within hole region

		return false;
	}

	private float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	


}
