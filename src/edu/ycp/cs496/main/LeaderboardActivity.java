package edu.ycp.cs496.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;


import edu.ycp.cs496.asteroids.controllers.GetLeaderboard;
import edu.ycp.cs496.asteroids.controllers.ScoreController;
import edu.ycp.cs496.asteroids.model.User;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class LeaderboardActivity extends Activity {
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		
		context = this;
		setDefaultView(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leaderboard, menu);
		return true;
	}

	public void getLeaderboard() throws URISyntaxException, ClientProtocolException,
	IOException, ParserConfigurationException, SAXException{
		Log.d("", "HERE!"); 
		GetLeaderboard gl = new GetLeaderboard();
		User[] users = gl.getLeaderboard();
		Log.d("", users[0].getName());
		if (users != null) {
			displayLeaderboardView(users);
		} else {
			Toast.makeText(LeaderboardActivity.this, "INVENTORY NOT FOUND!", Toast.LENGTH_SHORT).show();
		}
	}

	/*public void getItem() throws URISyntaxException, ClientProtocolException,
	IOException, ParserConfigurationException, SAXException{
		GetItem ic = new GetItem();

		String itemString = itemTextView.getText().toString(); 

		if(itemString != null){
			Item item = ic.getItems(itemString);

			if(item != null){
				displayItemView(item); 
			}
			else{
				Toast.makeText(MobileInventoryClient.this, "ITEM NOT FOUND!", Toast.LENGTH_SHORT).show();
			}

		}
		else{
			getInventory(); 
		}
	}*/

	/*public void deleteItem() throws URISyntaxException, ClientProtocolException,
	IOException, ParserConfigurationException, SAXException{
		DeleteItem dc = new DeleteItem();

		String itemString = itemTextView.getText().toString(); 

		//if(itemString != null){
		if(dc.deleteItem(itemString) == true){
			Toast.makeText(MobileInventoryClient.this, itemString + " deleted!", Toast.LENGTH_SHORT).show();; 
		}
		else{
			Toast.makeText(MobileInventoryClient.this, "ITEM NOT FOUND!", Toast.LENGTH_SHORT).show();
		}
		//}

	}*/

	/*	public void postItem() throws URISyntaxException, ClientProtocolException,
	IOException, ParserConfigurationException, SAXException{
		ScoreController sc = new ScoreController();

		String itemString = itemTextView.getText().toString(); 
		Integer itemQuant = Integer.parseInt(quantityTextView.getText().toString());

		if(itemString != null && itemQuant != null){
			if(pc.postItem(itemString, itemQuant) == true){
				Toast.makeText(MobileInventoryClient.this, itemString + " updated!", Toast.LENGTH_SHORT).show();; 
			}
			else{
				Toast.makeText(MobileInventoryClient.this, "Post failed!", Toast.LENGTH_SHORT).show();
			}
		}

	}*/

	public void setDefaultView() {
		setContentView(R.layout.activity_leaderboard);
		
		Log.d(" ", "SDV"); 
		try {
			getLeaderboard();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}


	// Method for displaying inventory list
	public void displayLeaderboardView(User[] leaderboard) {
		// Create Linear layout
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);

		// Add back button
		Button backButton = new Button(this);
		backButton.setText("Main Menu");
		backButton.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		// TODO: Add back button onClickListener

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// go back to main menu
				
				Intent menuIntent = new Intent(context, MenuActivity.class);
				
			
				startActivity(menuIntent);
				
				try {
					setDefaultView(); 
				}
				catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		// Add button to layout
		layout.addView(backButton);

		final ArrayList<String> listArray = new ArrayList<String>(); 

		for(User u : leaderboard){
			listArray.add(u.getName().toString() + "-" + Integer.toString(u.getScore())); 
		}
		// TODO: Add ListView with inventory
		ListAdapter la = new ArrayAdapter<String>(this, R.layout.activity_leaderboard, listArray);
		ListView lv = new ListView(this);
		lv.setAdapter(la);      
		layout.addView(lv);
		// Make inventory view visible
		setContentView(layout,llp);    	
	}

}
