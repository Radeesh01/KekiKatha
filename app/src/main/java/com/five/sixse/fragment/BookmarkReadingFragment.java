package com.five.sixse.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.five.sixse.R;

import com.five.sixse.activity.BookMarkReadingActivity;
import com.five.sixse.activity.MainActivity;
import com.five.sixse.adapter.BookmarkReadingAdapter;
import com.five.sixse.adapter.BookmarkListAdapter;
import com.five.sixse.model.Book;
import com.five.sixse.helper.BookmarkDBHelper;
import com.five.sixse.helper.Constants;
import com.five.sixse.helper.MainDBHelper;
import com.five.sixse.helper.Utility;

import java.util.ArrayList;


public class BookmarkReadingFragment extends Fragment {

    public static ViewPager pager;
    public BookmarkReadingAdapter adapter;
    public Context mContext;
    public ArrayList<Book> bookArrayList;
    public Book book;
    public static View view;
    public Fragment fragment = null;
    private FragmentTransaction ft = null;
    private SharedPreferences preferences;
    private Menu menu;
    public Point p;
    private int isfav = 0;
    private MainDBHelper dba = MainActivity.dba;
    private BookmarkDBHelper bookmarkDbHelper = MainActivity.bookmarkDbHelper;
    public AlertDialog alertDialog;
    public String id;
    int selected;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            final View view = inflater.inflate(R.layout.fragment_detail, container, false);
            this.view = view;
            isfav = 0;
            mContext = this.getActivity();
            pager = (ViewPager) view.findViewById(R.id.viewPager);
            fragment = BookmarkReadingFragment.this;
            setHasOptionsMenu(true);
            bookArrayList = BookmarkListAdapter.bookArrayList;

            Bundle b = getActivity().getIntent().getExtras();
            selected = Integer.parseInt(b.getString("position"));

           /* Bundle bundle = getActivity().getIntent().getExtras();
            id = bundle.getString("id");*/

