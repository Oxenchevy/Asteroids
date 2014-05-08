package edu.ycp.cs496.main;


import java.util.List;

import edu.ycp.cs496.asteroids.controllers.AsteroidController;
import edu.ycp.cs496.asteroids.controllers.AsteroidsSingleton;
import edu.ycp.cs496.asteroids.controllers.ProjectileController;
import edu.ycp.cs496.asteroids.controllers.ShipController;
import edu.ycp.cs496.asteroids.model.Asteroid;
import edu.ycp.cs496.asteroids.model.Game;
import android.app.Activity;
import edu.ycp.cs496.asteroids.model.Projectile;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Panel extends SurfaceView implements Callback  {
	public static int mWidth;
	public static int mHeight;
	private static final String TAG = Panel.class.getSimpleName();

	//Game Model
	private GameThread thread; 
	private Game game; 
	private Context context;

	//Media
	private SoundPool sp;
	private int soundIds[] = new int[10]; 
	private boolean loaded = false; 
	private long timeToLoad; 
	private MediaPlayer mp; 

	//Bitmaps
	private Bitmap shipBitMap;
	private Bitmap cRotate;
	private Bitmap ccRotate;
	private Bitmap fire;
	private Bitmap space;
	private Bitmap asteroidSmall;
	private Bitmap asteroidMedium;
	private Bitmap asteroidLarge;
	private Bitmap ballBitMap; 
	private Bitmap greenHealth;
	private Bitmap yellowHealth;
	private Bitmap orangeHealth;
	private Bitmap redHealth;

	Bitmap temp;

	//Controllers
	private AsteroidController asteroidCont;
	private ShipController shipCont; 
	private ProjectileController projCont; 

	//Local class fields; 
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
	private boolean countdown; 
	private boolean countSound; 
	private SparseArray<PointF> mActivePointers = new SparseArray<PointF>();

	@SuppressLint("NewApi")
	public Panel(Context context) {
		super(context);
		// TODO: Initialize class fields

		getHolder().addCallback(this);

		this.context = context;
		// create the game loop thread
		thread = new GameThread(getHolder(), this);
		thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			public void uncaughtException(Thread t, Throwable e) {
				//  System.out.println(t + " throws exception: " + e);
			}
		});

		AsteroidsSingleton.getInstance();
		AsteroidsSingleton.setThread(thread);

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
		shipBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.image_ship_symmetric);
		shipBitMap = Bitmap.createScaledBitmap(shipBitMap, shipBitMap.getWidth() * 2, shipBitMap.getHeight() * 2, true); 
		ballBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.image_projectile_red);
		ballBitMap = Bitmap.createScaledBitmap(ballBitMap, ballBitMap.getWidth() * 2, ballBitMap.getHeight() * 2, true); 
		asteroidSmall = BitmapFactory.decodeResource(getResources(), R.drawable.image_asteroid_small);
		//asteroidSmall = Bitmap.createScaledBitmap(asteroidSmall, asteroidSmall.getWidth() * 2, asteroidSmall.getHeight() * 2, true); 
		asteroidMedium = BitmapFactory.decodeResource(getResources(), R.drawable.image_asteroid_medium);
		//asteroidMedium = Bitmap.createScaledBitmap(asteroidMedium, asteroidMedium.getWidth() * 2, asteroidMedium.getHeight() * 2, true); 
		asteroidLarge = BitmapFactory.decodeResource(getResources(), R.drawable.image_asteroid_large);
		//asteroidLarge = Bitmap.createScaledBitmap(asteroidLarge, asteroidLarge.getWidth() * 2, asteroidLarge.getHeight() * 2, true); 


		greenHealth = BitmapFactory.decodeResource(getResources(), R.drawable.green_health);
		greenHealth = Bitmap.createScaledBitmap(greenHealth, greenHealth.getWidth() /4, greenHealth.getHeight() / 4, true);

		orangeHealth = BitmapFactory.decodeResource(getResources(), R.drawable.orange_health);
		orangeHealth = Bitmap.createScaledBitmap(orangeHealth, orangeHealth.getWidth() /4, orangeHealth.getHeight() / 4, true);

		yellowHealth = BitmapFactory.decodeResource(getResources(), R.drawable.yellow_health);
		yellowHealth = Bitmap.createScaledBitmap(yellowHealth, yellowHealth.getWidth() /4, yellowHealth.getHeight() / 4, true);

		redHealth = BitmapFactory.decodeResource(getResources(), R.drawable.red_health);
		redHealth = Bitmap.createScaledBitmap(redHealth, redHealth.getWidth() /4, redHealth.getHeight() / 4, true);


		temp = greenHealth;
		//Media
		/*sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0); 
		soundIds[0] = sp.load(context, R.raw.countdown, 1); 

		sp.setOnLoadCompleteListener(new OnLoadCompleteListener(){

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true; 

			}	
		}); 

		 */

		mp = MediaPlayer.create(context, R.raw.countdown);
		countSound = true; 
		//Create game model
		game = new Game(mWidth, mHeight); 

		//Set instance of game model in singleton
		AsteroidsSingleton.getInstance();
		AsteroidsSingleton.setGame(game); 
		AsteroidsSingleton.setAsteroidWidths(asteroidSmall.getWidth(), asteroidMedium.getWidth(), asteroidLarge.getWidth());

		//Create controllers
		asteroidCont = new AsteroidController(); 
		projCont = new ProjectileController();
		shipCont = new ShipController(); 

		//Make the Panel focusable so it can handle events
		setFocusable(true);
		rotate = false; 
		countdown = true; 

		//Set button locations
		clockwiseX = cRotate.getWidth() + 150; 
		clockwiseY = mHeight - cRotate.getHeight()-5; 
		counterX = 50; 
		counterY = mHeight - ccRotate.getHeight() - 5; 
		fireX = mWidth - fire.getWidth(); 
		fireY = mHeight - fire.getHeight(); 
		fireTime = System.currentTimeMillis();


	}

	public void countDown(long elapsed, Canvas canvas){

		if(elapsed < 6500 ){
			canvas.drawColor(Color.BLACK);
			if(countSound){
				mp.start(); // no need to call prepare(); create() does that for you
			}
			else{
				countSound = false; 
			}

			Paint p = new Paint(); 
			p.setColor(Color.WHITE); 
			p.setTextSize(mWidth/4); 
			p.setTextAlign(Align.CENTER); 

			if(countdown){
				int count = (int) elapsed / 1000; 
				count = 5 - count; 
				System.out.println("count " + count); 
				canvas.drawText(Integer.toString(count), mWidth/2, mHeight/2, p); 


				if(elapsed > 5000){
					canvas.drawColor(Color.BLACK);
					canvas.drawText("Go!", mWidth/2, mHeight/2, p); 
				}
			}
		}

		else{
			countdown = false; 
			mp.stop(); 
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev){

		System.out.println(ev.getPointerCount());
		int pointerIndex = ev.getActionIndex();

		// get pointer ID
		int pointerId = ev.getPointerId(pointerIndex);




		// We have a new pointer. Lets add it to the list of pointers

		PointF f = new PointF();
		f.x = ev.getX(pointerIndex);
		f.y = ev.getY(pointerIndex);
		mActivePointers.put(pointerId, f);

		int size = mActivePointers.size();

		if (size == 1)
		{
			float x = mActivePointers.get(0).x;//ev.getX(); 
			float y = mActivePointers.get(0).y; 
			pressure = 1; //ev.getPressure() * 2; 

			if(buttonHits(x, y) == ButtonType.CLOCKWISE){
				rotate = true;
				button = ButtonType.CLOCKWISE; 

				cRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotateclockwise_press);
			}

			if(buttonHits(x, y) == ButtonType.COUNTERCLOCKWISE){
				rotate = true; 
				button = ButtonType.COUNTERCLOCKWISE;

				ccRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotatecounter_press);
			}

			if(buttonHits(x, y) == ButtonType.FIRE){
				//Log.d(TAG, "FIRE!"); 
				button = ButtonType.FIRE;
				fire(); 

				fire = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_fire_press);
			}

			if(buttonHits(x, y) == ButtonType.NONE){
				//Log.d(TAG, "BLEH"); 
				button = ButtonType.NONE;
			}
			if(ev.getAction() == MotionEvent.ACTION_UP){
				rotate = false;
				button = ButtonType.NONE;
				//Log.d(TAG, "UP!"); 

				cRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotateclockwise);
				ccRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotatecounter);
				fire = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_fire);
				mActivePointers.remove(0);
				
			}

		}
		
	if (size == 2)
	{
	
		float x2 = mActivePointers.get(1).x;
		float y2 = mActivePointers.get(1).y;
		pressure = 1; //ev.getPressure() * 2; 

		if(buttonHits(x2, y2) == ButtonType.CLOCKWISE){
			rotate = true;
			button = ButtonType.CLOCKWISE; 

			cRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotateclockwise_press);
		}

		if(buttonHits(x2, y2) == ButtonType.COUNTERCLOCKWISE ){
			rotate = true; 
			button = ButtonType.COUNTERCLOCKWISE;

			ccRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotatecounter_press);
		}

		if(buttonHits(x2, y2) == ButtonType.FIRE){
			//Log.d(TAG, "FIRE!"); 
			button = ButtonType.FIRE;
			fire(); 

			fire = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_fire_press);
		}

		if(buttonHits(x2, y2) == ButtonType.NONE){
			//Log.d(TAG, "BLEH"); 
			button = ButtonType.NONE;
		}
	
			if(ev.getAction() == MotionEvent.ACTION_UP){
				rotate = false;
				button = ButtonType.NONE;
				mActivePointers.remove(0);
				mActivePointers.remove(1);
				//Log.d(TAG, "UP!"); 

				cRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotateclockwise);
				ccRotate = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_rotatecounter);
				fire = BitmapFactory.decodeResource(getResources(), R.drawable.image_button_fire);
			}
		
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
		if(System.currentTimeMillis() - fireTime > 200){
			projCont.fire(mHeight, mWidth); 
		}

		fireTime = System.currentTimeMillis(); 
	}

	public void drawBackground(Canvas canvas){

		//Draw Background
		canvas.drawBitmap(space, 0, 0, new Paint());

	}

	public void drawButtons(Canvas canvas){

		//Draw Buttons
		Paint paint = new Paint();
		canvas.drawBitmap(cRotate, clockwiseX, clockwiseY, paint);
		canvas.drawBitmap(ccRotate, counterX, counterY, paint);
		canvas.drawBitmap(fire, fireX, fireY, paint);
		canvas.drawBitmap(RotateBitmap(shipBitMap, shipCont.getRotation()), mWidth/2 - (shipBitMap.getWidth()/2), mHeight/2 - (shipBitMap.getHeight()/2), paint);

		// Draw Score
		paint.setColor(Color.WHITE); 
		paint.setTextSize(30); 
		canvas.drawText("Score: ", canvas.getWidth()-250, 25, paint); 
		canvas.drawText(Integer.toString(game.getUser().getScore()), canvas.getWidth()-150, 25, paint); 

		// Draw Health Meter

		canvas.drawText("Health: ", canvas.getWidth()-600, 25, paint);
		int buffer = 500;



		for (int i =0; i < game.getShip().getHitpoints(); i++)
		{			
			if ( game.getShip().getHitpoints() > 4 )
			{
				// use green
				temp = greenHealth;
			}

			if ( game.getShip().getHitpoints() == 3)
			{
				// use yellow  
				temp = yellowHealth;
			}
			if ( game.getShip().getHitpoints() == 2)
			{
				// use  orange
				temp = orangeHealth;
			}

			if ( game.getShip().getHitpoints() == 1)
			{
				// use  red
				temp = redHealth;
			}				

			canvas.drawBitmap(temp, canvas.getWidth()- (buffer -10),0, new Paint());
			buffer -= temp.getWidth();
		}



	}

	public void render(Canvas canvas) {

		if(!countdown){
			drawBackground(canvas);

			if(rotate){
				canvas.drawBitmap(RotateBitmap(shipBitMap, shipCont.getRotation()), mWidth/2 - (shipBitMap.getWidth()/2), mHeight/2 - (shipBitMap.getHeight()/2), new Paint());
			}


			Paint paint = new Paint(); 
			paint.setColor(Color.RED); 
			for(int i = 0; i < projectiles.length; i++){
				canvas.drawBitmap(ballBitMap, ((Projectile) projectiles[i]).getX(), ((Projectile) projectiles[i]).getY(), new Paint());  
			}


			for(int i = 0; i < asteroidCont.getAsteroidList().length; i++){
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
		}


	}


	public static Bitmap RotateBitmap(Bitmap source, float angle)
	{
		Matrix matrix = new Matrix();
		matrix.postTranslate(0 , 0); 
		matrix.postRotate(angle);
		matrix.postTranslate(mWidth/2 - (source.getWidth()/2), mHeight/2 - (source.getHeight()/2)); 
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}


	@SuppressLint("NewApi")
	public void update() {

		//if(!countdown){
		if(button == ButtonType.CLOCKWISE){
			shipCont.rotateShip(dThetaR * pressure); 
		}

		else if(button == ButtonType.COUNTERCLOCKWISE){
			shipCont.rotateShip(-dThetaL * pressure); 
		}


		projCont.updateProjectiles(mWidth, mHeight); 
		projectiles = projCont.getProjectileCoords(); 

		asteroidCont.update();
		asteroids = asteroidCont.getAsteroidList(); 

		asteroidCont.fireCollision();
		asteroidCont.asteroidCollision();
		asteroidCont.shipToAsteroidCollision();

		if(game.getShip().getHitpoints() < 1)
		{
			game.getShip().setHitpoints(6);

		}

		if (game.checkEndGame())
		{

			/*Intent gameover = new Intent (context, GameOver.class);
			context.startActivity(gameover);	*/
			Log.d("Panel", "GAME OVER");
		}

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

	private static enum ButtonType{
		CLOCKWISE, COUNTERCLOCKWISE, FIRE, NONE
	}; 
}
