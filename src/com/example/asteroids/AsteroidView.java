package com.example.asteroids;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.app.Activity;
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

	public float mAngle;
	private float[] mMMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];

	private float[] mProjMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	FloatBuffer mTriangleVB;


	private int mProgram;
	private int maPositionHandle;
	private int muMVPMatrixHandle;
	private final String vertexShaderCode =  
			"uniform mat4 uMVPMatrix; \n" +
					"attribute vec4 vPosition; \n" + 
					"void main(){ \n" +
					"   gl_Position = uMVPMatrix * vPosition; \n" +
					"} \n";
	private final String fragmentShaderCode =  
			"precision mediump float; \n" + 
					"void main(){ \n" + 
					"   gl_FragColor = vec4 (0.63671875, 0.76953125, 0.22265625, 1.0); \n" + 
					"} \n";





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asteroid_view);

		// Create GLSurfaceView
		//  mGLView = new OpenGLSurfaceView(this); LOOK INTO THIS
		setContentView(mGLView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.asteroid_view, menu);
		return true;
	}
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {       
		// Set the background frame color
		GLES20.glClearColor(0.1f,0.1f,0.1f,1.0f);
		init();

		// Load/Compile shaders from shader source
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

		// Create shader program object
		mProgram = GLES20.glCreateProgram();

		// Attach vertex and fragment shader to program
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);

		// Link shaders to create shader program
		GLES20.glLinkProgram(mProgram);

		// Get vertex shader variable reference
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}

	private int loadShader(int type, String shaderCode){

		// Create shader object of appropriate type
		int shader = GLES20.glCreateShader(type); 

		// Compile shader source
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	public void init() {


	}


	public void onDrawFrame(GL10 unused) {    
		// Clear frame and depth buffers
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Select shader program
		GLES20.glUseProgram(mProgram);

		// Attaches vertex buffer to shader
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 12, mTriangleVB);
		GLES20.glEnableVertexAttribArray(maPositionHandle);

		// Compute local transformation (rotation) matrix
		Matrix.setRotateM(mMMatrix, 0, mAngle, 0, 0, 1.0f);

		// Add local transformation into modelview matrix
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);

		// Add modelview matrix to projection matrix
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

		// Pass modelview-projection matrix to shader
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		// Draw geometry
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
		GLES20.glDisableVertexAttribArray(maPositionHandle);    
	}
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPreviousX;
	private float mPreviousY;

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
