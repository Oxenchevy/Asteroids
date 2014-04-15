package edu.ycp.cs496.main;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class OpenGLSurfaceView extends GLSurfaceView {
	private OpenGLRenderer mRenderer;
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPreviousX;
	private float mPreviousY;

	public OpenGLSurfaceView(Context context){
		super(context);
		// Create an OpenGL ES 2.0 context.
	       // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new OpenGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}		  

	@Override 
	public boolean onTouchEvent(MotionEvent event) {

		// Get locations from event
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			// Compute swipe distance
			float dx = x - mPreviousX;
			float dy = y - mPreviousY;

			// Change direction of rotation above the mid-line
			if (y > getHeight() / 2) {
				dx = dx * -1 ;
			}

			// Change direction of rotation to left of the mid-line
			if (x < getWidth() / 2) {
				dy = dy  * -1 ;
			}

			// Update rotation angle
			mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR;

			// Render updated frame
			requestRender();
		}

		// Store current locations
		mPreviousX = x;
		mPreviousY = y;

		return true;
	} 

}
