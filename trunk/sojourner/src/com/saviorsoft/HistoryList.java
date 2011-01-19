package com.saviorsoft;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class HistoryList extends ListActivity {

	private static final int INSERT_ID = Menu.FIRST;
	private int mNoteNumber = 1;
	private HistoryDbAdapter mDbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.historylayout);
        
        mDbHelper = new HistoryDbAdapter(this);
        mDbHelper.open();
        fillData();
	}

	private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllNotes();
        startManagingCursor(c);

        String[] from = new String[] { HistoryDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.text1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.history_row, c, from, to);
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
            createNote();
            return true;
        }
       
        return super.onOptionsItemSelected(item);
	}

	private void createNote() {
        String noteName = "Note " + mNoteNumber++;
        mDbHelper.createNote(noteName, "");
        fillData();
	}

	
	


	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
//        Cursor c = mNotesCursor;
//        c.moveToPosition(position);
//        Intent i = new Intent(this, NoteEdit.class);
//        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
//        i.putExtra(NotesDbAdapter.KEY_TITLE, c.getString(
//                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
//        i.putExtra(NotesDbAdapter.KEY_BODY, c.getString(
//                c.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
//        startActivityForResult(i, ACTIVITY_EDIT);
    }
	
}
