package edu.ycp.cs496.asteroids.controllers;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import edu.ycp.cs496.asteroids.model.Game;
import edu.ycp.cs496.asteroids.model.Projectile;

public class GameController {
	private Game game; 
	private ArrayList<Point> bullets; 
	
	public GameController(Game game){
		this.game = game;
		bullets = new ArrayList<Point>(); 
	}
	
	public void rotateShip(float dTheta){
		game.getShip().rotate(dTheta); 
	}
	
	public float getRotation(){
		return game.getShip().getTheta(); 
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
	
	
}
