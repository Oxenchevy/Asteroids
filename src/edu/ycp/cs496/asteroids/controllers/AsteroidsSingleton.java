package edu.ycp.cs496.asteroids.controllers;

import edu.ycp.cs496.asteroids.model.Game;

public class AsteroidsSingleton {

	private static Game game; 
	private static int smallAsteroidWidth; 
	private static int medAsteroidWidth; 
	private static int largeAsteroidWidth; 
	

	private static AsteroidsSingleton instance = null;
	protected AsteroidsSingleton() {
		// Exists only to defeat instantiation.
	}
	public static AsteroidsSingleton getInstance() {
		if(instance == null) {
			instance = new AsteroidsSingleton();
		}
		return instance;
	}

	public static Game getGame( )
	{
		return game; 
	}

	public static void setGame(Game g)
	{
		game = g; 
	}


	public static void setAsteroidWidths(int small, int med, int large)
	{
		smallAsteroidWidth = small; 
		medAsteroidWidth = med; 
		largeAsteroidWidth = large; 
	}
	
	
	public static int getSmallAsteroidWidth() {
		return smallAsteroidWidth;
	}
	public static int getMedAsteroidWidth() {
		return medAsteroidWidth;
	}
	public static int getLargeAsteroidWidth() {
		return largeAsteroidWidth;
	}

	


}