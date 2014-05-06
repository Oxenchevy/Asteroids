package edu.ycp.cs496.asteroids.model;

public class Projectile {

	private float x;
	private float y; 
	private int radius; 
	private float theta; 
	private float speed; 
	
	public Projectile(int x, int y, float theta){
		this.x = x; 
		this.y = y;
		this.theta = theta;
		this.speed = 15; 
		radius = 10; 
		

	}
	
	public float getX() {
		return x;
	}


	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x; 
	}
	public void setY(float y) {
		this.y = y;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public float getTheta() {
		return theta;
	}

	public void setTheta(float theta) {
		this.theta = theta;
	}

	public float getSpeed(){
		return speed; 
	}
	


}
