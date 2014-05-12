package edu.ycp.cs496.main;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.ycp.cs496.asteroids.controllers.AsteroidsSingleton;
import edu.ycp.cs496.asteroids.controllers.DeleteLeaderboard;
import edu.ycp.cs496.asteroids.controllers.PostScore;
import edu.ycp.cs496.asteroids.model.Game;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		AsteroidsSingleton.getInstance();

		game = AsteroidsSingleton.getGame();
		TextView finalScore = (TextView)findViewById(R.id.finalScore); 
		finalScore.setText(Integer.toString(game.getUser().getScore()));

		final TextView UserName = (TextView)findViewById(R.id.txtName);


		UserName.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				name = UserName.getText().toString();
		
		
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		});


		Button submit = (Button) findViewById(R.id.Submitbtn);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			
				/*try {
					for (int i = 0; i < 9; i++)
					{
						deleteInventory();
					}
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

*/

				score = game.getUser().getScore();
				if (name != null)
				{

					try {
						postItem(name.toUpperCase(), score);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 

					Intent intent = new Intent(getBaseContext(), LeaderboardActivity.class);
					startActivity(intent); 

				}
				else
				{
					Toast.makeText(GameOver.this, "Fill in Name", Toast.LENGTH_SHORT).show(); 
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
		boolean scores = ic.postScore(name, score); 
		if (scores == true) { 
			//Toast.makeText(GameOver.this, "Added " + score + " " + name, Toast.LENGTH_SHORT).show(); 
		} else { 
			//Toast.makeText(GameOver.this, "Could not add " + name, Toast.LENGTH_SHORT).show(); 
		} 
	} // end the postItem 

	public static void deleteInventory() throws URISyntaxException, ClientProtocolException, 
	IOException, ParserConfigurationException, SAXException{ 
		DeleteLeaderboard ic = new DeleteLeaderboard(); 
		boolean item = ic.deleteScores(); 

	} // end the deleteItem 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {
	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
	     return true;
	     }
	     return super.onKeyDown(keyCode, event);    
	}

}
