package edu.ycp.cs496.asteroids.controllers;
import edu.ycp.cs496.asteroids.model.Game;


public class ShipController {
	
	private Game game; 
	
	public ShipController(){
		game = AsteroidsSingleton.getGame(); 
	}
	
	public void rotateShip(float dTheta){
		game.getShip().rotate(dTheta); 
	}
	
	public float getRotation(){
		return game.getShip().getTheta(); 
	}
	
}
