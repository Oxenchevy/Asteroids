package edu.ycp.cs496.main;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MotionEvent;

public class AsteroidView extends Activity {
	private GLSurfaceView mGLView;


	final float RAD2DEG = (180.0f/3.14159f);

	final float DEG2RAD =(3.14159f/180.0f);
	final float DEG_PER_SEC =(360.0f/60.0f);
	final float FPS_INTERVAL= 1000;
	final float  PI =3.14159f;
	final float  NUM_DIVS  = 50;
	final float SPIN  = 0.5f;
	final float  ORBIT = 0.1f;
	final float  ACCEL = 0.1f;
	final float  WARP = 0.05f;
	final float  WARPMAX  = 2.0f;
	final float  WARPMIN =  0.0f;
	final float  MAXSPEED =  0.35f;
	final float  BOOSTMAX =  0.90f;
	final float  DALPHA  = 0.01f;
	final float  ALPHAMAX =  0.9f;
	final float  DTHRUST =  0.2f;
	final float  THRUSTMAX  = 1.0f;
	final float  THRUSTBOOSTMAX = 3.0f;
	final float  BOMBSPEED =  0.35f;
	final float  DXPLODE =  2.0f;
	final float  MAXPLODE  = 24.0f;
	final float  DOFFSET  = 0.025f;
	final float  OFFSETMAX = 0.2f;
	final float  OFFSETBOOSTMAX = 0.4f;
	// Display list identifiers.
	final float  ARWING =  11f;
	final float  ASTEROIDS  = 12f;
	final float  BACKGROUND  = 13f;
	final float  BOMB= 14f;
	final float  THRUSTER =15f;
	// Axis shortcuts.
	final short X =0;
	final short Y =  1;
	final short Z =  2;
	// Texture units.
	final short SPACE =0;
	final short ASTEROID= 1;
	final short BOMBTEX =2;
	final short AST_NORM= 3;
	final short NUM_TEXTURES= 4;
	// Bump mapping units.
	final short BUMP_AST= 0;
	final short BUMP_AST_NORM =1;

	// Global rotation values
	int  roll = 0;
	int pitch = 0;
	int yaw = 0;
	double asteroid_rot = 0.0;
	double asteroid_spin = 0.0;

	// Global camera vectors
	float[] at = {0.0f,4.0f,0.0f};
	float[] eye = {1.0f,4.0f,0.0f};
	float[] up = {0.0f,1.0f,0.0f};
	float[] v = {-1.0f,0.0f,0.0f};
	double cam_pitch = 0;
	double cam_yaw = 180;
	double d_cam_yaw = 0;
	double d_cam_pitch = 0;
	float speed = 0.0f;
	float warp = 0.0f;
	boolean travel = false;
	boolean boost = false;
	boolean stealth = false;
	float alpha_offset = 0.0f;

	// Global spherical coordinate values
	float azimuth = 45.0f;
	float daz = 2.0f;
	float elevation = 45.0f;
	float del = 2.0f;
	float radius = 5.0f;
	float dr = 0.1f;
	float min_radius = 2.0f;

	double max_speed = MAXSPEED;

	// put textures here

	// Base cube vertices
	float[][] cube = {{-1.0f,-1.0f,-1.0f},{1.0f,-1.0f,-1.0f},{1.0f,-1.0f,1.0f},
			{-1.0f,-1.0f,1.0f},{-1.0f,1.0f,-1.0f},{1.0f,1.0f,-1.0f},
			{1.0f,1.0f,1.0f},{-1.0f,1.0f,1.0f}};

	// Test colors
	float[] color_blue = {0.0f,0.0f,1.0f};
	float[] color_green = {0.0f,1.0f,0.0f};
	float[] color_red = {1.0f,0.0f,0.0f};

	// Time based rendering parameters
	int fps = 30;
	float rpm = 10.0f;
	int time = 0;
	int lasttime = 0;

	// Global mouse movement variables
	int centerX, centerY;

	// Thruster variables
	float thrust_scale = 0.0f;
	float thrust_max = THRUSTMAX;
	float color_offset = 0.0f;
	float offset_max = 0.2f;

	// Bomb variables
	boolean fire_bomb = false;
	boolean bomb_explode = false;
	float explode = 0.0f;
	int ttl;
	float[] P = new float[3];
	float[] V = new float[3];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create GLSurfaceView
		mGLView = new OpenGLSurfaceView(this);
		setContentView(mGLView);
		

	}

	@Override
	protected void onPause() {
		super.onPause();
		// The following call pauses the rendering thread.
		// If your OpenGL application is memory intensive,
		// you should consider de-allocating objects that
		// consume significant memory here.
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		mGLView.onResume();
	}
	
}
