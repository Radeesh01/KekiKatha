package com.five.sixse.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.five.sixse.R;
import com.five.sixse.fragment.BookmarkListFragment;
import com.five.sixse.helper.Constants;
import com.five.sixse.helper.Utility;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.Field;

import static com.five.sixse.fragment.BookmarkListFragment.view;


public class BookmarkListActivity extends AppCompatActivity {
    public Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public Toolbar toolBar;
    public int id;
    public AlertDialog alertDialog;
    BottomNavigationView navigation;
    RadioGroup lang_select;
    public String activity;

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
        getSupportActionBar().setTitle("Bookmark");
        activity = getIntent().getStringExtra("activity");

        //load banner Ads
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        init();
    }

    //Second page ........................................
    public void init() {

        fragmentManager = BookmarkListActivity.this.getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new BookmarkListFragment();
        fragmentTransaction.replace(R.id.frame_container, fragment);
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
    /*        for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
    */
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
        AlertDialog.Builder lang_dialog = new AlertDialog.Builder(BookmarkListActivity.this);
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
        alertDialog.show();
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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        //clear filter
        Constants.filter = "";
        Intent intent;
        switch (activity) {
            case "list":
                intent = new Intent(BookmarkListActivity.this, ListActivity.class);
                break;
            case "reading":
                intent = new Intent(BookmarkListActivity.this, ReadingActivity.class);
                break;
            case "main":
                intent = new Intent(BookmarkListActivity.this, MainActivity.class);
                break;
            default:
                intent = new Intent(BookmarkListActivity.this, MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }


}
