package edu.ycp.cs496.asteroids.controllers;

import edu.ycp.cs496.asteroids.model.Ship;

public class ShipController {
	private Ship ship; 
	float frameTime = 0.016f;
	
public ShipController()
{
	 ship = new Ship();
}
	public void updateShip(float xAccel, float yAccel)
	{
	
		float XtempVel = ship.getxVelocity();
		float YtempVel = ship.getyVelocity();
		XtempVel += (xAccel * frameTime);
		YtempVel += (yAccel * frameTime);
		
		ship.setxVelocity(XtempVel);
		ship.setyVelocity(YtempVel);
		
		//Calc distance travelled in that time
		 float xS = (XtempVel/2)*frameTime;
		 float yS = (YtempVel/2)*frameTime;
		 
		 float x = ship.getx();
		 float y = ship.gety();
		 
		 x -= xS; 
		 y -= yS;	
		
		ship.update(x,y);
	}
	
	
public Ship getShip()
{
	return ship;
}

}
