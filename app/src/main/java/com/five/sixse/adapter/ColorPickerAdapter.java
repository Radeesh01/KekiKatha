package com.five.sixse.adapter;

import java.util.ArrayList;
import java.util.List;

import com.five.sixse.R;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ColorPickerAdapter extends BaseAdapter {

    private Context context;
    // list which holds the colors to be displayed
    private List<Integer> colorList = new ArrayList<Integer>();
    // width of grid column
    int colorGridColumnWidth;

    public static String colors[][] = {{"000000", "3E4095", "880203",},
                                        {"A53692", "008000", "7A75B5"},};

    public ColorPickerAdapter(Context context) {
        this.context = context;

        // defines the width of each color square
        colorGridColumnWidth = context.getResources().getInteger(R.integer.colorGridColumnWidth);

        // for convenience and better reading, we place the colors in a two
        // dimension array

        colorList = new ArrayList<Integer>();

        // add the color array to the list
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[i].length; j++) {
                colorList.add(Color.parseColor("#" + colors[i][j]));
            }
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        // can we reuse a view?
        if (convertView == null) {
            imageView = new ImageView(context);
            // set the width of each color square
            imageView.setLayoutParams(new GridView.LayoutParams(colorGridColumnWidth, colorGridColumnWidth));

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setBackgroundColor(colorList.get(position));
        imageView.setId(position);

        return imageView;
    }

    public int getCount() {
        return colorList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
}
