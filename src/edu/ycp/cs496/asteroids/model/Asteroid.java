package edu.ycp.cs496.asteroids.model;

import java.util.Random;

import edu.ycp.cs496.main.Panel;

public class Asteroid {

	private final int MAX_VELOCITY = 6;
	private Location location;
	//private Direction direction;
	private int hitpoints, size, radius, speed;

	// Constructor for asteroids with random attributes.
	public Asteroid() {
		location = new Location(0,0);
		//direction.genRandomDirection();

		Random rand = new Random();
		size = rand.nextInt(3)+1;
		hitpoints = size;
		radius = size;

		switch(size) {
		case 1: speed = 3; break;
		case 2: speed = 2; break;
		case 3: speed = 1; break;
		}
		
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
		hitpoints = size;
		radius = size;

		switch(size) {
		case 1: speed = 3; break;
		case 2: speed = 2; break;
		case 3: speed = 1; break;
		}
	}

	public Location getLocation() {
		return location;
	}

	/*public Direction getDirection() {
		return direction;
	}*/

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
	
	public void checkBoundary() {
		// Left/right
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
		}
	}
}