package edu.ycp.cs496.asteroids.servlets;


import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs496.asteroids.model.json.JSON;

public class Leaderboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Handle get request
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			
		}

		//Handle post request
		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			
		}


		//Handle delete request
		@Override
		protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			
		}
}
