package edu.ycp.cs496.main;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ViewThread extends Thread {
	   private Panel mPanel;
	   private SurfaceHolder mHolder;
	   private boolean mRun = false;
	   private long mStartTime;
	   private long mElapsed;
	   

	   public ViewThread(Panel panel) {
	      mPanel = panel;
	      mHolder = mPanel.getHolder();
	   }

	   // Set current thread state
	   public void setRunning(boolean run) {
	      mRun = run;
	   }

	   @Override
	   public void run() {
	      Canvas canvas = null;

	      // Retrieve time when thread starts
	      mStartTime = System.currentTimeMillis();
	     
	      // Thread loop
	      while (mRun) {
	         // Obtain lock on canvas object
	         canvas = mHolder.lockCanvas();
	         mPanel.drawbackground(canvas);

	         if (canvas != null) {
	            // Update state based on elapsed time 
	            mElapsed = System.currentTimeMillis() - mStartTime;
	           // mPanel.update(mElapsed);

	            // Render updated state
	         
	            mPanel.draw(canvas, mElapsed);
	            

	            // Release lock on canvas object
	            mHolder.unlockCanvasAndPost(canvas);
	         }

	         // Update start time
	         mStartTime = System.currentTimeMillis();
	      }
	   }
	}