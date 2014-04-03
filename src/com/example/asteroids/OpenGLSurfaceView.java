package com.example.asteroids;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class OpenGLSurfaceView extends GLSurfaceView {
	   private OpenGLRenderer mRenderer;

	   public OpenGLSurfaceView(Context context){
	      super(context);
	      // Create an OpenGL ES 2.0 context.
	      setEGLContextClientVersion(2);

	      // set the mRenderer member
	      mRenderer = new OpenGLRenderer();
	      setRenderer(mRenderer);

	      // Render the view only when there is a change
	      setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	   }
	}