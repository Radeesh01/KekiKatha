package com.five.sixse.helper;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


import com.five.sixse.R;

import com.five.sixse.activity.MainActivity;
import com.five.sixse.adapter.ColorPickerAdapter;
import com.five.sixse.fragment.BookmarkListFragment;
import com.five.sixse.fragment.BookmarkReadingFragment;
import com.five.sixse.fragment.ReadingFragment;
import com.five.sixse.fragment.ListFragment;


public class Utility extends MainActivity {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static int verticalScrollMax;
    public static Timer scrollTimer = null;
    public static TimerTask scrollerSchedule;
    public static FragmentTransaction ft = null;
    public static Handler handler = new Handler();
    public static int selectedTextColor = 0;
    public static AlertDialog alertDialog;

    /*
    * color picker dialog method
    */
    public static void colorPickerDialog(final Context context, final View view, final Fragment fragment) {

        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("set color");
            LayoutInflater inflater1 = fragment.getActivity().getLayoutInflater();
            View dialogView = inflater1.inflate(R.layout.color_picker, null);
            dialog.setView(dialogView);

            alertDialog = dialog.create();
            alertDialog.show();
            GridView gridViewColors = (GridView) dialogView.findViewById(R.id.gridViewColors);
            gridViewColors.setAdapter(new ColorPickerAdapter(context));

            gridViewColors.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    if (position == 0) {
                        selectedTextColor = Color.parseColor("#" + ColorPickerAdapter.colors[0][0]);
                    } else if (position == 1) {

                        selectedTextColor = Color.parseColor("#" + ColorPickerAdapter.colors[0][1]);
                    } else if (position == 2) {
                        selectedTextColor = Color.parseColor("#" + ColorPickerAdapter.colors[0][2]);

                    } else if (position == 3) {
                        selectedTextColor = Color.parseColor("#" + ColorPickerAdapter.colors[1][0]);

                    } else if (position == 4) {
                        selectedTextColor = Color.parseColor("#" + ColorPickerAdapter.colors[1][1]);

                    } else if (position == 5) {
                        selectedTextColor = Color.parseColor("#" + ColorPickerAdapter.colors[1][2]);

                    } else {
                        alertDialog.dismiss();
                    }
                    editor = sharedPreferences.edit();
                    //selected color save in shared preferences
                    editor.putInt(Constants.PREF_TEXTCOLOR, selectedTextColor);
                    editor.commit();

                    FragmentManager mgr = ((Activity) context)
                            .getFragmentManager();
                    mgr.getBackStackEntryCount();
                    if (fragment instanceof ReadingFragment) {
                        ((ReadingFragment) fragment).init();
                    } else if (fragment instanceof BookmarkReadingFragment) {
                        ((BookmarkReadingFragment) fragment).init();
                    }


                    alertDialog.dismiss();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Change Font Size
    */
    public static void fontSizeDialog(final Context context, final View view, final Fragment fragment) {

        String changedFontSize;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        changedFontSize = sharedPreferences.getString(Constants.PREF_TEXTSIZE, Constants.TEXTSIZE_MIN);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Change font Size");
        LayoutInflater inflater1 = fragment.getActivity().getLayoutInflater();
        View dialogView = inflater1.inflate(R.layout.dialog_font_size, null);
        dialog.setView(dialogView);

        alertDialog = dialog.create();
        Button btnOk = (Button) dialogView.findViewById(R.id.btnYes);
        final EditText edt_font_size_value = (EditText) dialogView.findViewById(R.id.edt_font_size_value);
        final SeekBar skBar_value = (SeekBar) dialogView.findViewById(R.id.skBar_value);

        skBar_value.setMax(17);
        skBar_value.setProgress(Integer.parseInt(changedFontSize) - 18);
        edt_font_size_value.setText(changedFontSize);
        edt_font_size_value.setSelection(edt_font_size_value.getText().toString().length());

        skBar_value.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                edt_font_size_value.setText(Integer.toString(progress + 18));
                edt_font_size_value.setSelection(edt_font_size_value.getText().toString().length());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                editor = sharedPreferences.edit();
                if (Integer.parseInt(edt_font_size_value.getText().toString().trim()) >= 35) {
                    edt_font_size_value.setText(Constants.TEXTSIZE_MAX);
                    editor.putString(Constants.PREF_TEXTSIZE, Constants.TEXTSIZE_MAX);

                } else if (Integer.parseInt(edt_font_size_value.getText().toString().trim()) < 18) {
                    edt_font_size_value.setText(Constants.TEXTSIZE_MIN);
                    editor.putString(Constants.PREF_TEXTSIZE, Constants.TEXTSIZE_MIN);

                } else
                    editor.putString(Constants.PREF_TEXTSIZE, edt_font_size_value.getText().toString().trim());

                editor.commit();

                if (fragment instanceof ReadingFragment) {
                    ((ReadingFragment) fragment).init();
                } else if (fragment instanceof BookmarkReadingFragment) {
                    ((BookmarkReadingFragment) fragment).init();
                }
            }

        });

        edt_font_size_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentProgress = edt_font_size_value.getText().toString().trim();
                if (!currentProgress.equals("")) {
                    skBar_value.setProgress(Integer.parseInt(currentProgress) - 18);
                    edt_font_size_value.setSelection(edt_font_size_value.getText().toString().length());
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    /*
    * apply current change when change color OR text Size
    */
    public static void setCurrentSetting(TextView description, TextView today, Context context) {
        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            today.setTextColor(sharedPreferences.getInt(Constants.PREF_TEXTCOLOR, Constants.TEXT_DEF_COLOR));
            description.setTextSize(Integer.valueOf(sharedPreferences.getString(
                    Constants.PREF_TEXTSIZE, Constants.TEXTSIZE_MIN)));
            description.setTextColor(sharedPreferences.getInt(
                    Constants.PREF_TEXTCOLOR, Constants.TEXT_DEF_COLOR));

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    /*
    * set default color,textSize and Language
    */
    public static void setDefaultSetting(final Context context,
                                         final View view, final Fragment fragment) {

        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle(context.getResources().getText(
                    R.string.settingResetTitleMsg));
            alertDialog.setMessage(context.getResources().getText(
                    R.string.settingResetMsg));
            alertDialog.setIcon(R.drawable.ic_reset);
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            editor = sharedPreferences.edit();
                            editor.putString(Constants.PREF_TEXTSIZE, Constants.TEXTSIZE_MIN);
                            editor.putInt(Constants.PREF_TEXTCOLOR, Constants.TEXT_DEF_COLOR);
                            editor.putString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG);

                            editor.commit();

                            if (fragment instanceof ReadingFragment) {
                                ((ReadingFragment) fragment).init();
                            }

                            dialog.dismiss();
                        }
                    });

            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }


    /*
    * Scroll Content inside detail fragment
    */
    public static void startAutoScrolling(final Activity context, final NestedScrollView scrollView) {
        try {
            scrollTimer = null;
            if (scrollTimer == null) {
                scrollTimer = new Timer();
                final Runnable Timer_Tick = new Runnable() {
                    public void run() {
                        int scrollPos = 0;
                        scrollPos = (int) (scrollView.getScrollY() + Constants.SPEED);
                        if (scrollPos >= verticalScrollMax) {
                            stopAutoScrolling();
                            scrollPos = 0;
                        }
                        scrollView.scrollTo(0, scrollPos);
                    }
                };
                if (scrollerSchedule != null) {
                    scrollerSchedule.cancel();
                    scrollerSchedule = null;
                }
                scrollerSchedule = new TimerTask() {
                    @Override
                    public void run() {
                        (context).runOnUiThread(Timer_Tick);
                    }
                };
                scrollTimer.schedule(scrollerSchedule, 30, Constants.DELAY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Stop auto scroll when click pause button button
    */
    public static void stopAutoScrolling() {
        if (scrollTimer != null) {
            scrollTimer.cancel();
            scrollTimer = null;
        }
    }


    /**
     * Auto ScrollView
     */
    public static void getScrollMaxAmount(LinearLayout OuterLayout) {
        int actualWidth = OuterLayout.getMeasuredHeight();
        verticalScrollMax = actualWidth;
    }

    /*
    * Change Language of reading content and set selected language as current Lang
    */
    public static void languageChangeMethod(final Context context, final View view, final Fragment fragment, final int lang_id) {

        try {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            editor = sharedPreferences.edit();

            if (lang_id == R.id.lang_eng) {
                editor.putString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG);

            }

            editor.apply();
            if (fragment instanceof ListFragment) {
                ((ListFragment) fragment).init();
            } else if (fragment instanceof ReadingFragment) {
                ((ReadingFragment) fragment).init();
            } else if (fragment instanceof BookmarkListFragment) {
                ((BookmarkListFragment) fragment).init();
            } else if (fragment instanceof BookmarkReadingFragment) {
                ((BookmarkReadingFragment) fragment).init();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    * set activity_main id for indicator . when to read the last-read  chapter
    */
    public static void setmain(String key, String value, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getmain(String key, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    /*
    * save CategoryId in preference
    */
    public static void setcategory(String key, String value, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getcategory(String key, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    /*
    * save chapter id or subCategoryId in preference
    */
    public static void setsubid(String key, String value, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getsubid(String key, Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }


    /*
    * set indicator value
    */
    public static void setBoolean(String key, boolean value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("isindicator", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, Context context) {
        // TODO Auto-generated method stub
        SharedPreferences preferences = context.getSharedPreferences("isindicator", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
}

