package com.saviorsoft;

import android.location.Location;


public class MyAppContext extends android.app.Application {

    //sensible place to declare a log tag for the application
    public static final String LOG_TAG = "Sojouner";
    public static String newline = System.getProperty("line.separator");

    //instance 
    //private static MyAppContext instance = null;

    //keep references to our global resources
    public Location mCurrLocation = new Location(LOG_TAG);
    public Location mWayLocation = new Location(LOG_TAG);
    public float mWaypointAngle = 0;
    public float mWaypointDistance = 0;
    
    
    
    public void reCalcWaypoint(){
    	mCurrLocation.setAltitude(0);
    	mWayLocation.setAltitude(0);
        mWaypointAngle = mCurrLocation.bearingTo(mWayLocation);
        mWaypointDistance = (float) (mCurrLocation.distanceTo(mWayLocation)/0.3048);    	
    }

    public String locationToString(){
    	String str = "My current location is:" +	"\nLatitude = " +
		mCurrLocation.getLatitude() + "\nLongitude = " + 
		mCurrLocation.getLongitude() + "\nAltitude = " + mCurrLocation.getAltitude() +
		newline +	
		"\nMy Waypoint location is:" +	"\nLatitude = " + 
		mWayLocation.getLatitude()	+ "\nLongitude = " + 
		mWayLocation.getLongitude() + "\nAltitude = " + mWayLocation.getAltitude();
    	return str;
    }
    
    public String bearingsToString(){
    	String str = "Waypoint Angle: " + String.valueOf(mWaypointAngle) + "\n" +
    	"Distance: " + String.valueOf(mWaypointDistance);
    	return str;
    }
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        //provide an instance for our static accessors
        //instance = this;
    }

	
}
