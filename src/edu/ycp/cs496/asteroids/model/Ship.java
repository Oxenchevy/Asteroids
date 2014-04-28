package edu.ycp.cs496.asteroids.model;



public class Ship extends User {


	private Location location;
	//private Direction direction;
	private int hitpoints, lives, radius;

	private float x;
	private float y;
	private float dx;
	private float dy;

	private float xVelocity;	
	private float yVelocity;

	public Ship()
	{
		x = 500;
		y = 400;
		dx= 0;
		dy= 0;
		xVelocity= 0;	
		yVelocity= 0;
	}

	
	public Ship(int x, int y) {
		location = new Location(x, y);
		// INITIALIZE DIRECTION
		hitpoints = 1;
		radius = 1;
		lives = 2;
	}
	
	public float getx() {
		return x;
	}

	public float gety() {
		return y;
	}
	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}


	public float getDy() {
		return dy;
	}


	public void setDy(float dy) {
		this.dy = dy;
	}


	public float getxVelocity() {
		return xVelocity;
	}


	public void setxVelocity(float xVelocity) {
		this.xVelocity = xVelocity;
	}


	public float getyVelocity() {
		return yVelocity;
	}


	public void setyVelocity(float yVelocity) {
		this.yVelocity = yVelocity;
	}

	public void update(float x, float y)
	{
		this.x = x;
		this.y = y;
	}



	public void setLocation(int x, int y) {
		location.setX(x);
		location.setY(y);
	}

	/*	public void setDirection(int dx, int dy) {
			direction.setDx(dx);
			direction.setDy(dy);
		}*/

	/*public void setDirection(int angle) {
			direction.setAngle(angle);
		}*/

	public Location getLocation() {
		return location;
	}

	/*	public Direction getDirection() {
			return direction;
		}*/

	public int getLives() {
		return lives;
	}

	public int isAlive() {
		return hitpoints;
	}

	public void loseHitpoint() {
		hitpoints--;
	}

	public void loseLife() {
		lives--;
	}

	public int getRadius() {
		return radius;
	}

}
