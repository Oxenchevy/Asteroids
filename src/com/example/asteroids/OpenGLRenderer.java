package com.example.asteroids;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class OpenGLRenderer implements GLSurfaceView.Renderer{

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
					"   gl_FragColor = vec4 (0.5, 0.5, 0.5, 1.0); \n" + 
					"} \n";


	private FloatBuffer mTriangleVB;
	private float[] mProjMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	private float[] mMMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];

	public float mAngle;

	
	public void init() {

		float triangleCoords[] = {
				// X, Y, Z
				/* -0.25f, -0.125f, 0,
			      0.25f, -0.125f, 0,
			      0.0f,  0.259016994f, 0	*/
				
				
				0.0f,1.0f,0.0f,
				1.0f,1.0f,0.0f,
				1.0f,-1.0f,0.0f,
				-1.0f,1.0f,0.0f,
				
			      
		}; 
		// initialize vertex Buffer for triangle  
		ByteBuffer vbb = ByteBuffer.allocateDirect(
				// (# of coordinate values * 4 bytes per float)
				triangleCoords.length * 4); 

		// Use native byte order
		vbb.order(ByteOrder.nativeOrder());

		// Create floating point buffer from byte buffer
		mTriangleVB = vbb.asFloatBuffer();

		// Add vertices to buffer
		mTriangleVB.put(triangleCoords);

		// Set the buffer to first vertex
		mTriangleVB.position(0); 
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {       
		// Set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
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



	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// Set viewport to new extents
		GLES20.glViewport(0, 0, width, height);

		// Compute aspect ratio for proper scaling
		float ratio = (float) width / height;

		// Create perspective projection matrix
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 3);

		// Set camera modelview matrix
		Matrix.setLookAtM(mVMatrix, 0, 0, 0, 2.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
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




}

