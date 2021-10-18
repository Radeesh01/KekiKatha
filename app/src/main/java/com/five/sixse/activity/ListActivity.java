package com.five.sixse.activity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.five.sixse.R;
import com.five.sixse.fragment.ListFragment;
import com.five.sixse.helper.Constants;
import com.five.sixse.helper.Utility;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.reflect.Field;

import static com.five.sixse.fragment.ListFragment.view;


public class ListActivity extends AppCompatActivity {

    public Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public Toolbar toolBar;
    public int id;
    public AlertDialog alertDialog;
    BottomNavigationView navigation;
    RadioGroup lang_select;
    public String actionBarTitle;
    public SharedPreferences preferences;
    public InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(navigation);
        Bundle b = getIntent().getExtras();
        id = Integer.parseInt(b.getString("id"));
        setActionBarTitle();
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        init();
    }

    public void setActionBarTitle() {

        preferences = PreferenceManager.getDefaultSharedPreferences(ListActivity.this);
        if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {
            actionBarTitle = MainActivity.dba.getActionBarTitle(id, Constants.VAR_MAIN_CATEGORY_ENG);
            getSupportActionBar().setTitle(actionBarTitle);
        }
        else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {
            actionBarTitle = MainActivity.dba.getActionBarTitle(id, Constants.VAR_MAIN_CATEGORY_HINDI);
            getSupportActionBar().setTitle(actionBarTitle);
        }
        else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_FRENCH)) {
            actionBarTitle = MainActivity.dba.getActionBarTitle(id, Constants.VAR_MAIN_CATEGORY_FRENCH);
            getSupportActionBar().setTitle(actionBarTitle);
        }
    }

    //Second page ........................................
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void init() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            fragmentManager = ListActivity.this.getFragmentManager();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        fragment = new ListFragment();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            fragmentTransaction.replace(R.id.frame_container, fragment);
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.invalidateOptionsMenu();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.invalidateOptionsMenu();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.menu_language_selection:
                    LanguageChangeDialog();
                    break;

                case R.id.app_share:
                    try {
                        Intent shareAppIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareAppIntent.setType("text/plain");
                        shareAppIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                        startActivity(Intent.createChooser(shareAppIntent, "Share via"));

                    } catch (Exception e) {
                        Log.e("Share error", e.getMessage());
                    }
                    break;
                case R.id.menu_rateus:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    startActivity(intent);
                    break;
                case R.id.bookmark_list:
                    Constants.subList = true;
                    Intent bookmarkIntent = new Intent(ListActivity.this, BookmarkListActivity.class);
                    bookmarkIntent.putExtra("activity", "list");
                    bookmarkIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    bookmarkIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(bookmarkIntent);
                    break;
            }

            return true;
        }
    };

    /*
    * Bottom Navigation Menu with title if menu more than 3
    */
    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }


    /*
    * language Change Dialog
    */
    public void LanguageChangeDialog() {
        //subFragment = ListFragment.this;
        AlertDialog.Builder lang_dialog = new AlertDialog.Builder(ListActivity.this);
        LayoutInflater inflater1 = getLayoutInflater();
        View dialogView = inflater1.inflate(R.layout.language_dialog, null);
        lang_dialog.setView(dialogView);
        lang_dialog.setTitle("Select Language");

        Drawable color = new ColorDrawable(Color.WHITE);
        color.setAlpha(200);
        lang_select = (RadioGroup) dialogView.findViewById(R.id.language_selection);
        lang_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int lang_id = lang_select.getCheckedRadioButtonId();
                if (lang_id == R.id.lang_eng) {
                    Utility.languageChangeMethod(getApplicationContext(), view, fragment, lang_id);
                    Constants.radio = R.id.lang_eng;
                }

            }
        });

        lang_select.check(Constants.radio);
        Button lang_ok = (Button) dialogView.findViewById(R.id.lang_ok);
        alertDialog = lang_dialog.create();

        lang_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
            }
        });

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
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
    public void onBackPressed() {

        super.onBackPressed();
        //clear filter
        Constants.filter = "";
        Intent intent = new Intent(ListActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
        displayInterstitial();
    }


}