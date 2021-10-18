package com.five.sixse.adapter;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.SeekBar;
import android.widget.TextView;


import com.five.sixse.activity.NoteAddActivity;
import com.five.sixse.activity.MainActivity;
import com.five.sixse.activity.ReadingActivity;
import com.five.sixse.fragment.ReadingFragment;
import com.five.sixse.model.Book;
import com.five.sixse.R;
import com.five.sixse.helper.Constants;
import com.five.sixse.helper.Utility;

import java.text.Normalizer;
import java.util.ArrayList;


import static com.five.sixse.activity.ReadingActivity.seekBar;
import static com.five.sixse.activity.ReadingActivity.playPause;

public class ReadingAdapter extends PagerAdapter {


    public static Activity activity;
    private ArrayList<Book> chapterList;
    private LayoutInflater inflater;
    private TextView tvTitle;

    public static NestedScrollView scrollView;
    public LinearLayout linearLayout;
    public static TextView tvDescription;
    private static boolean isMoveingSeekBar = false;
    private SharedPreferences preferences;
    private String search, searchHighlight;
    private String title, description;

    public ReadingAdapter(Activity activity, ArrayList<Book> chapterList) {

        this.activity = activity;
        this.chapterList = chapterList;
        this.search = Constants.filter;
        this.inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View instantiateItem(ViewGroup container, final int position) {


        final View view = inflater.inflate(R.layout.view_pager_layout, container, false);
        tvDescription = (TextView) view.findViewById(R.id.tvDisplayDetails);
        tvTitle = (TextView) view.findViewById(R.id.play_title);
        scrollView = (NestedScrollView) view.findViewById(R.id.vertical_scrollview_id);
        scrollView.setTag("play");
        linearLayout = (LinearLayout) view.findViewById(R.id.vertical_outer_layout_id);

        playPause.setTag("play");
        playPause.setImageResource(R.drawable.ic_play);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chapterList.size() != 0) {
                    String tag = playPause.getTag().toString();
                    String tag1 = scrollView.getTag().toString();

                    if (tag.equalsIgnoreCase("play") && tag1.equalsIgnoreCase("play")) {
                        scrollView = (NestedScrollView) view.findViewWithTag(scrollView.getTag().toString());
                        Utility.startAutoScrolling(activity, (NestedScrollView) ReadingFragment.view.findViewById(R.id.vertical_scrollview_id));
                        playPause.setImageResource(R.drawable.ic_pause);
                        playPause.setTag("pause");
                        scrollView.setTag("pause");
                    } else {

                        Utility.stopAutoScrolling();
                        playPause.setImageResource(R.drawable.ic_play);
                        playPause.setTag("play");
                        scrollView.setTag("play");
                    }
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isMoveingSeekBar = false;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isMoveingSeekBar = true;

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (chapterList.size() != 0) {
                    if (isMoveingSeekBar) {

                        Utility.startAutoScrolling(activity, (NestedScrollView) ReadingFragment.view.findViewById(R.id.vertical_scrollview_id));
                        Constants.SPEED = progress / 20;
                        if (Utility.scrollTimer != null) {
                            playPause.setImageResource(R.drawable.ic_pause);
                        }
                        scrollView.setTag("pause");
                    }
                }
            }
        });


        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {
            title = MainActivity.dba.getTitle(chapterList.get(ReadingFragment.pager.getCurrentItem()).getId(), Constants.VAR_ENG);
            description = MainActivity.dba.getDescription(chapterList.get(position).getId(), Constants.VAR_ENG);
            tvTitle.setText(title);
            tvDescription.setText(description);
            SearchHightLightText(description);

        } else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {

            title = MainActivity.dba.getTitle(chapterList.get(ReadingFragment.pager.getCurrentItem()).getId(), Constants.VAR_HINDI);
            description = MainActivity.dba.getDescription(chapterList.get(position).getId(), Constants.VAR_HINDI);

            tvTitle.setText(title);
            tvDescription.setText(description);
            SearchHightLightText(description);
        }
        else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_FRENCH)) {

            title = MainActivity.dba.getTitle(chapterList.get(ReadingFragment.pager.getCurrentItem()).getId(), Constants.VAR_FRENCH);
            description = MainActivity.dba.getDescription(chapterList.get(position).getId(), Constants.VAR_FRENCH);

            tvTitle.setText(title);
            tvDescription.setText(description);
            SearchHightLightText(description);
        }

        //method  call when change textSize or color
        Utility.setCurrentSetting(tvDescription, tvTitle, activity);

        ReadingActivity.noteAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, NoteAddActivity.class);
                intent.putExtra("v_id", chapterList.get(ReadingFragment.pager.getCurrentItem()).getId());
                intent.putExtra("activity", "add");
                activity.startActivity(intent);

            }
        });

        ViewTreeObserver vTreeObserver = linearLayout.getViewTreeObserver();

        vTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utility.getScrollMaxAmount(linearLayout);
            }
        });

        // System.out.println("book id  :    " + sliderList.get(position).getId());
        ((ViewPager) container).addView(view);
        return view;
    }

    public void SearchHightLightText(String originalText) {
        if (searchHighlight != null && !searchHighlight.equalsIgnoreCase("")) {
            String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            int start = normalizedText.indexOf(searchHighlight);
            if (start < 0) {
                // return originalText;
            } else {
                Spannable highlighted = new SpannableString(originalText);
                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + searchHighlight.length(), originalText.length());

                    highlighted.setSpan(new BackgroundColorSpan(activity.getResources().getColor(R.color.colorPrimary)), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    highlighted.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.off_white)), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = normalizedText.indexOf(searchHighlight, spanEnd);
                }
                tvDescription.setText(highlighted);
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    /*
           Get searchable from search view
           */
    public void setFilter(String search) {
        this.searchHighlight = search.toString().toLowerCase();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return chapterList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);

    }


}
