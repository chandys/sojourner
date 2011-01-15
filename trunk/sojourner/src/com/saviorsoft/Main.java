package com.saviorsoft;

//import com.example.android.skeletonapp.R;

import com.saviorsoft.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	
    static final private int BACK_ID = Menu.FIRST;
    static final private int CLEAR_ID = Menu.FIRST + 1;
	
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

        // We are going to create two menus. Note that we assign them
        // unique integer IDs, labels from our string resources, and
        // given them shortcuts.
        menu.add(0, BACK_ID, 0, R.string.back).setShortcut('0', 'b');
        menu.add(0, CLEAR_ID, 0, R.string.clear).setShortcut('1', 'c');

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
        case BACK_ID:
            //finish();
            return true;
        case CLEAR_ID:
            //mEditor.setText("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A call-back for when the user presses the New Track button.
     */
    OnClickListener mNewTrackListener = new OnClickListener() {
        public void onClick(View v) {
            //finish();
        }
    };

    /**
     * A call-back for when the user presses the history button.
     */
    OnClickListener mHistoryListener = new OnClickListener() {
        public void onClick(View v) {
            //finish();
        }
    };

    /**
     * A call-back for when the user presses the current track button.
     */
    OnClickListener mCurrentLocationListener = new OnClickListener() {
        public void onClick(View v) {
          
        }
    };

    
    
    
   
}