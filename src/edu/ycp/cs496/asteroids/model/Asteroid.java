package edu.ycp.cs496.asteroids.model;

import java.util.Random;

import edu.ycp.cs496.main.Panel;



public class Asteroid {

	private final int MAX_VELOCITY = 6;
	private Location location;
	//private Direction direction;
	private int hitpoints, size, radius, speed;

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
	}

	public Asteroid(int size) {
		location = new Location(0,0);
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

	private void checkBoundary() {
		// Left or right boundary
		if (location.getX() <= 0) {
			//mDx *= -1;
			location.setX( (int) Panel.mWidth);
			location.setY((int)Panel.mHeight - location.getY());
		} else if (location.getX()  >= Panel.mWidth) {
			//mDx *= -1;
			location.setX(0);
			location.setY ((int)Panel.mHeight - location.getY()) ;		
		}

		// Top or bottom boundary
		if (location.getY() <= 0) {
			//mDx *= -1;
			location.setY ((int)Panel.mHeight);
			location.setX( (int)Panel.mWidth - location.getX());
		} else if (location.getY()  >= Panel.mHeight) {
			//mDx *= -1;
			location.setY(0);
			location.setX( (int)Panel.mWidth - location.getX()) ;		
		}
	}

}