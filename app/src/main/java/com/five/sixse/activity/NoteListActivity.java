package com.five.sixse.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.five.sixse.R;
import com.five.sixse.helper.Constants;
import com.five.sixse.model.Note;
import com.five.sixse.helper.NoteDBHelper;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layout_manager;
    public NoteListAdapter adapter;
    public ArrayList<Note> noteList;
    public static NoteDBHelper noteDBHelper;
    public SharedPreferences preferences;
    public TextView emptyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Notes");
        noteDBHelper = MainActivity.noteDBHelper;
        emptyMsg = (TextView) findViewById(R.id.emptyMsg);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layout_manager = new LinearLayoutManager(NoteListActivity.this);
        recyclerView.setLayoutManager(layout_manager);
        noteList = new ArrayList<>();
        init();

    }

    public void init() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {
            noteList = noteDBHelper.getAllNote(Constants.VAR_ENG);

        } else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {
            noteList = noteDBHelper.getAllNote(Constants.VAR_HINDI);

        }
        else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_FRENCH)) {
            noteList = noteDBHelper.getAllNote(Constants.VAR_FRENCH);

        }
        if (noteList.size() == 0) {
            emptyMsg.setVisibility(View.VISIBLE);
        }
        adapter = new NoteListAdapter(noteList, NoteListActivity.this);
        recyclerView.setAdapter(adapter);
    }

    /*
    * noteList Adapter
    */
    public class NoteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public ArrayList<Note> noteList;
        public Activity activity;


        public NoteListAdapter(ArrayList<Note> noteList, Activity activity) {
            this.noteList = noteList;
            this.activity = activity;
        }

        public class NoteHolder extends RecyclerView.ViewHolder {
            TextView v_title, n_title, no;
            CardView cardView;
            ImageView delete;

            public NoteHolder(View itemView) {
                super(itemView);
                v_title = (TextView) itemView.findViewById(R.id.v_title);
                n_title = (TextView) itemView.findViewById(R.id.n_title);
                no = (TextView) itemView.findViewById(R.id.no);
                cardView = (CardView) itemView.findViewById(R.id.cardView);
                delete = (ImageView) itemView.findViewById(R.id.delete);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notelist_layout, parent, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            NoteHolder noteHolder = (NoteHolder) holder;
            final Note note = noteList.get(position);

           /* preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {
                noteHolder.v_title.setText(note.getV_title());
                noteHolder.no.setText(String.valueOf(position+1));

            } else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {
                noteHolder.v_title.setText(note.getV_title());
                noteHolder.no.setText(String.valueOf(position+1));

            }
            else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_FRENCH)) {
                noteHolder.v_title.setText(note.getV_title());
                noteHolder.no.setText(String.valueOf(position+1));

            }*/
            noteHolder.v_title.setText(note.getV_title());
            noteHolder.no.setText(String.valueOf(position+1));
            noteHolder.no.setBackgroundResource(R.drawable.txt_bg);
            noteHolder.no.setTextColor(Color.WHITE);
            noteHolder.no.setPadding(10, 5, 10, 5);
            noteHolder.n_title.setText(note.getNote());
            noteHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(NoteListActivity.this, NoteAddActivity.class);
                    intent.putExtra("v_id", Integer.parseInt(note.getV_id()));
                    intent.putExtra("activity", "list");
                    startActivity(intent);
                }
            });
            noteHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setTitle("Delete note");
                    alert.setMessage("Are you sure you want to delete a note?");
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //delete nite from table
                            noteDBHelper.delete_note(note.getV_id());
                            noteList.remove(position);
                            adapter.notifyDataSetChanged();
                            notifyDataSetChanged();
                        }
                    });
                    alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // close dialog
                            dialog.cancel();
                        }
                    });
                    alert.show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return noteList.size();
        }
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
        super.onBackPressed();
    }
}
