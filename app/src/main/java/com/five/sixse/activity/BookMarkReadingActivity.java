package com.five.sixse.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.five.sixse.R;
import com.five.sixse.fragment.BookmarkReadingFragment;

public class BookMarkReadingActivity extends AppCompatActivity {

    int id;
    public static SeekBar seekBar;
    public static ImageView playPause;
    public Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public static Toolbar toolBar;
    public static FloatingActionButton noteAddFab, bookmark_fab;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        id = Integer.parseInt(b.getString("catId"));
        getSupportActionBar().setTitle("Bookmark");
        init();

        playPause = (ImageView) findViewById(R.id.tgbtnScrollingSpeed);
        seekBar = (SeekBar) findViewById(R.id.seekbarScrollSpeed);
        noteAddFab = (FloatingActionButton) findViewById(R.id.fab);
        bookmark_fab = (FloatingActionButton) findViewById(R.id.bm_list_fab);
        bookmark_fab.setVisibility(View.GONE);
    }


    public void init() {

        fragmentManager = BookMarkReadingActivity.this.getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new BookmarkReadingFragment();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //Write your logic here
            //finish();
            case android.R.id.home:

                onBackPressed();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(BookMarkReadingActivity.this, BookmarkListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}
