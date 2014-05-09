package edu.ycp.cs496.asteroids.model;

import java.util.ArrayList;
import java.util.List;



public class Ship extends User {

	private int hitpoints, lives, radius;

	private int x;
	private int y;
	private float theta; 
	
	private List<Projectile> projectiles; 

	public Ship(int x, int y) {
		
		this.x = x; 
		this.y = y; 
		theta = 0; 
		// INITIALIZE DIRECTION
		hitpoints = 3;
		radius = 40;
		lives = 0;
		
		projectiles = new ArrayList<Projectile>(); 
	}
	
	public float getx() {
		return x;
	}

	public float gety() {
		return y;
	}


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
	
	public int getHitpoints(){
		return hitpoints; 
	}
	
	public void setHitpoints(int points){
		hitpoints = points; 
	}
	
	public void rotate(float dTheta){
		this.theta += dTheta; 
		
		if(theta < 0){
			theta = 359; 
		}
		
		if(theta > 359){
			theta = 0; 
		}
	}
	
	public float getTheta() {
		return theta;
	}

	public void setTheta(float theta) {
		this.theta = theta;
	}
	
	public void removeProjectile(int index)
	{
		projectiles.remove(index);
	}
	
	public Object[] getProjectiles(){
		return projectiles.toArray(); 
	}
	public void addProjectile(int x, int y, float theta){
		projectiles.add(new Projectile(x, y, theta)); 
	}
	
	public void updateProjectiles(int width, int height){
		
		Object[] tempArray = projectiles.toArray();
		
		for(int i = 0; i < tempArray.length; i++){
			if(((Projectile) tempArray[i]).getX() < 0 || ((Projectile) tempArray[i]).getX() > width){
				if(tempArray.length == 0){
					break; 
				}
				projectiles.remove(i); 
			}
			
			if(((Projectile) tempArray[i]).getY() < 0 || ((Projectile) tempArray[i]).getY() > height){
				if(tempArray.length == 0){
					break; 
				}
				projectiles.remove(i); 
			}
			
			else{
				float speed = ((Projectile) tempArray[i]).getSpeed(); 
				float x = ((Projectile) tempArray[i]).getX(); 
				float y = ((Projectile) tempArray[i]).getY(); 
				float theta = ((Projectile) tempArray[i]).getTheta(); 
				
				
				float dx = (float) Math.sin(Math.toRadians(theta)) * speed;
		        float dy = -(float) Math.cos(Math.toRadians(theta)) * speed;
		        
		        ((Projectile) tempArray[i]).setX(x + dx); 
		        ((Projectile) tempArray[i]).setY(y + dy); 
			}
			
		}
	}
	
	

}
