package edu.ycp.cs496.asteroids.controllers;

import edu.ycp.cs496.asteroids.model.Ship;

public class ShipController {
	private Ship ship; 
	float frameTime = 0.016f;
	int angle;

	public ShipController()
	{
		ship = new Ship();
	}

	public void setAngle(int angle)
	{
		this.angle += angle;
	}

	public int getAngle()
	{
		return angle;
	}


	public Ship getShip()
	{
		return ship;
	}

}
