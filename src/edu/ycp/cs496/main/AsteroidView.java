package edu.ycp.cs496.main;

import edu.ycp.cs496.asteroids.controllers.ShipController;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
public class AsteroidView extends Activity  {

	SensorManager sensorManager;
	Sensor accelerometer; 

	int accelerometerSensor; 
	int magnetometerSensor;

	ShipController cont;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); 
		cont = new ShipController();
		setContentView(new Panel(this, cont,sensorManager));
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.asteroid_view, menu);		
		return true;
	}






	final SensorEventListener sensorEventListener = new SensorEventListener(){
		public void onSensorChanged(SensorEvent event) {


			float[] mGravity = null;
			float[] mGeomagnetic = null;
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
				mGravity = event.values;
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
				mGeomagnetic = event.values;
			if (mGravity != null && mGeomagnetic != null) {
				float R[] = new float[9];
				float I[] = new float[9];
				boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
				if (success) {
					float orientation[] = new float[3];
					SensorManager.getOrientation(R, orientation);
					float XAccel  = orientation[1]; // pitch
					float YAccel = orientation[2]; // roll
					
					
					System.out.println(XAccel);
					System.out.println(YAccel);

					cont.updateShip(XAccel, YAccel);
				}
			}

			//Set sensor values as acceleration



		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}
	};



}
