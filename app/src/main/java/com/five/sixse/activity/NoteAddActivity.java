package com.five.sixse.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.five.sixse.R;
import com.five.sixse.adapter.ListAdapter;
import com.five.sixse.model.Book;
import com.five.sixse.helper.Constants;
import com.five.sixse.model.Note;
import com.five.sixse.helper.NoteDBHelper;

import java.util.ArrayList;

public class NoteAddActivity extends AppCompatActivity {


    public EditText addNote;
    public Button save;
    public NoteDBHelper noteDBHelper;
    public int vId;
    public String v_note, exist_note;
    public Toolbar toolbar;
    public ArrayList<Note> noteList;
    public ArrayList<Book> bookList;
    public String activity;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layout_manager;
    public ListAdapter adapter;
    public TextView notes;
    public SharedPreferences preferences;
    public String evalue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Note");
        noteDBHelper = MainActivity.noteDBHelper;

        vId = getIntent().getIntExtra("v_id", 0);
        activity = getIntent().getStringExtra("activity");

        bookList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layout_manager = new LinearLayoutManager(NoteAddActivity.this);
        recyclerView.setLayoutManager(layout_manager);
        addNote = (EditText) findViewById(R.id.add_note);
        save = (Button) findViewById(R.id.save_note);
        notes = (TextView) findViewById(R.id.notes);

        //get note from db if note exist with chapter id in table
        exist_note = noteDBHelper.GetNoteByV_Id(vId);
        addNote.setText(exist_note);


        addNote.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                evalue = "1";
                return false;
            }
        });

        //when activity  is equals to add then we hide recyclerview
        //if activity is equal list .then we show recyclerview
        if (activity.equals("add")) {
            //when note doesn't exist in table
            notes.setVisibility(View.VISIBLE);
        } else if (activity.equals("list")) {
            //when note exist in table
            init();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v_note = addNote.getText().toString();
                if (exist_note.equals("")) {

                    if (!v_note.isEmpty()) {
                        noteDBHelper.insertIntoDB(v_note, vId);
                        finish();
                    } else {
                        Toast.makeText(NoteAddActivity.this, "please write note", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    if (!v_note.isEmpty()) {
                        noteDBHelper.UpdateNote(vId, v_note);
                        finish();
                    } else {
                        Toast.makeText(NoteAddActivity.this, "please write note", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //noteList = new ArrayList<>();
        //noteList = noteDBHelper.getAllNote();
    }

    public void init() {

        recyclerView.setVisibility(View.VISIBLE);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {
            bookList = MainActivity.dba.get_Bookmarked(vId, Constants.VAR_ENG);

        } else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {
            bookList = MainActivity.dba.get_Bookmarked(vId, Constants.VAR_HINDI);

        }else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_FRENCH)) {
            bookList = MainActivity.dba.get_Bookmarked(vId, Constants.VAR_FRENCH);

        }

        adapter = new ListAdapter(NoteAddActivity.this, bookList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        v_note = addNote.getText().toString();
        if (!evalue.equals("")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Save Note");

            alert.setMessage("Are you  want to save a note?");
            alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    v_note = addNote.getText().toString();
                    if (exist_note.equals("")) {
                        noteDBHelper.insertIntoDB(v_note, vId);
                        finish();
                    } else {
                        noteDBHelper.UpdateNote(vId, v_note);
                        finish();
                    }

                }
            });
            alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // close dialog
                    dialog.cancel();

                    finish();
                }
            });
            alert.show();
        } else {
            super.onBackPressed();
        }
    }
}
