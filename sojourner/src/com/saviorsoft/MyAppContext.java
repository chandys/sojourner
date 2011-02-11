package com.saviorsoft;

import android.location.Location;


public class MyAppContext extends android.app.Application {

    //sensible place to declare a log tag for the application
    public static final String LOG_TAG = "Sojouner";

    //instance 
    //private static MyAppContext instance = null;

    //keep references to our global resources
    public Location mCurrLocation = new Location(LOG_TAG);
    public Location mWayLocation = new Location(LOG_TAG);
    public float mWaypointAngle = 0;
    public float mWaypointDistance = 0;
    
    
    
    public void reCalcWaypoint(){
        mWaypointAngle = mCurrLocation.bearingTo(mWayLocation);
        mWaypointDistance = (float) (mCurrLocation.distanceTo(mWayLocation)/0.3048);    	
    }

    /**
     * Convenient accessor, saves having to call and cast getApplicationContext() 
     */
    //public static MyAppContext getInstance() {
    //    checkInstance();
    //    return instance;
    //}

    /**
     * Accessor for some resource that depends on a context
     */
    //public static Location getCurrLocation() {
    //    if (mCurrLocation == null) {
    //        checkInstance();
    //        mCurrLocation = new Location(LOG_TAG);
    //   }
    //    return mCurrLocation;
    //}

    /**
     * Accessor for another resource that depends on a context
     */
    //public static Location getWayLocation() {
    //    if (mWayLocation == null) {
    //        checkInstance();
    //        mWayLocation = new Location(LOG_TAG);
    //    }
    //    return mWayLocation;
    //}

    //private static void checkInstance() {
    //    if (instance == null)
    //        throw new IllegalStateException("Application not created yet!");
    //}

    @Override
    public void onCreate() {
        super.onCreate();
        //provide an instance for our static accessors
        //instance = this;
    }

	
}
