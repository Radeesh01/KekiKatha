package com.five.sixse.helper;

import android.content.Context;
import android.content.res.TypedArray;

import com.five.sixse.R;


public class Utils {

    /*to get toolbar height when use layout behavior with coordinate layout
    */
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }


}
