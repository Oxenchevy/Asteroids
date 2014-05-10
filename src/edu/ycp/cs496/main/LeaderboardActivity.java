package edu.ycp.cs496.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class LeaderboardActivity extends Activity {
	Context context;
	Button mainMenu;
	private ArrayList<TextView> static_txt_list;
	private List<TextView> dynamic_list;

	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_leaderboard);

		mainMenu = (Button)findViewById(R.id.mainMenu);
		mainMenu.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {				
				// submit the information to the server and proceed to the leaderboard
				Intent intent = new Intent(getBaseContext(), MenuActivity.class);
				startActivity(intent); 
			}
		});  

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		static_txt_list = new ArrayList<TextView>();
		static_txt_list.add((TextView) findViewById(R.id.index0));
		static_txt_list.add((TextView) findViewById(R.id.index1));
		static_txt_list.add((TextView) findViewById(R.id.index2));
		static_txt_list.add((TextView) findViewById(R.id.index3));
		static_txt_list.add((TextView) findViewById(R.id.index4));
		static_txt_list.add((TextView) findViewById(R.id.index5));
		static_txt_list.add((TextView) findViewById(R.id.index6));
		static_txt_list.add((TextView) findViewById(R.id.index7));
		static_txt_list.add((TextView) findViewById(R.id.index8));
		static_txt_list.add((TextView) findViewById(R.id.index9));

		dynamic_list = new ArrayList<TextView>();
		leaderboard();
		context = this;

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



	public void leaderboard() {

		try {
			getLeaderboard();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}


	// Method for displaying inventory list
	public void displayLeaderboardView(User[] leaderboard) {


		final ArrayList<String> listArray = new ArrayList<String>(); 
		// gets the right number of text boxers to display information
		for(int i = 0; i < leaderboard.length; i++)
		{
			dynamic_list.add(static_txt_list.get(i));
		}

		for(User u : leaderboard){
			listArray.add(u.getName().toString() + "-" + Integer.toString(u.getScore())); 
		}		

		// only get the top 10
		for(int i = 0; i < 9; i++)
		{
			dynamic_list.get(i).setText(listArray.get(i));

		}
	}

}
