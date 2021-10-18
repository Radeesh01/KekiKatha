package com.five.sixse.adapter;

import java.util.ArrayList;
import java.util.Locale;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.five.sixse.R;
import com.five.sixse.activity.ReadingActivity;
import com.five.sixse.helper.Constants;
import com.five.sixse.model.Book;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static ArrayList<Book> bookArrayList;
    private Activity context;
    private SharedPreferences preferences;
    private String search;


    //constructor
    public ListAdapter(Activity context,
                       ArrayList<Book> data) {
        this.bookArrayList = data;
        this.context = context;
        search = Constants.filter;

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_list, parent, false);
        return new IndexHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        IndexHolder indexHolder = (IndexHolder) holder;


       /* preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {
            indexHolder.title.setText(bookArrayList.get(position).getTitle());
            indexHolder.id.setText(String.valueOf(position+1));

        } else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {
            indexHolder.title.setText(bookArrayList.get(position).getTitle());
            indexHolder.id.setText(String.valueOf(position+1));

        }*/
        indexHolder.title.setText(bookArrayList.get(position).getTitle());
        indexHolder.id.setText(String.valueOf(position+1));
        if (search != null && !search.isEmpty()) {
            int startPos, endPos;
            startPos = bookArrayList.get(position).getTitle().toLowerCase(Locale.US).indexOf(search.toLowerCase(Locale.US));
            endPos = startPos + search.length();

            if (startPos != -1) {
                Spannable spannable = new SpannableString(bookArrayList.get(position).getTitle());
                //spannable.setSpan(new BackgroundColorSpan(context.getResources().getColor(R.color.Khaki)), startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new BackgroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.off_white)), startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                indexHolder.title.setText(spannable);
            } else {
                indexHolder.title.setText(bookArrayList.get(position).getTitle());
            }

        } else {
            indexHolder.title.setText(bookArrayList.get(position).getTitle());
        }
        //Constants.filterTitle = "" + indexHolder.title.getText();
        indexHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Constants.subList = true;
                    Intent intent = new Intent(context, ReadingActivity.class);
                    intent.putExtra("position", "" + (position));
                    intent.putExtra("catId", ""+bookArrayList.get(position).getCatId());
                    context.startActivity(intent);


                } catch (Exception e) {

                }
            }
        });
    }

    //set filter data in arrayList
    public void setFilter(ArrayList<Book> models, String search) {
        bookArrayList = new ArrayList<>();
        bookArrayList.addAll(models);
        this.search = search.toString().toLowerCase();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    /*
    * IndexHolder class for views
    */
    public class IndexHolder extends RecyclerView.ViewHolder {
        TextView title, id;
        RelativeLayout relativeLayout;

        public IndexHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            id = (TextView) itemView.findViewById(R.id.id);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.table_lay);
        }
    }
}