package edu.ycp.cs496.main;

import edu.ycp.cs496.asteroids.controllers.AsteroidsSingleton;
import edu.ycp.cs496.asteroids.model.Game;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameOver extends Activity {


	private Game game; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);


		// display game over
		// display final score
		// go to leader board or main menu
		AsteroidsSingleton.getInstance();

		game = AsteroidsSingleton.getGame();
		TextView finalScore = (TextView)findViewById(R.id.finalScore); 
		finalScore.setText(Integer.toString(game.getUser().getScore()));
		
		AsteroidsSingleton.getInstance();
		GameThread thread = AsteroidsSingleton.getThread();
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Button menu = (Button) findViewById(R.id.Menubtn);		
		menu.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				
			
				Intent intent = new Intent(getBaseContext(), MenuActivity.class);
				startActivity(intent); 
			}

		});  


		Button leaderboard = (Button) findViewById(R.id.Leaderboardbtn);
		leaderboard.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
	
				Intent intent = new Intent(getBaseContext(), LeaderboardActivity.class);
				startActivity(intent); 
			}

		});  
		
	


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
	}

}