            init();
            pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {

                    selected = position;
                    isfav = bookmarkDbHelper.getBookmarks(bookArrayList.get(selected).getId());

                    if (isfav == bookArrayList.get(selected).getId()) {
                        bookItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark));
                        bookItem.setTitle(getString(R.string.bookmarked));
                    } else {
                        bookItem.setIcon(getResources().getDrawable(R.drawable.ic_unbook));
                        bookItem.setTitle(getString(R.string.unbook));
                    }
                    if (Utility.getmain("Main", getActivity()) == null) {
                    } else if (Integer.parseInt(Utility.getcategory("Category", getActivity())) == bookArrayList.get(selected).getCatId()) {
                        if (Integer.parseInt(Utility.getsubid("Subid", getActivity())) == (selected)) {
                            menuItem.setIcon(R.drawable.ic_pin);
                        } else {
                            menuItem.setIcon(R.drawable.ic_unpin);
                        }
                    }
                    if (Utility.scrollTimer != null) {
                        Utility.stopAutoScrolling();
                        view.findViewById(R.id.vertical_scrollview_id).scrollTo(0, 0);
                        BookMarkReadingActivity.seekBar.setProgress(0);
                    }
                    if (BookMarkReadingActivity.seekBar.getProgress() != 0) {
                        BookMarkReadingActivity.seekBar.setProgress(0);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            book = bookArrayList.get(selected);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


   /* void showactionbarmenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(getActivity());
            java.lang.reflect.Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");

            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            // presumably, not relevant
        }
    }*/

    public void init() {

        mContext = this.getActivity();

        try {
            fragment = BookmarkReadingFragment.this;
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {

                bookArrayList = BookmarkListAdapter.bookArrayList;
                adapter = new BookmarkReadingAdapter(getActivity(), bookArrayList);
                pager.setAdapter(adapter);
                pager.setCurrentItem(selected, true);

            } else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {

                bookArrayList = BookmarkListAdapter.bookArrayList;
                adapter = new BookmarkReadingAdapter(getActivity(), bookArrayList);
                pager.setAdapter(adapter);
                pager.setCurrentItem(selected, true);

            }
            else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_FRENCH)) {

                bookArrayList = BookmarkListAdapter.bookArrayList;
                adapter = new BookmarkReadingAdapter(getActivity(), bookArrayList);
                pager.setAdapter(adapter);
                pager.setCurrentItem(selected, true);

            }


            try {
                if (bookArrayList.size() != 0) {
                    isfav = bookmarkDbHelper.getBookmarks(bookArrayList.get(selected).getId());
                    if (isfav == bookArrayList.get(selected).getId()) {
                        bookItem.setIcon(R.drawable.ic_bookmark);
                        bookItem.setTitle(getString(R.string.bookmarked));
                    } else {
                        bookItem.setIcon(R.drawable.ic_unbook);
                        bookItem.setTitle(getString(R.string.unbook));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), "error" + e, Toast.LENGTH_LONG).show();
        }


    }

    private MenuItem bookItem, menuItem;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
        this.menu = menu;
        bookItem = menu.findItem(R.id.bookmark);
        menuItem = menu.findItem(R.id.indicator).setVisible(false);
        try {
            if (bookArrayList.size() != 0) {
                isfav = bookmarkDbHelper.getBookmarks(bookArrayList.get(selected).getId());

                if (isfav == bookArrayList.get(selected).getId()) {
                    bookItem.setIcon(R.drawable.ic_bookmark);
                    bookItem.setTitle(getString(R.string.bookmarked));
                } else {
                    bookItem.setIcon(R.drawable.ic_unbook);
                    bookItem.setTitle(getString(R.string.unbook));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.play_screen_menu, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            if (bookArrayList.size() != 0) {
                if (isfav == bookArrayList.get(selected).getId()) {
                    bookItem.setIcon(R.drawable.ic_bookmark);
                    bookItem.setTitle(getString(R.string.bookmarked));
                } else {
                    bookItem.setIcon(R.drawable.ic_unbook);
                    bookItem.setTitle(getString(R.string.unbook));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (item.getItemId()) {

            case R.id.brightness:

                int[] location = new int[2];
                p = new Point();
                p.x = location[0];
                p.y = location[1];
                showPopup(p);
                break;

            case R.id.colorPick:
                Utility.colorPickerDialog(mContext, view, fragment);
                break;

            case R.id.textSize:
                Utility.fontSizeDialog(mContext, view, fragment);
                break;

            case R.id.language_selection:
                LanguageChangeDialog();
                break;

            case R.id.menu_resetDefault:
                Utility.setDefaultSetting(mContext, view, fragment);
                break;

            case R.id.bookmark:
                if (bookItem.getTitle().equals(getString(R.string.unbook))) {
                    bookmarkDbHelper.insertIntoDB(bookArrayList.get(selected).getId());
                    bookItem.setIcon(R.drawable.ic_bookmark);
                    bookItem.setTitle(getString(R.string.bookmarked));
                    init();


                } else if (bookItem.getTitle().equals(getString(R.string.bookmarked))) {
                    bookmarkDbHelper.delete_id(bookArrayList.get(selected).getId());
                    bookItem.setIcon(R.drawable.ic_unbook);
                    bookItem.setTitle(getString(R.string.unbook));
                    BookmarkListAdapter.bookArrayList.remove(selected);
                    adapter.notifyDataSetChanged();
                    init();


                }
                break;

        }

        return super.onOptionsItemSelected(item);

    }


    private void showPopup(Point p) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getActivity())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getActivity().getPackageName()));
                getActivity().startActivityForResult(intent, 200);
            } else {
                BrightnessPopup();
            }
        } else {
            BrightnessPopup();
        }
    }

    public void BrightnessPopup() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int popupWidth = size.x;

        //int popupHeight = actionBar.getHeight();
        SeekBar seekBar;
        final PopupWindow popup = new PopupWindow(getActivity());

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) getActivity().findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.seekbar, viewGroup);

        seekBar = (SeekBar) layout.findViewById(R.id.seekBar1);
        seekBar.setMax(255);
        Settings.System.putInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        float curBrightnessValue = 0;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(getActivity().getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        int screen_brightness = (int) curBrightnessValue;

        seekBar.setProgress(screen_brightness);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue,
                                          boolean fromUser) {
                progress = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here,
                // if you want to do anything at the start of
                // touching the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                android.provider.Settings.System.putInt(getActivity().getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        progress);
            }
        });

        // Creating the PopupWindow

        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        //popup.setHeight(popupHeight);
        popup.setFocusable(true);


        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        // Getting a reference to Close button, and close the popup when clicked.
    }

    public void LanguageChangeDialog() {
        AlertDialog.Builder lang_dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater1 = getActivity().getLayoutInflater();
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
                    Utility.languageChangeMethod(mContext, view, fragment, lang_id);
                    Constants.radio = R.id.lang_eng;
                } else if (lang_id == R.id.lang_eng) {
                    Utility.languageChangeMethod(mContext, view, fragment, lang_id);
                    Constants.radio = R.id.lang_eng;
                } else if (lang_id == R.id.lang_eng) {
                    Utility.languageChangeMethod(mContext, view, fragment, lang_id);
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
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
