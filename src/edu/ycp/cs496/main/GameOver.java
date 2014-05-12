package edu.ycp.cs496.main;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.ycp.cs496.asteroids.controllers.AsteroidsSingleton;
import edu.ycp.cs496.asteroids.controllers.PostScore;
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
	private String name;
	private int score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game_over);

		AsteroidsSingleton.getInstance();

		game = AsteroidsSingleton.getGame();
		TextView finalScore = (TextView)findViewById(R.id.finalScore); 
		finalScore.setText(Integer.toString(game.getUser().getScore()));

		final TextView UserName = (TextView)findViewById(R.id.txtName);

		Button submit = (Button) findViewById(R.id.Submitbtn);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				


				try { 
					name = UserName.getText().toString();
					score = game.getUser().getScore();
					if (name.length() > 0) 
					{ 
						// just add the name that was entered 
						postItem(name, score); 
						// if completed it should show a toast message that it was done 
						Intent intent = new Intent(getBaseContext(), LeaderboardActivity.class);
						startActivity(intent); 
					} 
					else 
					{ 
						// throw a toast error 
						Toast.makeText(GameOver.this, "Fill in the name", Toast.LENGTH_SHORT).show(); 
					} 
				} 
				catch (Exception e) { 
					e.printStackTrace(); 
				} 
			}
		}) ;  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_over, menu);
		return true;
	}

	public void postItem(String name, int score) throws URISyntaxException, ClientProtocolException, 
	IOException, ParserConfigurationException, SAXException{ 
		PostScore ic = new PostScore(); 
		boolean item = ic.postScore(name, score); 
		if (item == true) { 
			//Toast.makeText(GameOver.this, "Added " + score + " " + name, Toast.LENGTH_SHORT).show(); 
		} else { 
			//Toast.makeText(GameOver.this, "Could not add " + name, Toast.LENGTH_SHORT).show(); 
		} 
	} // end the postItem 

}
