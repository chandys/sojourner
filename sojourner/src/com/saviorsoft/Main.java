package com.saviorsoft;

//import com.example.android.skeletonapp.R;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity {
	
    static final private int COMPASS_ID = Menu.FIRST;
    static final private int SETTINGS_ID = Menu.FIRST + 1;
    static final private int EXIT_ID = Menu.FIRST + 2;

    
    public Main(){    	
    }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Hook up button presses to the appropriate event handler.
        ((Button) findViewById(R.id.ButtonNew)).setOnClickListener(mNewTrackListener);
        ((Button) findViewById(R.id.ButtonHistory)).setOnClickListener(mHistoryListener);
        ((Button) findViewById(R.id.ButtonCurrent)).setOnClickListener(mCurrentLocationListener);
    }
    
    
    /**
     * Called when the activity is about to start interacting with the user.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    
    /**
     * Called when your activity's options menu needs to be created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return true;
    }
    
    /**
     * Called right before your activity's option menu is displayed.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Before showing the menu, we need to decide whether the clear
        // item is enabled depending on whether there is text to clear.
        //menu.findItem(CLEAR_ID).setVisible(mEditor.getText().length() > 0);

        return true;
    }

    
    /**
     * Called when a menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.itemcompass:
        	Intent i = new Intent();
        	i.setClass(getApplicationContext(), com.saviorsoft.Compass.class);
        	startActivity(i);
            return true;
        case R.id.itemsettings:
        	Dialog dialog = new Dialog(this);
        	dialog.setContentView(R.layout.about);
        	dialog.setTitle(Html.fromHtml("<big>Sojourner</big><sup><small>TM </small> </sup>  <small> by</small> <a href=\"http://code.google.com/p/sojourner\"><cite><big>Savior</big></cite><sub><small>Soft</small></sub></a>"));
        	dialog.show();
        	return true;
        case R.id.itemexit:
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A call-back for when the user presses the New Track button.
     */
    OnClickListener mNewTrackListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent i = new Intent();
        	i.setClass(getApplicationContext(), com.saviorsoft.CompassMap.class);
        	startActivity(i);
            //finish();
        }
    };

    /**
     * A call-back for when the user presses the history button.
     */
    OnClickListener mHistoryListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent i = new Intent();
        	i.setClass(getApplicationContext(), com.saviorsoft.HistoryList.class);
        	startActivity(i);
        	//setContentView(R.layout.lunar_layoutcompassview);
        }
    };

    /**
     * A call-back for when the user presses the current track button.
     */
    OnClickListener mCurrentLocationListener = new OnClickListener() {
        public void onClick(View v) {
        	Intent i = new Intent();
        	i.setClass(getApplicationContext(), com.saviorsoft.CompassActivity.class);
        	startActivity(i);
        	//setContentView(R.layout.lunar_layout);
        }
    };

    
    
    
   
}