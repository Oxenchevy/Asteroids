package edu.ycp.cs496.asteroids.controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.ycp.cs496.asteroids.model.User;
import edu.ycp.cs496.asteroids.model.json.JSON;

/**
 * Controller to get the complete inventory (list of items).
 */
public class PostScore {
	 
		public boolean postScore(String itemName, int score) throws URISyntaxException, JsonGenerationException, JsonMappingException, IOException { 

			return makePostRequest(itemName,score); 
		} 

		public boolean makePostRequest(String itemName, int score) throws URISyntaxException, JsonGenerationException, JsonMappingException, IOException { 
			// TODO: Implement method to issue delete inventory request 
			// Create HTTP client 
			HttpClient client = new DefaultHttpClient(); 

			// Construct URI 
			URI uri; 
			uri = URIUtils.createURI("http", "23.21.105.22", 8081, "/leaderboard", 
				    null, null);
			// Construct request 
			HttpPost request = new HttpPost(uri); 

			// Execute request 

			User newScore = new User(); 		

			newScore.setName(itemName); 
			newScore.setScore(score); 
			StringWriter sw = new StringWriter(); 
			JSON.getObjectMapper().writeValue(sw, newScore); 

			// Add JSON object to request 
			StringEntity reqEntity = new StringEntity(sw.toString()); 
			reqEntity.setContentType("application/json"); 
			request.setEntity(reqEntity); 

			HttpResponse response = client.execute(request); 
			// Parse response 
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
				System.out.println(response.getStatusLine().getStatusCode());
				return true;                     
			}  
			else 
			{ 
				System.out.println(response.getStatusLine().getStatusCode());
				return false; 
				
			} 
		} 
 
}


