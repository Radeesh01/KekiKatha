package com.five.sixse.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.five.sixse.R;
import com.five.sixse.adapter.CategoryListAdapter;
import com.five.sixse.model.Category;
import com.five.sixse.helper.Constants;
import com.five.sixse.helper.BookmarkDBHelper;
import com.five.sixse.helper.MainDBHelper;
import com.five.sixse.helper.NoteDBHelper;
import com.five.sixse.helper.Utility;

import java.io.IOException;
import java.util.ArrayList;

import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    public Toolbar toolBar;
    private RecyclerView recyclerView;
    public RecyclerView.LayoutManager layout_manager;
    public FloatingActionButton indicator, noteListFabBotton, bookmarkFabBotton;
    public static MainDBHelper dba;
    public static BookmarkDBHelper bookmarkDbHelper;
    public static NoteDBHelper noteDBHelper;
    public ArrayList<Category> categoryArrayList;
    CategoryListAdapter adapter;
    AlertDialog alertDialog;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Constants.status = true;
        bookmarkDbHelper = new BookmarkDBHelper(getApplicationContext());
        dba = new MainDBHelper(getApplicationContext());
        noteDBHelper = new NoteDBHelper(getApplicationContext());
        try {
            //create database
            dba.createDB();
            bookmarkDbHelper.createDatabase();
            noteDBHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        indicator = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.my_list);
        layout_manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout_manager);
        categoryArrayList = new ArrayList<>();
        init();

        //get last read chapter
        indicator.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (Utility.getmain("Main", getApplicationContext()) == null) {
                    Toast.makeText(getApplicationContext(), "Indicator not set!", Toast.LENGTH_LONG).show();
                } else {
                    Constants.subList = true;
                    Utility.setBoolean("isindicator", true, getApplicationContext());
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    intent.putExtra("id", "" + Integer.parseInt(Utility.getmain("Main", getApplicationContext())));
                    Constants.category = "" + Utility.getmain("Main", getApplicationContext());
                    Constants.mainid = Integer.parseInt(Utility.getmain("Main", getApplicationContext()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    //finish();
                }
            }
        });
        noteListFabBotton = (FloatingActionButton) findViewById(R.id.note_fab);
        noteListFabBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteListActivity.class);
                Constants.category = "note";
                startActivity(intent);
            }
        });

        bookmarkFabBotton = (FloatingActionButton) findViewById(R.id.bookmark_fab);
        bookmarkFabBotton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.subList = true;
                Intent intent = new Intent(MainActivity.this, BookmarkListActivity.class);
                intent.putExtra("activity", "main");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }

    public void init() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {
            Constants.radio = R.id.lang_eng;
            categoryArrayList = dba.getMainCategory(Constants.VAR_MAIN_CATEGORY_HINDI);

        } else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {
            Constants.radio = R.id.lang_eng;
            categoryArrayList = dba.getMainCategory(Constants.VAR_MAIN_CATEGORY_ENG);

        }
        else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_FRENCH)) {
            Constants.radio = R.id.lang_eng;
            categoryArrayList = dba.getMainCategory(Constants.VAR_MAIN_CATEGORY_FRENCH);

        }
        adapter = new CategoryListAdapter(this, categoryArrayList);
        recyclerView.setAdapter(adapter);
    }


    /*
  * Language Change Dialog
  */
    public void LanguageChangeDialog() {
        AlertDialog.Builder lang_dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater1 = getLayoutInflater();
        View dialogView = inflater1.inflate(R.layout.language_dialog, null);
        lang_dialog.setView(dialogView);
        lang_dialog.setTitle("Select Language");

        Drawable color = new ColorDrawable(Color.WHITE);
        color.setAlpha(200);
        final RadioGroup lang_select = (RadioGroup) dialogView.findViewById(R.id.language_selection);
        lang_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int lang_id = lang_select.getCheckedRadioButtonId();
                if (lang_id == R.id.lang_eng) {
                    languageChangeMethod(MainActivity.this, lang_id);
                    Constants.radio = R.id.lang_eng;
                }


            }
        });

        lang_select.check(Constants.radio);
        Button lang_ok = (Button) dialogView.findViewById(R.id.lang_ok);
        alertDialog = lang_dialog.create();
        alertDialog.show();
        lang_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
            }
        });
    }

    /*
    * Language Change Method
    */
    public void languageChangeMethod(final Activity context, final int lang_id) {

        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (lang_id == R.id.lang_eng) {
                editor.putString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG);
            }
            else if (lang_id == R.id.lang_eng) {
                editor.putString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_HINDI);
            }
            else if(lang_id==R.id.lang_eng){
                editor.putString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_FRENCH);
            }
            editor.commit();
            init();

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;//return true so that the menu pop up is opened
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            //search chapter from all category
            case R.id.search:
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("id", "0");
                intent.putExtra("title", "");
                Constants.category = "all_search";
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.language:
                LanguageChangeDialog();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void onBack() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle("Keti Katha")
                .setMessage("Are you sure you want to close ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("NO", null)
                .show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }



}
