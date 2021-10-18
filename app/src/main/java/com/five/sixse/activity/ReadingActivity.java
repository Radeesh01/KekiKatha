package com.five.sixse.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import com.five.sixse.fragment.ReadingFragment;
import com.five.sixse.helper.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class ReadingActivity extends AppCompatActivity {

    int id;
    public String actionBarTitle;
    public static SeekBar seekBar;

    public static ImageView playPause;
    public Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public static Toolbar toolBar;
    public static FloatingActionButton noteAddFab, bookmarkFabButton;
    public SharedPreferences preferences;
    public InterstitialAd mInterstitialAd;

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
        setActionBarTitle();
        init();

        playPause = (ImageView) findViewById(R.id.tgbtnScrollingSpeed);
        seekBar = (SeekBar) findViewById(R.id.seekbarScrollSpeed);
        noteAddFab = (FloatingActionButton) findViewById(R.id.fab);
        bookmarkFabButton = (FloatingActionButton) findViewById(R.id.bm_list_fab);
        bookmarkFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.subList = true;
                Intent intent = new Intent(ReadingActivity.this, BookmarkListActivity.class);
                intent.putExtra("activity", "reading");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    public void setActionBarTitle() {
        preferences = PreferenceManager.getDefaultSharedPreferences(ReadingActivity.this);
        if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {
            actionBarTitle = MainActivity.dba.getActionBarTitle(id, Constants.VAR_MAIN_CATEGORY_ENG);
            getSupportActionBar().setTitle(actionBarTitle);

        } else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {
            actionBarTitle = MainActivity.dba.getActionBarTitle(id, Constants.VAR_MAIN_CATEGORY_HINDI);
            getSupportActionBar().setTitle(actionBarTitle);
        }
        else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_FRENCH)) {
            actionBarTitle = MainActivity.dba.getActionBarTitle(id, Constants.VAR_MAIN_CATEGORY_FRENCH);
            getSupportActionBar().setTitle(actionBarTitle);
        }
    }

    public void init() {

        fragmentManager = ReadingActivity.this.getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new ReadingFragment();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();

    }

    public void displayInterstitial() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else if (!mInterstitialAd.isLoaded()) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
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


        if (Constants.category.equals("note")) {
            super.onBackPressed();
            finish();
        } else {
            Intent intent = new Intent(ReadingActivity.this, ListActivity.class);
            intent.putExtra("id", "" + id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        displayInterstitial();
        super.onBackPressed();
    }
}
