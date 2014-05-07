package edu.ycp.cs496.asteroids.controllers;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import edu.ycp.cs496.asteroids.model.Asteroid;
import edu.ycp.cs496.asteroids.model.Game;
import edu.ycp.cs496.asteroids.model.Location;
import edu.ycp.cs496.asteroids.model.Projectile;

public class GameController {
	private Game game; 
	private ArrayList<Point> bullets; 
	private List<Asteroid> asteroids;
	private GameController gc; 
	private int smallWidth, medWidth, largeWidth; 
	
	public GameController(){
		game = AsteroidsSingleton.getGame(); 
		smallWidth = AsteroidsSingleton.getSmallAsteroidWidth(); 
		medWidth = AsteroidsSingleton.getMedAsteroidWidth(); 
		largeWidth = AsteroidsSingleton.getLargeAsteroidWidth(); 
		
		bullets = new ArrayList<Point>();
		asteroids = new ArrayList<Asteroid>(); 
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
	
	public int getLevel(){
		return game.getLevel(); 
	}
	
	public void removeProjectile(int index){
		 game.getShip().removeProjectile(index);
	}
	
	public int update() {
		
		int points = 0;
		
		if(asteroids.isEmpty()) {
			// Increment the game level.
			game.nextLevel(); 
			
			// Repopulate list of asteroids based on current level.
			for(int i = 0; i < 3*game.getLevel(); i++) {
				// Spawn an asteroid with random attributes at a random location on the edge of the map.
				asteroids.add(new Asteroid(smallWidth, medWidth, largeWidth));
			}
		} else { // Otherwise, check for destroyed asteroids/spawn new asteroids, update asteroid positions, etc.
			
			List<Asteroid> updatedAsteroids = new ArrayList<Asteroid>();
			
			for(Asteroid asteroid : asteroids) {
				
				// If the current asteroid has no hitpoints left, add the newly spawned asteroids to the updated list.
				// (You can't simply remove it because that breaks the for-each loop.)
				if(asteroid.isDestroyed()) {
					
					switch(asteroid.getSize()) {
					case 3: // If a LARGE asteroid, then spawn two MEDIUM asteroids at its location.
						updatedAsteroids.add(new Asteroid(2, new Location(asteroid.getLocation().getX()-5, asteroid.getLocation().getY()-5)));
						updatedAsteroids.add(new Asteroid(2, new Location(asteroid.getLocation().getX()+5, asteroid.getLocation().getY()+5)));
						points += 3;
						break;
					case 2: // If a MEDIUM asteroid, then spawn two SMALL asteroids at its location.
						updatedAsteroids.add(new Asteroid(1, new Location(asteroid.getLocation().getX()-5,asteroid.getLocation().getY()-5)));
						updatedAsteroids.add(new Asteroid(1, new Location(asteroid.getLocation().getX()+5,asteroid.getLocation().getY()+5)));
						points += 2;
						break;
					case 1: // If a SMALL asteroid, then simply add its value to score tally.
						points++;
						default: break;
					}
				} else { // Otherwise, just move the asteroid to the new list.
					updatedAsteroids.add(asteroid);
					
				}	
			}
			
			// Point to the updated list (garbage collection will get rid of the old one).
			asteroids = updatedAsteroids;
		
			// Check to see if asteroids are moving off-screen.
			for(Asteroid asteroid : asteroids) {
				asteroid.updateLocation(); 
				asteroid.checkBoundary();	
			}
		}
		
		return points;
	}
	
	public Object[] getAsteroidList(){
		
		return asteroids.toArray(); 
	}
	
	public void removeAsteroid(int index)
	{
		asteroids.remove(index);
	}
	
	
}
