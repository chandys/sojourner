package com.saviorsoft;


//import com.saviorsoft.CompassView.CompassThread;
import android.app.Activity;
import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassActivity extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */
   
   private SensorManager sensorManager;
//   private TextView txtRawData;
//   private TextView txtDirection;
   private float myAzimuth = 0;
   private float myPitch = 0;
   private float myRoll = 0;
   private CompassView mCompassView;
//   private CompassThread mCompassThread;


   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.compassview);
        
   
//        txtRawData = (TextView) findViewById(R.id.txt_info);
//        txtDirection = (TextView) findViewById(R.id.txt_direction);
//        txtRawData.setText("Compass");
//        txtDirection.setText("");
        
        
        
        // get handles to the LunarView from XML, and its LunarThread
        mCompassView = (CompassView) findViewById(R.id.compsurface);
        //mCompassThread = mCompassView.getThread();

        // give the LunarView a handle to the TextView used for messages
//        mCompassView.setTextView((TextView) findViewById(R.id.text));

        if (savedInstanceState == null) {
            // we were just launched: set up a new game
//            mCompassThread.setState(CompassThread.STATE_READY);
//            Log.w(this.getClass().getName(), "SIS is null");
        } else {
            // we are being restored: resume a previous game
            mCompassView.getThread().restoreState(savedInstanceState);
//            Log.w(this.getClass().getName(), "SIS is nonnull");
        }

        // Real sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);        
        
        
    }
    
    /** Register for the updates when Activity is in foreground */
    @Override
    protected void onResume() {
      super.onResume();
//      mCompassThread.unpause();
      sensorManager.registerListener(this, 
            sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    /** Stop the updates when Activity is paused */
    @Override
    protected void onPause() {
      super.onPause();
//      mCompassThread.pause();
      sensorManager.unregisterListener(this);
    }
    

   public void onAccuracyChanged(Sensor sensor, int accuracy) {
      //
      
   }

   public void onSensorChanged(SensorEvent event) {

           myAzimuth = Math.round(event.values[0]);
           myPitch = Math.round(event.values[1]);
           myRoll = Math.round(event.values[2]);

           mCompassView.mAzimuth = myAzimuth;
           mCompassView.mPitch = myPitch;
           mCompassView.mRoll = myRoll;
           
           mCompassView.mWayPointAngle = 228;
   }
   

}