package edu.ycp.cs496.asteroids.controllers;

import java.util.List;

import edu.ycp.cs496.asteroids.model.Asteroid;

public class AsteroidController {	
	
	private List<Asteroid> asteroids;
	
	public AsteroidController(List<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}
	
	public void update(long elapsedTime) {
		/*mX += mDx * (elapsedTime / 20f);
		mY += mDy * (elapsedTime / 20f);
		checkBoundary();*/
		
		if(asteroids.isEmpty()) {
			// Increment the game level.
			
			// Repopulate list of asteroids based on level.
		}
		
		for(Asteroid asteroid : asteroids) {
			// Update positions, check for collisions, update velocities.
		}
	}

	
}
