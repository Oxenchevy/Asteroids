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

	private static final float ALPHA = 0.25f;
	Canvas Canvas;

	private ViewThread mThread;

	private Bitmap shipBitMap;
	private Bitmap ballBitMap;
	private Bitmap ClockwiseRotateBitMap;
	private Bitmap CounterRotateBitMap;
	private Bitmap fireBitMap;
	private Bitmap space;

	private ArrayList<Sprite> mSpriteList = new ArrayList<Sprite>();
	private int mNumSprites;
	private Paint mPaint;
	

	SensorManager sensorManager;
	Sensor accelerometer; 

	int accelerometerSensor; 
	int magnetometerSensor;


	public ShipController cont;

	// TODO: Add class fields

	@SuppressLint("NewApi")
	public Panel(Context context, ShipController cont, SensorManager senors) {
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

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
	
		
		sensorManager = senors;

		magnetometerSensor = Sensor.TYPE_MAGNETIC_FIELD; 
		accelerometerSensor = Sensor.TYPE_ACCELEROMETER; 
		sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(magnetometerSensor), SensorManager.SENSOR_DELAY_UI); 
		sensorManager.registerListener(sensorEventListener,  sensorManager.getDefaultSensor(accelerometerSensor), SensorManager.SENSOR_DELAY_UI);


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
	    
	    
		canvas.drawBitmap(space, -400,-400, null);			
		canvas.drawBitmap(resizedClockwisebitmap, resizedClockwisebitmap.getWidth() + 15, canvas.getHeight() - resizedClockwisebitmap.getHeight()-5, null);
		canvas.drawBitmap(resizedCounterbitmap, 5, canvas.getHeight() - resizedCounterbitmap.getHeight()-5, null);
		canvas.drawBitmap(resizedFirebitmap, canvas.getWidth() - resizedFirebitmap.getWidth(),canvas.getHeight() - resizedFirebitmap.getHeight(), null);
		

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

	/*	canvas.drawColor(Color.BLACK);
		synchronized (mSpriteList) {
			for (Sprite sprite : mSpriteList) {
				sprite.doDraw(canvas);
			}
		}*/
		
		

		canvas.drawBitmap(shipBitMap,canvas.getWidth()/2,canvas.getHeight()/2, null);
		
	}


	private boolean checkGameEnd() {
		// TODO: Check if ball is within hole region

		return false;
	}

	private float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}

	final SensorEventListener sensorEventListener = new SensorEventListener(){
		float[] gravity; 
		float[] geomagnetic; 
		float var;
		float inclination;
		public void onSensorChanged(SensorEvent sensorEvent) {

			Context context;
			int SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 6;
			final int rotation = getResources().getConfiguration().orientation;
			float R[] = new float[9]; 
			float I[] = new float[9]; 
			float outR[] = new float[9]; 
			float orientation[] = new float[3];


			// Acquire and filter magnetic field data from device.
			if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				gravity = lowPass(sensorEvent.values.clone(), gravity);
			}

			// Acquire and filter accelerometer data from device.
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				geomagnetic = lowPass(sensorEvent.values.clone(), geomagnetic);
			}

			// As long as acquired data is valid, acquire the transformation matrix.
			if(gravity != null && geomagnetic != null){
				SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);

				inclination = (float) Math.acos(outR[8]);

				// If the device is upright (or nearly so), use unadjusted values.
				if (inclination < 25.0f || inclination > 155.0f ) {
					SensorManager.getOrientation(R, orientation); 
				} else { // Otherwise, remap the coordinates for portrait mode.
					SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
					SensorManager.getOrientation(outR, orientation); 
				}	

				// Covert from radians to degrees.
				double x180pi = 180.0 / Math.PI;


				pitch = (float)(orientation[1] * x180pi);
				roll = (float)(orientation[2] * x180pi);	
				
			
				
					float XAccel  = pitch; // pitch
					float YAccel = roll; // roll
					
	

					cont.updateShip(XAccel, YAccel);
				}
		

			//Set sensor values as acceleration

		}
		protected float[] lowPass( float[] input, float[] output) {
			if(output == null) return input;

			for(int  i = 0; i < input.length; i++) {
				output[i] = output[i] + ALPHA * (input[i] - output[i]);
			}
			return output;
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};


}
