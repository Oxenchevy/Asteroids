package edu.ycp.cs496.asteroids.controllers;

import edu.ycp.cs496.asteroids.model.Game;
import edu.ycp.cs496.main.GameThread;

public class AsteroidsSingleton {

	private static Game game; 
	private static int smallAsteroidWidth; 
	private static int medAsteroidWidth; 
	private static int largeAsteroidWidth; 
	private static int screenHeight; 
	private static int screenWidth; 


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

	public static Game getGame( ){
		return game; 
	}

	public static void setGame(Game g){
		game = g; 
	}


	public static void setAsteroidWidths(int small, int med, int large){
		smallAsteroidWidth = small; 
		medAsteroidWidth = med; 
		largeAsteroidWidth = large; 
	}

	public static void setScreenSize(int width, int height){
		screenWidth = width;
		screenHeight = height; 
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

	public static int getScreenHeight() {
		return screenHeight;
	}

	public static int getScreenWidth() {
		return screenWidth;
	}
	

	
	
}