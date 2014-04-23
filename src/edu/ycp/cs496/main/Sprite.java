package edu.ycp.cs496.main;

import java.util.Random;

import edu.ycp.cs496.asteroids.controllers.ShipController;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Sprite {
	private float mX;
	private float mY;
	private int mDx;
	private int mDy;
	private Bitmap ballBitMap;
	private Bitmap shipBitMap;
	
	private ShipController cont;

	// Constructor
	public Sprite(Resources res, int x, int y, ShipController cont) {
		// Get bitmap from resource file

		//  shipBitMap = BitmapFactory.decodeResource(res, R.drawable.ship);
	    ballBitMap = BitmapFactory.decodeResource(res, R.drawable.image_asteroid_small);
	    shipBitMap = BitmapFactory.decodeResource(res, R.drawable.image_ship);
		// Store upper left corner coordinates
		mX = x - ballBitMap.getWidth()/2;
		mY = y - ballBitMap.getHeight()/2;

		// Set random velocity
		Random rand = new Random();
		mDx = rand.nextInt(7) - 3;
		mDy = rand.nextInt(7) - 3;
		
		this.cont = cont;
	}



	// Update (time-based) position
	public void update(long elapsedTime) {
		mX += mDx * (elapsedTime / 20f);
		mY += mDy * (elapsedTime / 20f);
		checkBoundary();
	}

	// Collision detection
	private void checkBoundary() {
		// Left or right boundary
		if (mX <= 0) {
			mDx *= -1;
			mX = 0;
		} else if (mX + ballBitMap.getWidth() >= Panel.mWidth) {
			mDx *= -1;
			mX = Panel.mWidth - ballBitMap.getWidth();
		}

		// Top or bottom boundary
		if (mY <= 0) {
			mDy *= -1;
			mY = 0;
		} else if (mY + ballBitMap.getHeight() >= Panel.mHeight) {
			mDy *= -1;
			mY = Panel.mHeight - ballBitMap.getHeight();
		}
	}

	public void doDraw(Canvas canvas ) {
		canvas.drawBitmap(ballBitMap, mX, mY, null);
		canvas.drawBitmap(shipBitMap, cont.getShip().getx(), cont.getShip().gety(), null);

	}
}