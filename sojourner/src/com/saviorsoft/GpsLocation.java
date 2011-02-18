package com.saviorsoft;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.location.Location;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GpsLocation extends Activity {

	private LocationManager mLocManager;
	private LocationListener mLocListener;
	@SuppressWarnings("unused")
	private Bundle mBundle;
	//private MyAppContext mContext;

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gpslocation);
	
		
		
		drawText();
		
		/* Use the LocationManager class to obtain GPS locations */
		//mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		//mLocListener = new MyLocationListener();
		//mLocManager.requestLocationUpdates( 
		//		LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
		
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.buttonGetLoc)).setOnClickListener(GetLocListener);
        ((Button) findViewById(R.id.buttonSaveLoc)).setOnClickListener(SaveLocListener);

	}	

    private void drawText(){
    	MyAppContext mContext = ((MyAppContext) getApplicationContext());
    	String str = mContext.locationToString();
    	str += MyAppContext.newline + MyAppContext.newline;
    	mContext.reCalcWaypoint();
    	str += mContext.bearingsToString();
    	TextView t = (TextView) findViewById(R.id.textViewGPS);
		t.setText(str);
    }
    
	
//	private void PrintStats(){
//		MyAppContext mContext = ((MyAppContext) getApplicationContext());
//		String str = "My current location is:" +	"\nLatitude = " +
//		mContext.mCurrLocation.getLatitude() + "\nLongitude = " + 
//		mContext.mCurrLocation.getLongitude() +	
//		"\nMy Waypoint location is:" +	"\nLatitude = " + 
//		mContext.mWayLocation.getLatitude()	+ "\nLongitude = " + 
//		mContext.mWayLocation.getLongitude();
//		TextView t = (TextView) findViewById(R.id.textViewGPS);
//		t.setText(str);			
//	}

	
	
    /**
     * A call-back for when the user presses the current track button.
     */
    OnClickListener GetLocListener = new OnClickListener() {
        public void onClick(View v) {
    		/* Use the LocationManager class to obtain GPS locations */
    		mLocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    		mLocListener = new MyLocationListener();
    		mLocManager.requestLocationUpdates( 
    				LocationManager.GPS_PROVIDER, 0, 10, mLocListener);
        	
    		final Criteria criteria = new Criteria();

    		criteria.setAccuracy(Criteria.ACCURACY_FINE);
    		criteria.setAltitudeRequired(false);
    		criteria.setBearingRequired(false);
    		criteria.setCostAllowed(true);
    		criteria.setPowerRequirement(Criteria.POWER_LOW);
    		
    		
        	Toast.makeText( getApplicationContext(), "Getting Gps Location ...", 
        			Toast.LENGTH_SHORT ).show();
        }
    };

	
    /**
     * A call-back for when the user presses the current track button.
     */
    OnClickListener SaveLocListener = new OnClickListener() {
        public void onClick(View v) {
        	
        	Toast.makeText( getApplicationContext(), "Save Gps Location", 
        			Toast.LENGTH_SHORT ).show();
        }
    };

    

	/* Class My Location Listener */	
	public class MyLocationListener implements LocationListener	{
	
		@Override
		public void onLocationChanged(Location loc){
			//save to application context
			MyAppContext mContext = ((MyAppContext) getApplicationContext());
			mContext.mCurrLocation.setLatitude(loc.getLatitude());
			mContext.mCurrLocation.setLongitude(loc.getLongitude());
			mContext.mCurrLocation.setAltitude(loc.getAccuracy());

			
			//save to persistance storage
			SharedPreferences settings = getSharedPreferences(
					Main.PREFS_NAME, Context.MODE_WORLD_WRITEABLE);
			Editor ed = settings.edit();
			ed.putString("MyLat", String.valueOf(loc.getLatitude()));
			ed.putString("MyLong", String.valueOf(loc.getLongitude()));
			ed.putString("MyAtt", String.valueOf(loc.getAltitude()));
			ed.commit();

			drawText();
			
			String Text = "My current location is:" +	"\nLatitude = " + loc.getLatitude() 
			+ "\nLongitude = " + loc.getLongitude();
			Toast.makeText( getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
			//unregister
			mLocManager.removeUpdates(mLocListener);
		}
		
				
		@Override	
		public void onProviderDisabled(String provider){
			Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
		}
		
		
		@Override	
		public void onProviderEnabled(String provider)	{
			Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
		}
		
		
		@Override	
		public void onStatusChanged(String provider, int status, Bundle extras){
		}
	
	}/* End of Class MyLocationListener */

}/* End of GPS Location Activity */