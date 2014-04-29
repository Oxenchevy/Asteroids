package edu.ycp.cs496.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.ycp.cs496.asteroids.controllers.ShipController;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


public class Panel extends SurfaceView implements Callback  {
	public static float mWidth;
	public static float mHeight;


	int width;
	int height;

	Paint paint;
	Context c;
	float x;
	float y;

	float roll;
	float pitch;
	/////////////////////////////
	int fire_location_x;
	int fire_location_y;
	int clockwise_location_x;
	int clockwise_location_y;
	int counter_location_x;
	int counter_location_y;

	int angle;
	///////////////////////////
	private static final float ALPHA = 0.25f;
	Canvas Canvas;

	private ViewThread mThread;

	private Bitmap shipBitMap;
	private Bitmap ballBitMap;
	private Bitmap ClockwiseRotateBitMap;
	private Bitmap CounterRotateBitMap;
	private Bitmap fireBitMap;
	private Bitmap space;


	private int mNumSprites;
	private Paint mPaint;




	public ShipController cont;

	// TODO: Add class fields

	@SuppressLint("NewApi")
	public Panel(Context context, ShipController cont, SensorManager senors) {
		super(context);
		// TODO: Initialize class fields

		getHolder().addCallback(this);
		mPaint = new Paint();
		mThread = new ViewThread(this);     
		angle = 0;

		c = context;
		paint = new Paint();

		paint.setAntiAlias(true);
		x = 100.0f;
		y = 100.0f;

		this.cont = cont;

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;


		startLevel();

	}

	public void drawbackground(Canvas canvas)
	{
		
		
		space = BitmapFactory.decodeResource(getResources(), R.drawable.space);
		ClockwiseRotateBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotateclockwise);
		CounterRotateBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotatecounter);
		fireBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_fire);

		Bitmap resizedFirebitmap = Bitmap.createScaledBitmap(fireBitMap, fireBitMap.getWidth()/2, fireBitMap.getHeight()/2, true);
		Bitmap resizedClockwisebitmap = Bitmap.createScaledBitmap(ClockwiseRotateBitMap, ClockwiseRotateBitMap.getWidth()/2, ClockwiseRotateBitMap.getHeight()/2, true);
		Bitmap resizedCounterbitmap = Bitmap.createScaledBitmap(CounterRotateBitMap, CounterRotateBitMap.getWidth()/2, CounterRotateBitMap.getHeight()/2, true);



		fireBitMap = resizedFirebitmap;
		CounterRotateBitMap = resizedCounterbitmap;
		ClockwiseRotateBitMap = resizedClockwisebitmap;



		canvas.drawBitmap(space, -400,-400, null);			
		canvas.drawBitmap(ClockwiseRotateBitMap, ClockwiseRotateBitMap.getWidth() + 15, canvas.getHeight() - ClockwiseRotateBitMap.getHeight()-5, null);
		canvas.drawBitmap(CounterRotateBitMap, 5, canvas.getHeight() - CounterRotateBitMap.getHeight()-5, null);
		canvas.drawBitmap(fireBitMap, canvas.getWidth() - fireBitMap.getWidth(),canvas.getHeight() - fireBitMap.getHeight(), null);

		fire_location_x = canvas.getWidth() - fireBitMap.getWidth();  
		fire_location_y = canvas.getHeight() - fireBitMap.getHeight(); 
		clockwise_location_x = ClockwiseRotateBitMap.getWidth() + 15;
		clockwise_location_y = canvas.getHeight() - ClockwiseRotateBitMap.getHeight()-5;
		counter_location_x = 5;
		counter_location_y = canvas.getHeight() - CounterRotateBitMap.getHeight()-5;

	}

	@Override
	public boolean onTouchEvent(final MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// Code on finger down
			float posX = ev.getX();
			float posY = ev.getY();	

			float fx1;
			float fx2;
			float fy1;
			float fy2;

			// touch for the fire button
			fx1 = fire_location_x-10;
			fx2 = fire_location_x+100;			 
			fy1 = fire_location_y;
			fy2 =  fire_location_y+100;

			if ((posX >= fx1 && posX <= fx2) && (posY >= fy1 && posY <= fy2)) {
				// we are in the square
				//Toast.makeText(c, "hit FIRE", Toast.LENGTH_SHORT).show();
			} else {
				// we are somewhere else on the canvas
			}

			// touch for the rotate right button

			float rx1;
			float rx2;
			float ry1;
			float ry2;

			rx1 = counter_location_x-20;
			rx2 = counter_location_x+60;

			ry1 = counter_location_y;
			ry2 =  counter_location_y+100;

			if ((posX >= rx1 && posX <= rx2) && (posY >= ry1 && posY <= ry2)) {
				// we are in the square
				//Toast.makeText(c, "hit ROTATE LEFT", Toast.LENGTH_SHORT).show();
				cont.setAngle(-5);
			} else {
				// we are somewhere else on the canvas
			}

			float crx1;
			float crx2;
			float cry1;
			float cry2;

			crx1 = clockwise_location_x-20;
			crx2 = clockwise_location_x+60;			 
			cry1 = clockwise_location_y;
			cry2 =  clockwise_location_y+100;


			if ((posX >= crx1 && posX <= crx2) && (posY >= cry1 && posY <= cry2)) {
				// we are in the square
				//Toast.makeText(c, "hit ROTATE RIGHT", Toast.LENGTH_SHORT).show();
				cont.setAngle(5);
				
			} else {
				// we are somewhere else on the canvas
			}

			//Toast.makeText(c, "pos X = " + posX + " pos Y = " + posY, Toast.LENGTH_SHORT).show();

			break;
		}

		}
		  return super.onTouchEvent(ev);
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
		Random rand = new Random();

		for (int i = 0; i < level; i++){			
			/*	synchronized (mSpriteList) {
				x = rand.nextInt(width) + 1;
				mSpriteList.add(new Sprite(getResources(), (int)x, (int)y, cont));
				mNumSprites = mSpriteList.size();
			}*/
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Stop thread
		if (mThread.isAlive()) {
			mThread.setRunning(false);
		}
	}

	public void draw(Canvas canvas, long elapsed) {
		ballBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.image_asteroid_small);
		shipBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.image_ship);

		Matrix matrix = new Matrix();
		
		
		matrix.setRotate(cont.getAngle());

		 // recreate the new Bitmap
		 Bitmap resizedBitmap = Bitmap.createBitmap(shipBitMap,0, 0, shipBitMap.getWidth(), shipBitMap.getHeight(), matrix, true); 
		canvas.drawBitmap(resizedBitmap,canvas.getWidth()/2,canvas.getHeight()/2, null);

	}


	private boolean checkGameEnd() {
		// TODO: Check if ball is within hole region

		return false;
	}

	private float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}

	
		


}
