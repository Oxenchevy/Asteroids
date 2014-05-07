package edu.ycp.cs496.main;


import java.util.List;

import edu.ycp.cs496.asteroids.controllers.AsteroidController;
import edu.ycp.cs496.asteroids.controllers.GameController;
import edu.ycp.cs496.asteroids.model.Asteroid;
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
import android.widget.Toast;



public class Panel extends SurfaceView implements Callback  {
	public static int mWidth;
	public static int mHeight;
	private MediaPlayer mediaPlayer; 
	private static final String TAG = Panel.class.getSimpleName();
	private GameThread thread; 
	private Game game; 

	Context c;



	private Bitmap shipBitMap;
	private Bitmap cRotate;
	private Bitmap ccRotate;
	private Bitmap fire;
	private Bitmap space;
	private Bitmap asteroidSmall;
	private Bitmap asteroidMedium;
	private Bitmap asteroidLarge;


	private GameController cont;
	private AsteroidController Asteroidcont;

	private float dThetaR = 5.0f;
	private float dThetaL = 5.0f; 
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
	private Object[] asteroids; 
	// TODO: Add class fields

	@SuppressLint("NewApi")
	public Panel(Context context) {
		super(context);
		// TODO: Initialize class fields

		getHolder().addCallback(this);

		c = context;
		// create the game loop thread
		thread = new GameThread(getHolder(), this);
		thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			public void uncaughtException(Thread t, Throwable e) {
				//  System.out.println(t + " throws exception: " + e);
			}
		});

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

		asteroidSmall = BitmapFactory.decodeResource(getResources(), R.drawable.image_asteroid_small);
		asteroidMedium = BitmapFactory.decodeResource(getResources(), R.drawable.image_asteroid_medium);
		asteroidLarge = BitmapFactory.decodeResource(getResources(), R.drawable.image_asteroid_large);



		//Create Models and Controllers
		game = new Game(mWidth, mHeight); 
		cont = new GameController(game); 
		Asteroidcont = new AsteroidController(asteroidSmall.getWidth(), asteroidMedium.getWidth(), asteroidLarge.getWidth());

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
			//Log.d(TAG, "FIRE!"); 
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
			//Log.d(TAG, "UP!"); 
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
		if(System.currentTimeMillis() - fireTime > 250){
			cont.fire(mHeight, mWidth); 
		}

		fireTime = System.currentTimeMillis(); 
	}

	public void drawBackground(Canvas canvas){

		//Draw Background

		canvas.drawBitmap(space, 0, 0, new Paint());

	}

	public void drawButtons(Canvas canvas){

		//Draw Buttons
		canvas.drawBitmap(cRotate, clockwiseX, clockwiseY, new Paint());
		canvas.drawBitmap(ccRotate, counterX, counterY, new Paint());
		canvas.drawBitmap(fire, fireX, fireY, new Paint());
		canvas.drawBitmap(RotateBitmap(shipBitMap, cont.getRotation()), mWidth/2 - (shipBitMap.getWidth()/2), mHeight/2 - (shipBitMap.getHeight()/2), new Paint());
	}

	public void render(Canvas canvas) {

		drawBackground(canvas);
		if(rotate){
			canvas.drawBitmap(RotateBitmap(shipBitMap, cont.getRotation()), mWidth/2 - (shipBitMap.getWidth()/2), mHeight/2 - (shipBitMap.getHeight()/2), new Paint());
		}

		Paint paint = new Paint(); 
		paint.setColor(Color.RED); 
		for(int i = 0; i < projectiles.length; i++){
			canvas.drawCircle(((Projectile) projectiles[i]).getX(), ((Projectile) projectiles[i]).getY(), ((Projectile) projectiles[i]).getRadius(), paint); 
		}

		for(int i = 0; i < Asteroidcont.getAsteroidList().length; i++){
			int size = ((Asteroid) asteroids[i]).getSize();

			if(size == 1){
				canvas.drawBitmap(asteroidSmall, ((Asteroid) asteroids[i]).getLocation().getX(), ((Asteroid) asteroids[i]).getLocation().getY(),  new Paint());
			}

			else if(size == 2){
				canvas.drawBitmap(asteroidMedium, ((Asteroid) asteroids[i]).getLocation().getX(), ((Asteroid) asteroids[i]).getLocation().getY(),  new Paint());
			}

			else{
				canvas.drawBitmap(asteroidLarge, ((Asteroid) asteroids[i]).getLocation().getX(), ((Asteroid) asteroids[i]).getLocation().getY(),  new Paint());
			}

		}

		drawButtons(canvas); 
		checkFireCollilsion();
		checkAsteroidsCollilsion();
	}



	public static Bitmap RotateBitmap(Bitmap source, float angle)
	{
		Matrix matrix = new Matrix();
		matrix.postTranslate(0 , 0); 
		matrix.postRotate(angle);
		matrix.postTranslate(mWidth/2 - (source.getWidth()/2), mHeight/2 - (source.getHeight()/2)); 
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

		Asteroidcont.update(cont.getLevel());
		asteroids = Asteroidcont.getAsteroidList(); 

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
		///Log.d(TAG, "Surface is being destroyed");
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

	public void checkFireCollilsion()
	{

		for(int i = 0; i < Asteroidcont.getAsteroidList().length; i++){
			for(int p = 0; p < projectiles.length; p++){

				double xDif = ((Asteroid) asteroids[i]).getLocation().getX() - ((Projectile) projectiles[p]).getX();
				double yDif = ((Asteroid) asteroids[i]).getLocation().getY() - ((Projectile) projectiles[p]).getY();
				double distanceSquared = xDif * xDif + yDif * yDif;



				boolean collision = distanceSquared < (((Asteroid) asteroids[i]).getRadius() + 
						((Projectile) projectiles[p]).getRadius()) * (((Asteroid) asteroids[i]).getRadius() + 
								((Projectile) projectiles[p]).getRadius());
				if(collision)
				{				
					Asteroidcont.removeAsteroid(i);		
					game.getShip().removeProjectile(p);
				}				
			}
		}
	}

	public void checkAsteroidsCollilsion()
	{
		for(int i=0; i<Asteroidcont.getAsteroidList().length; i++)
		{
			for(int j=i+1; j<Asteroidcont.getAsteroidList().length; j++)
			{

				double xDif = ((Asteroid) asteroids[i]).getLocation().getX() - ((Asteroid) asteroids[j]).getLocation().getX();
				double yDif = ((Asteroid) asteroids[i]).getLocation().getY() - ((Asteroid) asteroids[j]).getLocation().getY();
				double distanceSquared = xDif * xDif + yDif * yDif;



				boolean collision = distanceSquared < (((Asteroid) asteroids[i]).getRadius() + 
						((Asteroid) asteroids[j]).getRadius()) * (((Asteroid) asteroids[i]).getRadius() + 
								((Asteroid) asteroids[j]).getRadius());
				if(collision)
				{				
					System.out.println("Collison has occured");	
					
					((Asteroid) asteroids[i]).getLocation().setX(((Asteroid) asteroids[i]).getLocation().getX()  + 10);
					((Asteroid) asteroids[j]).getLocation().setX(((Asteroid) asteroids[j]).getLocation().getX() - 10);
					
				}				
			}
		}
	}

	private static enum ButtonType{
		CLOCKWISE, COUNTERCLOCKWISE, FIRE, NONE
	}; 
}
