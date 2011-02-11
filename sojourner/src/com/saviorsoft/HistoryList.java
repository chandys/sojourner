package com.saviorsoft;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryList extends ListActivity {

	private static final int INSERT_ID = Menu.FIRST;
	private static long mNoteNumber = 1;
	private HistoryDbAdapter mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.historylayout);
        
        mDbHelper = new HistoryDbAdapter(this);
        mDbHelper.open();
        
        ListView lv = getListView();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override 
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) { 
            	onLongListItemClick(v,pos,id);
            	return true; 
           }
        }); 
        
        fillData();
	}

	
	//You then create your handler method: 
	protected void onLongListItemClick(View v, int pos, long id) { 
	  Log.i( "Sojouner", "onLongListItemClick id=" + id ); 
	  mNoteNumber = id;
	  
	  final CharSequence[] items = {"Show Track", "Send Track", "Delete Track"};

	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	  builder.setTitle("Pick Action");
	  builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
		        if(items[item] == "Delete Track"){
		        	mDbHelper.deleteNote(mNoteNumber);
		        	fillData();
		        }
		    }
		});
	  AlertDialog alert = builder.create();
	  alert.show();
	} 

	
	
	private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllPoints();
        startManagingCursor(c);

        String[] from = new String[] { HistoryDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.history_row, c, from, to);
        notes.setViewBinder(new MyViewBinder());
        setListAdapter(notes);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return result;
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case INSERT_ID:
            createPoint();
            return true;
        }
       
        return super.onOptionsItemSelected(item);
	}

	private void createPoint() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);  
		  
		alert.setTitle("Waypoint Name");  
		alert.setMessage("Please provide a name for the waypoint");  
		  
		// Set an EditText view to get user input   
		final EditText input = new EditText(this);  
		alert.setView(input);  
		  
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
		public void onClick(DialogInterface dialog, int whichButton) {  
		  Editable value = input.getText();  
		  // Do something with value!
		  SharedPreferences settings = getSharedPreferences(Main.PREFS_NAME, 0);
		  String pointName = "Waypoint_" + mNoteNumber++;
		  if(value.length() > 0)
			  pointName = value.toString();
	      mDbHelper.createWaypoint(pointName, settings.getString("MyLat", "0"), 
	        		settings.getString("MyLong", "0"), settings.getString("MyAtt", "0"));
	      fillData();		  }  
		});  
		  
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
		  public void onClick(DialogInterface dialog, int whichButton) {  
		    // Canceled.  
		  }  
		});  
		  
		alert.show(); ;
		

	}

	
	


	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
       
        Cursor c = mDbHelper.fetchPoint(id);
        
		SharedPreferences settings = getSharedPreferences(
				Main.PREFS_NAME, Context.MODE_WORLD_WRITEABLE);
		Editor ed = settings.edit();
		
		ed.putString("WayLat", c.getString(c.getColumnIndexOrThrow(HistoryDbAdapter.KEY_LAT)));
		ed.putString("WayLong", c.getString(c.getColumnIndexOrThrow(HistoryDbAdapter.KEY_LONG)));
		ed.putString("WayAtt", c.getString(c.getColumnIndexOrThrow(HistoryDbAdapter.KEY_ATT)));
		ed.commit();
          
		//save to application context
		MyAppContext mContext = (MyAppContext) getApplicationContext();
		mContext.mWayLocation.setLatitude(
				c.getDouble(c.getColumnIndexOrThrow(HistoryDbAdapter.KEY_LAT)));
		mContext.mWayLocation.setLongitude(
				c.getDouble(c.getColumnIndexOrThrow(HistoryDbAdapter.KEY_LONG)));
		mContext.mWayLocation.setAltitude(
				c.getDouble(c.getColumnIndexOrThrow(HistoryDbAdapter.KEY_ATT)));
		mContext.reCalcWaypoint();
        
        Toast msg = Toast.makeText(this, "Set as Waypoint", Toast.LENGTH_SHORT);
          //msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
          msg.show();
    }
	

	
	
	
	
    class MyViewBinder implements SimpleCursorAdapter.ViewBinder {

        //@Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        	TextView tv = (TextView) view;
        	String str = cursor.getString(1)+"\nLatitude: " + cursor.getString(2) + "\nLongitude: " +
        		cursor.getString(3) + "\nAttitude: " + cursor.getString(4);
        	tv.setText(str);

            return true;
        }
    }	
	
	
	
	
	

}
