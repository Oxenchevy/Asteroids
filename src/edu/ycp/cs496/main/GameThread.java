package edu.ycp.cs496.main;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	private static final String TAG = GameThread.class.getSimpleName();
	private final static int 	MAX_FPS = 50;	
	private final static int	MAX_FRAME_SKIPS = 5;	
	private final static int	FRAME_PERIOD = 1000 / MAX_FPS;	

	private Panel mPanel;
	private SurfaceHolder mHolder;
	private boolean mRun = false;
	private long mStartTime;
	private long mElapsed;
	private int framesSkipped; 
	private int sleepTime; 
	private long countdownElapsed; 
	private long countElapsed; 
	private boolean countdown; 

	public void setRunning(boolean running) {
		this.mRun = running;
	}

	public GameThread(SurfaceHolder surfaceHolder, Panel gamePanel) {
		super();
		this.mHolder = surfaceHolder;
		this.mPanel = gamePanel;
		mElapsed = 0; 
		countdownElapsed = 0; 
	}
	
	
	@Override
	public void run() {
		Canvas canvas; 
		Log.d(TAG, "Starting game loop");

		sleepTime = 0;
	
		long start = System.currentTimeMillis(); 
		while (mRun) {
			canvas = null; 
			
			try{
				canvas = this.mHolder.lockCanvas();
				
				synchronized (mHolder) {

					mStartTime = System.currentTimeMillis();
					framesSkipped = 0;
					this.mPanel.update();
					countdownElapsed = System.currentTimeMillis(); 
					this.mPanel.countDown(countdownElapsed - start, canvas); 
					this.mPanel.render(canvas);
					// calculate how long did the cycle take
					mElapsed = System.currentTimeMillis() - mStartTime;
					// calculate sleep time
					sleepTime = (int)(FRAME_PERIOD - mElapsed);

					if (sleepTime > 0) {
						// if sleepTime > 0 we're OK
						try {
							// send the thread to sleep for a short period
							// very useful for battery saving
							Thread.sleep(sleepTime);	
						} catch (InterruptedException e) {}
					}


				}

				while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
					// we need to catch up
					this.mPanel.update(); // update without rendering
					sleepTime += FRAME_PERIOD;	// add frame period to check if in next frame
					framesSkipped++;
				}
			} finally {
				// in case of an exception the surface is not left in 
				// an inconsistent state
				if (canvas != null) {
				
					mHolder.unlockCanvasAndPost(canvas);
				}


			}	
		}
	}

}

