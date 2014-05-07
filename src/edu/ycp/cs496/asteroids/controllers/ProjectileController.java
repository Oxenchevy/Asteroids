package edu.ycp.cs496.asteroids.controllers;

import edu.ycp.cs496.asteroids.model.Game;

public class ProjectileController {
	
	private Game game; 
	
	public ProjectileController(){
		game = AsteroidsSingleton.getGame(); 
	}
	
	public void fire(int height, int width){
		float theta = game.getShip().getTheta(); 
		game.getShip().addProjectile(width/2, height/2, theta); 
	}
	
	public void updateProjectiles(int width, int height){
		game.getShip().updateProjectiles(width, height); 
	}
	
	public Object[] getProjectileCoords(){
		return game.getShip().getProjectiles(); 
	}
	
	public int getLevel(){
		return game.getLevel(); 
	}
	
	public void removeProjectile(int index){
		 game.getShip().removeProjectile(index);
	}
}
