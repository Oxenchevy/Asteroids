package edu.ycp.cs496.asteroids.controllers;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import org.xml.sax.SAXException;

public class DeleteLeaderboard {

		public boolean deleteScores() throws URISyntaxException, ClientProtocolException, IOException { 
			return makeDeleteRequest(); 
		} 

		private boolean makeDeleteRequest() throws URISyntaxException, ClientProtocolException, IOException { 
			// TODO: Implement method to issue delete inventory request 
			// Create HTTP client 
			HttpClient client = new DefaultHttpClient(); 

			// Construct URI 
			URI uri; 
			uri = URIUtils.createURI("http", "23.21.105.22", 8081, "/leaderboard", 
				    null, null);
			

			// Construct request 
			HttpDelete request = new HttpDelete(uri); 

			// Execute request 
			HttpResponse response = client.execute(request); 

			// Parse response 
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
				// Copy the response body to a string 


				// Parse JSON 
				return true;                     
			}  

			return false; 
		}

	}

	

