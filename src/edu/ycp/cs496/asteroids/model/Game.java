package edu.ycp.cs496.asteroids.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private Ship ship;
	private List<Asteroid> asteroids;
	private User user; 
	private int level; 
	
	public Game(int x, int y){
		user = new User(); 
		ship = new Ship(x/2, y/2); 
		asteroids = new ArrayList<Asteroid>(); 
		
		level = 0; 
	}
	
	public Game(User u, int x, int y){
		this.user = u;
		ship = new Ship(x/2, y/2); 
		asteroids = new ArrayList<Asteroid>(); 
		
		level = 0; 
	}
	
	public boolean checkEndGame(){
		
		return ship.getLives() < 0; 
	}
	
	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public List<Asteroid> getAsteroids() {
		return asteroids;
	}

	public void setAsteroids(List<Asteroid> asteroids) {
		this.asteroids = asteroids;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public int getLevel(){
		return level; 
	}
	
	public void nextLevel(){
		level++; 
	}
	


	

	
}
