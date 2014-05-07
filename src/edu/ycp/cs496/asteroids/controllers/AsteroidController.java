package edu.ycp.cs496.asteroids.controllers;

import java.util.ArrayList;
import java.util.List;

import android.R;
import edu.ycp.cs496.asteroids.model.Asteroid;
import edu.ycp.cs496.asteroids.model.Location;

public class AsteroidController {	
	
	private List<Asteroid> asteroids;
	private int smallWidth; 
	private int medWidth; 
	private int largeWidth; 
	
	public AsteroidController(List<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}
	
	public AsteroidController(int sW, int mW, int lW) {
		// TODO Auto-generated constructor stub
		asteroids = new ArrayList<Asteroid>();
		this.smallWidth = sW; 
		this.medWidth = mW; 
		this.largeWidth = lW; 
	}

	public int update(Integer level) {
		
		int points = 0;
		
		if(asteroids.isEmpty()) {
			// Increment the game level.
			level++;
			
			// Repopulate list of asteroids based on current level.
			for(int i = 0; i < 3*level; i++) {
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