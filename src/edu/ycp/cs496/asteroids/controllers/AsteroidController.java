package edu.ycp.cs496.asteroids.controllers;

import java.util.ArrayList;
import java.util.List;


import edu.ycp.cs496.asteroids.model.Asteroid;
import edu.ycp.cs496.asteroids.model.Game;
import edu.ycp.cs496.asteroids.model.Location;
import edu.ycp.cs496.asteroids.model.Projectile;

public class AsteroidController {	

	private Game game; 
	private int smallWidth; 
	private int medWidth; 
	private int largeWidth; 
	private List<Asteroid> asteroids; 
	
	public AsteroidController(){
		game = AsteroidsSingleton.getGame(); 
		asteroids = game.getAsteroids(); 
	}
	
	public void update() {

		
		asteroids = game.getAsteroids(); 
		
		if(asteroids.isEmpty()) {
			// Increment the game level.
			game.nextLevel(); 

			// Repopulate list of asteroids based on current level.
			for(int i = 0; i < 4*game.getLevel(); i++) {
				// Spawn an asteroid with random attributes at a random location on the edge of the map.
				asteroids.add(new Asteroid());
			}
		} else { // Otherwise, check for destroyed asteroids/spawn new asteroids, update asteroid positions, etc.

			List<Asteroid> updatedAsteroids = new ArrayList<Asteroid>();

			for(Asteroid asteroid : asteroids) {

				// If the current asteroid has no hitpoints left, add the newly spawned asteroids to the updated list.
				// (You can't simply remove it because that breaks the for-each loop.)
				if(asteroid.isDestroyed()) {

					switch(asteroid.getSize()) {
					case 3: // If a LARGE asteroid, then spawn two MEDIUM asteroids at its location.
						updatedAsteroids.add(new Asteroid(2, new Location(asteroid.getLocation().getX()-AsteroidsSingleton.getMedAsteroidWidth(), asteroid.getLocation().getY()-AsteroidsSingleton.getMedAsteroidWidth())));
						updatedAsteroids.add(new Asteroid(2, new Location(asteroid.getLocation().getX()+ AsteroidsSingleton.getMedAsteroidWidth(), asteroid.getLocation().getY()+AsteroidsSingleton.getMedAsteroidWidth())));
						game.getUser().addToScore(3); 
						break;
					case 2: // If a MEDIUM asteroid, then spawn two SMALL asteroids at its location.
						updatedAsteroids.add(new Asteroid(1, new Location(asteroid.getLocation().getX()-AsteroidsSingleton.getSmallAsteroidWidth(),asteroid.getLocation().getY()-AsteroidsSingleton.getSmallAsteroidWidth())));
						updatedAsteroids.add(new Asteroid(1, new Location(asteroid.getLocation().getX()+AsteroidsSingleton.getSmallAsteroidWidth(),asteroid.getLocation().getY()+AsteroidsSingleton.getSmallAsteroidWidth())));
						game.getUser().addToScore(2); 
						break;
					case 1: // If a SMALL asteroid, then simply add its value to score tally.
						game.getUser().addToScore(1); 
					default: break;
					}
				} else { // Otherwise, just move the asteroid to the new list.
					updatedAsteroids.add(asteroid);

				}	
			}

			// Point to the updated list (garbage collection will get rid of the old one).
			asteroids = updatedAsteroids;
			
			game.setAsteroids(asteroids); 
			
			// Check to see if asteroids are moving off-screen.
			for(Asteroid asteroid : asteroids) {
				asteroid.updateLocation(); 
				asteroid.checkBoundary();	
			}
		}

	}

	public Object[] getAsteroidList(){

		return asteroids.toArray(); 
	}

	public void removeAsteroid(int index)
	{
		asteroids.remove(index);
	}

	public void fireCollision()
	{
		Object[] asteroids  = getAsteroidList();
		for(int i = 0; i < getAsteroidList().length; i++){
			for(int p = 0; p < game.getShip().getProjectiles().length; p++){

				double xDif = ((Asteroid) asteroids[i]).getLocation().getX() - ((Projectile) game.getShip().getProjectiles()[p]).getX();
				double yDif = ((Asteroid) asteroids[i]).getLocation().getY() - ((Projectile) game.getShip().getProjectiles()[p]).getY();
				double distanceSquared = xDif * xDif + yDif * yDif;



				boolean collision = distanceSquared < (((Asteroid) asteroids[i]).getRadius() + 
						((Projectile) game.getShip().getProjectiles()[p]).getRadius()) * (((Asteroid) asteroids[i]).getRadius() + 
								((Projectile) game.getShip().getProjectiles()[p]).getRadius());
				if(collision)
				{				
					//removeAsteroid(i);
					((Asteroid) asteroids[i]).loseHitpoint(); 
					game.getShip().removeProjectile(p);
				}				
			}
		}
	}

	public void asteroidCollision()
	{
		Object[] asteroids  = getAsteroidList();

		for(int i=0; i<getAsteroidList().length; i++)
		{
			for(int j=i+1; j<getAsteroidList().length; j++)
			{

				double xDif = ((Asteroid) asteroids[i]).getLocation().getX() - ((Asteroid) asteroids[j]).getLocation().getX();
				double yDif = ((Asteroid) asteroids[i]).getLocation().getY() - ((Asteroid) asteroids[j]).getLocation().getY();
				double distanceSquared = xDif * xDif + yDif * yDif;

				double sumRadius = ((Asteroid) asteroids[i]).getRadius() + ((Asteroid) asteroids[j]).getRadius(); 
				double sqrRadius = sumRadius * sumRadius; 
				boolean collision = distanceSquared < sqrRadius;
				
				if(collision)
				{				
					//	System.out.println("Collision occured");
					/*((Asteroid) asteroids[j]).setDx();
					((Asteroid) asteroids[i]).setDx();
					((Asteroid) asteroids[j]).setDy();
					((Asteroid) asteroids[i]).setDy();*/
					
					((Asteroid) asteroids[i]).respondToCollision((Asteroid) asteroids[j]); 
				}
			}
		}
	}

	public void shipToAsteroidCollision()
	{
		Object[] asteroids  = getAsteroidList();

		for(int i=0; i<getAsteroidList().length; i++)
		{
			double xDif = ((Asteroid) asteroids[i]).getLocation().getX() - game.getShip().getx();
			double yDif = ((Asteroid) asteroids[i]).getLocation().getY() - game.getShip().gety();
			double distanceSquared = xDif * xDif + yDif * yDif;

			boolean collision = distanceSquared < (((Asteroid) asteroids[i]).getRadius() + 
					game.getShip().getRadius()) *(((Asteroid) asteroids[i]).getRadius() + 
							game.getShip().getRadius());
			if(collision)
			{				
				((Asteroid) asteroids[i]).setDx();
				((Asteroid) asteroids[i]).setDy();
				//System.out.println("COLLISION");

				int size = ((Asteroid) asteroids[i]).getSize();		

				switch(size) {
				case 3: // If a LARGE asteroid, lose 3 hitpoints
					game.getShip().loseHitpoint();
					game.getShip().loseHitpoint();
					game.getShip().loseHitpoint();
					break;
				case 2: // If a MEDIUM asteroid, lose 2 hitpoints

					game.getShip().loseHitpoint();
					game.getShip().loseHitpoint();
					break;
				case 1: // If a SMALL asteroid, lose 1 hitpoints
					game.getShip().loseHitpoint();

				}


			}
		}
	}
}