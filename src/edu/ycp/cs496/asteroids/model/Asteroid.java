package edu.ycp.cs496.asteroids.model;

import java.util.Random;

import android.graphics.Path.Direction;
import edu.ycp.cs496.main.Panel;

public class Asteroid {

	private final int MAX_VELOCITY = 6;
	private Location location;
	private float dx, dy; 
	private int hitpoints, size, radius, speed;
	private float theta; 
	private int smallWidth, medWidth, largeWidth; 
	// Constructor for asteroids with random attributes.
	public Asteroid(int smallWidth, int medWidth, int largeWidth) {
		location = new Location(0,0);
		//direction.genRandomDirection();

		Random rand = new Random();
		size = rand.nextInt(3)+1;
		hitpoints = size;
		
		this.smallWidth = smallWidth; 
		this.medWidth = medWidth; 
		this.largeWidth = largeWidth; 

		switch(size) {
		case 1: speed = 6; radius = smallWidth/2; break;
		case 2: speed = 4; radius = medWidth/2; break;
		case 3: speed = 2; radius = largeWidth/2; break;
		}

		theta = rand.nextInt(360); 

		dx = (float) Math.sin(Math.toRadians(theta)) * speed;
		dy = (float) Math.cos(Math.toRadians(theta)) * speed;

		int locgen = rand.nextInt(4)+1;

		switch(locgen) {
		case 1: // Spawn along the left edge of the screen.
			location.setX(0);
			location.setY(rand.nextInt((int) Panel.mHeight));
			break;
		case 2: // Spawn along the right edge of the screen.
			location.setX((int) Panel.mWidth);
			location.setY(rand.nextInt((int) Panel.mHeight));
			break;
		case 3: // Spawn along the bottom edge of the screen.
			location.setX(rand.nextInt((int) Panel.mWidth));
			location.setY(0);
			break;
		case 4: // Spawn along the top edge of the screen.
			location.setX(rand.nextInt((int) Panel.mHeight));
			location.setY((int) Panel.mHeight);
			break;
		default: break;
		}
	}

	public Asteroid(int size, Location location) {
		this.location = location;
		//direction.genRandomDirection();
		this.size = size;
		hitpoints = size * 2;
		radius = size;
		
		Random rand = new Random();
		theta = rand.nextInt(360); 

		dx = (float) Math.sin(Math.toRadians(theta)) * speed;
		dy = (float) Math.cos(Math.toRadians(theta)) * speed;

		switch(size) {
		case 1: speed = 3; break;
		case 2: speed = 2; break;
		case 3: speed = 1; break;
		}
	}

	public Location getLocation() {
		return location;
	}

	public void updateLocation(){
		int x = location.getX();
		int y = location.getY(); 

		location.setX(x + (int)dx);
		location.setY(y + (int)dy);
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public void loseHitpoint() {
		hitpoints--;
	}

	public int getSize() {
		return size;
	}

	public int getRadius() {
		return radius;
	}

	public boolean isDestroyed() {
		if(hitpoints < 1)
			return true;
		else 
			return false;
	}

	public float getDx(){

		return dx; 
	}

	public float getDy(){

		return dy; 
	}
	
	public void setDx(){

		 dx *= -1; 
	}

	public void setDy(){

		 dy *= -1;   
	}


	public void checkBoundary() {
		/*// Left/right
		if (location.getX() <= 0) {
			location.setX((int) Panel.mWidth);
			location.setY((int) Panel.mHeight - location.getY());
		} else if (location.getX() >= Panel.mWidth) {
			location.setX(0);
			location.setY((int) Panel.mHeight - location.getY()) ;		
		}

		// Top/bottom
		if (location.getY() <= 0) {
			location.setY((int) Panel.mHeight);
			location.setX((int) Panel.mWidth - location.getX());
		} else if (location.getY() >= Panel.mHeight) {
			location.setY(0);
			location.setX((int) Panel.mWidth - location.getX()) ;		
		}*/
		
		// Left or right boundary
		if (location.getX() <= 0) {
			dx *= -1;
			location.setX(0);  
		} else if (location.getX() + (radius*2) >= Panel.mWidth) {
			dx *= -1;
			location.setX(Panel.mWidth - (radius*2));
		}

		// Top or bottom boundary
		if (location.getY() <= 0) {
			dy *= -1;
			location.setY(0); 
		} else if (location.getY() + (radius*2) >= Panel.mHeight) {
			dy *= -1;
			location.setY(Panel.mHeight - (radius*2));
		}
	}
}