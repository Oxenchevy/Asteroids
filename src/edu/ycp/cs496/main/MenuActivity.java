package edu.ycp.cs496.main;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// making it full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setDefaultView(); 
		
		//Start Music
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.asteroids);
		mediaPlayer.start(); // no need to call prepare(); create() does that for you
	}
	
	public void setDefaultView(){
		setContentView(R.layout.activity_menu);
		
		ImageView play = (ImageView) findViewById(R.id.imageView2); 
		play.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), AsteroidsTemp.class);
				startActivity(intent); 
			}
			
		});  
				
		ImageView settings = (ImageView) findViewById(R.id.imageView4); 
		settings.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
				startActivity(intent); 
			}	
		});  	
				
				
		
	}

}
