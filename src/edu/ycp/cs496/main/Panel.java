package edu.ycp.cs496.main;


import java.util.List;

import edu.ycp.cs496.asteroids.controllers.GameController;
import edu.ycp.cs496.asteroids.model.Game;
import edu.ycp.cs496.asteroids.model.Projectile;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;



public class Panel extends SurfaceView implements Callback  {
	public static int mWidth;
	public static int mHeight;
	private MediaPlayer mediaPlayer; 
	private static final String TAG = Panel.class.getSimpleName();
	private GameThread thread; 
	private Game game; 

	private Bitmap shipBitMap;
	private Bitmap ballBitMap;
	private Bitmap cRotate;
	private Bitmap ccRotate;
	private Bitmap fire;
	private Bitmap space;


	private GameController cont;
	
	private float dThetaR = 10.0f;
	private float dThetaL = 10.0f; 
	private boolean rotate; 
	private int clockwiseX; 
	private int clockwiseY; 
	private int fireX; 
	private int fireY; 
	private int counterX; 
	private int counterY; 
	private static final int BUFFER = 100;
	private float pressure; 
	private long fireTime; 
	private ButtonType button;
	private Object[] projectiles; 
	// TODO: Add class fields

	@SuppressLint("NewApi")
	public Panel(Context context) {
		super(context);
		// TODO: Initialize class fields

		getHolder().addCallback(this);


		// create the game loop thread
		thread = new GameThread(getHolder(), this);

		//Get Screen Dimensions
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point(); 
		display.getSize(size); 
		mWidth = size.x; 
		mHeight = size.y; 


		//Initialize Bitmaps
		space = BitmapFactory.decodeResource(getResources(), R.drawable.space);
		space = Bitmap.createScaledBitmap(space, mWidth, mHeight, true);
		cRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotateclockwise);
		ccRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotatecounter);
		fire = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_fire);
		shipBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.image_ship);
		shipBitMap = Bitmap.createScaledBitmap(shipBitMap, shipBitMap.getWidth() * 2, shipBitMap.getHeight() * 2, true); 

		//Create Models and Controllers
		game = new Game(mWidth, mHeight); 
		cont = new GameController(game); 

		//Make the Panel focusable so it can handle events
		setFocusable(true);

	
		rotate = false; 

		//Set button locations
		clockwiseX = cRotate.getWidth() + 150; 
		clockwiseY = mHeight - cRotate.getHeight()-5; 
		counterX = 50; 
		counterY = mHeight - ccRotate.getHeight() - 5; 
		fireX = mWidth - fire.getWidth(); 
		fireY = mHeight - fire.getHeight(); 
		fireTime = System.currentTimeMillis(); 
		
		
        
	}
	
	

	@Override
	public boolean onTouchEvent(MotionEvent ev){
		 
		float x = ev.getX(); 
		float y = ev.getY(); 
		pressure = 1; //ev.getPressure() * 2; 
		
		if(buttonHits(x, y) == ButtonType.CLOCKWISE){
			rotate = true;
			button = ButtonType.CLOCKWISE; 
		}

		if(buttonHits(x, y) == ButtonType.COUNTERCLOCKWISE){
			rotate = true; 
			button = ButtonType.COUNTERCLOCKWISE;
		}

		if(buttonHits(x, y) == ButtonType.FIRE){
			Log.d(TAG, "FIRE!"); 
			button = ButtonType.FIRE;
			fire(); 
		}

		if(buttonHits(x, y) == ButtonType.NONE){
			//Log.d(TAG, "BLEH"); 
			button = ButtonType.NONE;
		}


		if(ev.getAction() == MotionEvent.ACTION_UP){
			rotate = false;
			button = ButtonType.NONE;
			Log.d(TAG, "UP!"); 
		}
		return true; 

	}



	public ButtonType buttonHits(float x, float y)
	{
		float fx1;
		float fx2;
		float fy1;
		float fy2;

		// touch for the fire button
		fx1 = fireX - BUFFER;
		fx2 = fireX + fire.getWidth() + BUFFER;			 
		fy1 = fireY;
		fy2 =  fireY + fire.getHeight();

		if ((x >= fx1 && x <= fx2) && (y >= fy1 && y <= fy2)) {

			return ButtonType.FIRE; 
		} 

		// touch for the rotate right button

		float rx1;
		float rx2;
		float ry1;
		float ry2;

		rx1 = counterX - BUFFER;
		rx2 = counterX + BUFFER;

		ry1 = counterY;
		ry2 =  counterY + ccRotate.getHeight();

		if ((x >= rx1 && x <= rx2) && (y >= ry1 && y <= ry2)) {

			return ButtonType.COUNTERCLOCKWISE; 
		} 


		float crx1;
		float crx2;
		float cry1;
		float cry2;

		crx1 = clockwiseX - BUFFER;
		crx2 = clockwiseX + BUFFER;			 
		cry1 = clockwiseY;
		cry2 =  clockwiseY + cRotate.getHeight();


		if ((x >= crx1 && x <= crx2) && (y >= cry1 && y <= cry2)) {

			return ButtonType.CLOCKWISE; 
		}

		return ButtonType.NONE;
	}
	
	public void fire(){
		if(System.currentTimeMillis() - fireTime > 100){
			cont.fire(mHeight, mWidth); 
		}
		
		fireTime = System.currentTimeMillis(); 
	}
	public void drawStartGame(Canvas canvas){

		//Draw bitmaps
		canvas.drawBitmap(space, 0, 0, new Paint());
		canvas.drawBitmap(cRotate, clockwiseX, clockwiseY, new Paint());
		canvas.drawBitmap(ccRotate, counterX, counterY, new Paint());
		canvas.drawBitmap(fire, fireX, fireY, new Paint());
		canvas.drawBitmap(RotateBitmap(shipBitMap, cont.getRotation()), mWidth/2, mHeight/2, new Paint());
	}
	
	public void render(Canvas canvas) {
		drawStartGame(canvas); 
		
		if(rotate){
			canvas.drawBitmap(RotateBitmap(shipBitMap, cont.getRotation()), mWidth/2, mHeight/2, new Paint());
		}
		
		Paint paint = new Paint(); 
		paint.setColor(Color.RED); 
		for(int i = 0; i < projectiles.length; i++){
			canvas.drawCircle(((Projectile) projectiles[i]).getX(), ((Projectile) projectiles[i]).getY(), ((Projectile) projectiles[i]).getRadius(), paint); 
		}
		
	}
	
	public static Bitmap RotateBitmap(Bitmap source, float angle)
	{
		Matrix matrix = new Matrix();
		matrix.postTranslate(0 , 0); 
		matrix.postRotate(angle);
		matrix.postTranslate(mWidth/2, mHeight/2); 
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}


	public void update() {
		
		if(button == ButtonType.CLOCKWISE){
			cont.rotateShip(dThetaR * pressure); 
		}
		
		else if(button == ButtonType.COUNTERCLOCKWISE){
			cont.rotateShip(-dThetaL * pressure); 
		}
		
		
			cont.updateProjectiles(mWidth, mHeight); 
			projectiles = cont.getProjectileCoords(); 
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	private static enum ButtonType{
		CLOCKWISE, COUNTERCLOCKWISE, FIRE, NONE
	}; 
}
