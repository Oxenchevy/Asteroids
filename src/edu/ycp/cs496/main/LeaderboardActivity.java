package edu.ycp.cs496.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import edu.ycp.cs496.asteroids.controllers.GetLeaderboard;
import edu.ycp.cs496.asteroids.controllers.ScoreController;
import edu.ycp.cs496.asteroids.model.User;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
	public static int mWidth;
	public static int mHeight;

	private ArrayList<TextView> static_name_list;
	private List<TextView> dynamic_name_list;

	private ArrayList<TextView> static_score_list;
	private List<TextView> dynamic_score_list;

	@SuppressLint("NewApi")
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

		static_name_list = new ArrayList<TextView>();
		static_name_list.add((TextView) findViewById(R.id.index0));
		static_name_list.add((TextView) findViewById(R.id.index1));
		static_name_list.add((TextView) findViewById(R.id.index2));
		static_name_list.add((TextView) findViewById(R.id.index3));
		static_name_list.add((TextView) findViewById(R.id.index4));
		static_name_list.add((TextView) findViewById(R.id.index5));
		static_name_list.add((TextView) findViewById(R.id.index6));
		static_name_list.add((TextView) findViewById(R.id.index7));
		static_name_list.add((TextView) findViewById(R.id.index8));
		static_name_list.add((TextView) findViewById(R.id.index9));
		dynamic_name_list = new ArrayList<TextView>();


		static_score_list = new ArrayList<TextView>();
		static_score_list.add((TextView) findViewById(R.id.txtScore0));
		static_score_list.add((TextView) findViewById(R.id.txtScore1));
		static_score_list.add((TextView) findViewById(R.id.txtScore2));
		static_score_list.add((TextView) findViewById(R.id.txtScore3));
		static_score_list.add((TextView) findViewById(R.id.txtScore4));
		static_score_list.add((TextView) findViewById(R.id.txtScore5));
		static_score_list.add((TextView) findViewById(R.id.txtScore6));
		static_score_list.add((TextView) findViewById(R.id.txtScore7));
		static_score_list.add((TextView) findViewById(R.id.txtScore8));
		static_score_list.add((TextView) findViewById(R.id.txtScore9));
		dynamic_score_list = new ArrayList<TextView>();		

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
	

		final ArrayList<String> nameArray = new ArrayList<String>(); 
		final ArrayList<String> scoreArray = new ArrayList<String>(); 
		// gets the right number of text boxers to display information
		for(int i = 0; i < leaderboard.length; i++)
		{
			dynamic_name_list.add(static_name_list.get(i));
			dynamic_score_list.add(static_score_list.get(i));
		}
		

		

		Collections.sort(Arrays.asList(leaderboard), new Comparator<User>() {  

			@Override
			public int compare(User lhs, User rhs) {
				// TODO Auto-generated method stub
		  
				 return rhs.getScore() - lhs.getScore();
			}
		});
		
		for(User u : leaderboard){
			nameArray.add(u.getName().toString()); // + "-" + Integer.toString(u.getScore())); 
			scoreArray.add(Integer.toString(u.getScore()));
		}	

		


		int count = 0;

		// only get the top 10
		for(int i = 0; i < leaderboard.length; i++)
		{
			if (count != 10)
			{
				dynamic_name_list.get(i).setText(nameArray.get(i));
				dynamic_score_list.get(i).setText(scoreArray.get(i));
				count++;
			}
			else
			{
				break;
			}

		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {
	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
	     return true;
	     }
	     return super.onKeyDown(keyCode, event);    
	}

}
