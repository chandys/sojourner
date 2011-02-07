package com.saviorsoft;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;




public class GpsUtils {
	private LocationManager mLocManager;
	private Activity mActivity;
	private MyLocationListener mLocListener;
	
	
	public GpsUtils(Activity activity)
	{
		mActivity = activity;;
		
	}
	
	public Location getGpsLocation()
	{
		
		mLocManager = (LocationManager)mActivity.getSystemService(Context.LOCATION_SERVICE);
		mLocListener = new MyLocationListener();
		mLocManager.requestLocationUpdates( 
				LocationManager.GPS_PROVIDER, 0, 0, mLocListener);

		//mLocListener.
		return null;
	}
	
	
	
	
	/* Class My Location Listener */	
	public class MyLocationListener implements LocationListener	{
	
		@Override
		public void onLocationChanged(Location loc){
			SharedPreferences settings = mActivity.getSharedPreferences(
					Main.PREFS_NAME, Context.MODE_WORLD_WRITEABLE);
			Editor ed = settings.edit();
			ed.putString("MyLat", String.valueOf(loc.getLatitude()));
			ed.putString("MyLong", String.valueOf(loc.getLongitude()));
			ed.putString("MyAtt", String.valueOf(loc.getAltitude()));
			ed.commit();
			String Text = "My current location is: " +	"Latitude = " + loc.getLatitude() 
				+ "  Longitude = " + loc.getLongitude();
			//loc.getTime();
			//loc.bearingTo(dest);
			//loc.distanceTo(dest);
			Toast.makeText( mActivity.getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
			TextView t = (TextView) mActivity.findViewById(R.id.textViewGPS);
			t.setText(Text);
			
			//unregister
			mLocManager.removeUpdates(mLocListener);
		}
		
		
		@Override	
		public void onProviderDisabled(String provider){
			Toast.makeText( mActivity.getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
		}
		
		
		@Override	
		public void onProviderEnabled(String provider)	{
			Toast.makeText( mActivity.getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
		}
		
		
		@Override	
		public void onStatusChanged(String provider, int status, Bundle extras){
		}
	
	}/* End of Class MyLocationListener */
	
}
