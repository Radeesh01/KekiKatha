package com.five.sixse.helper;

import java.util.HashMap;

import android.graphics.Color;

public class Constants {

    // Media controller

    public static long DELAY = 113;
    public static double SPEED = 1.0;

    public static boolean subList = false;

    // preferences strings
    public static final String PREF_NAME = "";
    public static final String PREF_TEXTSIZE = "fontSizePref";
    public static final String PREF_TEXTCOLOR = "textColorPref";
    public static final String PREF_LANGUAGE = "lang_pref";
    public static final String PREF_LANGUAGE_ENG = "lang_eng";
    public static final String PREF_LANGUAGE_HINDI = "lang_hindi";
    public static final String PREF_LANGUAGE_FRENCH="lang_french";


    //Table Name
    public static final String VAR_ENG = "var_eng";
    public static final String VAR_HINDI = "var_hindi";
    public static final String VAR_FRENCH="var_french";
    public static final String VAR_MAIN_CATEGORY_ENG = "main_category_eng";
    public static final String VAR_MAIN_CATEGORY_HINDI = "main_category_hindi";
    public static final String VAR_MAIN_CATEGORY_FRENCH = "main_category_french";

    public static int radio;

    //max text size
    public static final String TEXTSIZE_MAX = "35";

    //minimum text size
    public static final String TEXTSIZE_MIN = "18";

    //default color of textView
    public static final int TEXT_DEF_COLOR = Color.BLACK;
    public static boolean status = false;

    public static int id;
    public static HashMap<Integer, String> ids = new HashMap<Integer, String>();

    //activity_main id value
    public static int mainid = 0;

    //default value of searchView query
    public static String filter = "";

    //we will identify category keyword for all_search and and note
    public static String category;


    //public static String filterTitle;


}
