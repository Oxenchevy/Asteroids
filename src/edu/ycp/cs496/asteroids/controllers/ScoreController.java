package edu.ycp.cs496.asteroids.controllers;

import java.util.Collections;
import java.util.List;

import edu.ycp.cs496.asteroids.model.User;
import edu.ycp.cs496.asteroids.model.persist.Database;
import edu.ycp.cs496.asteroids.model.persist.IDatabase;

public class ScoreController {
	
	public void addScore(User u){
		IDatabase db = Database.getInstance();
		List<User> leaderboard = db.getLeaderboard(); 
		
		int lbSize = leaderboard.size(); 
		
		if(lbSize < 10){
			db.addUser(u); 	
		}
	}

}
