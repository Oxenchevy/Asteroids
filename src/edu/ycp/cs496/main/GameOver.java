package edu.ycp.cs496.main;

import edu.ycp.cs496.asteroids.controllers.AsteroidsSingleton;
import edu.ycp.cs496.asteroids.model.Game;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameOver extends Activity {


	private Game game; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game_over);

		
		// display game over
		// display final score
		// go to leader board or main menu
		AsteroidsSingleton.getInstance();

		game = AsteroidsSingleton.getGame();
		TextView finalScore = (TextView)findViewById(R.id.finalScore); 
		finalScore.setText(Integer.toString(game.getUser().getScore()));


		final TextView name = (TextView)findViewById(R.id.txtName);
		Button submit = (Button) findViewById(R.id.Submitbtn);
		submit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {				
					// submit the information to the server and proceed to the leaderboard
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
