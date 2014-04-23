package edu.ycp.cs496.asteroids.model;

public class Ship {

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



}
