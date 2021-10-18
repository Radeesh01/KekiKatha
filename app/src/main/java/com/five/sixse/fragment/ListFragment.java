package com.five.sixse.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.five.sixse.activity.ListActivity;
import com.five.sixse.activity.MainActivity;
import com.five.sixse.activity.ReadingActivity;
import com.five.sixse.model.Book;
import com.five.sixse.helper.MainDBHelper;
import com.five.sixse.R;
import com.five.sixse.adapter.ListAdapter;
import com.five.sixse.helper.Constants;
import com.five.sixse.helper.Utility;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ListFragment extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layout_manager;
    public Fragment subFragment = null;
    public Menu menu;
    public static View view;
    private SharedPreferences preferences;
    public static int catId;


    //Database Objects................
    public MainDBHelper dba = MainActivity.dba;
    // Third Page......................................
    public int currentVisiblePosition = 0;
    public ArrayList<Book> bookArrayList;
    public SearchView searchView;
    public Activity mActivity;
    public ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_list, container, false);
        mActivity = getActivity();
        recyclerView = (RecyclerView) v.findViewById(R.id.list);
        layout_manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layout_manager);

        setHasOptionsMenu(true);
        this.view = v;
        Bundle b = getActivity().getIntent().getExtras();
        catId = Integer.parseInt(b.getString("id"));
        //load banner Ads
        AdView adView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //set Adapter with current selected language
        init();
        return v;

    }


    public void init() {
        try {
            subFragment = ListFragment.this;
            bookArrayList = new ArrayList<>();
            //when we change language of book. we should change action bar title with selected language
            ((ListActivity) getActivity()).setActionBarTitle();
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_ENG)) {
                Constants.radio = R.id.lang_eng;
                 /* if category was all_search then we will get AllChapter from database with list
                 * other wise we will get chapters with category id
                 */
                if (Constants.category.equals("all_search")) {
                    bookArrayList = dba.getAllBookChapterFromTable(Constants.VAR_ENG);
                } else {
                    bookArrayList = dba.getBookChapterWithCategory(catId, Constants.VAR_ENG);
                }
            } else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_HINDI)) {
                Constants.radio = R.id.lang_eng;
                /* if category was all_search then we will get AllChapter from database with list
                 * other wise we will get chapters with category id
                 */
                if (Constants.category.equals("all_search")) {
                    bookArrayList = dba.getAllBookChapterFromTable(Constants.VAR_HINDI);
                } else {
                    bookArrayList = dba.getBookChapterWithCategory(catId, Constants.VAR_HINDI);
                }


            }
            else if (preferences.getString(Constants.PREF_LANGUAGE, Constants.PREF_LANGUAGE_ENG).equals(Constants.PREF_LANGUAGE_FRENCH)) {
                Constants.radio = R.id.lang_eng;
                /* if category was all_search then we will get AllChapter from database with list
                 * other wise we will get chapters with category id
                 */
                if (Constants.category.equals("all_search")) {
                    bookArrayList = dba.getAllBookChapterFromTable(Constants.VAR_FRENCH);
                } else {
                    bookArrayList = dba.getBookChapterWithCategory(catId, Constants.VAR_FRENCH);
                }


            }
            listAdapter = new ListAdapter(getActivity(), bookArrayList);
            recyclerView.setAdapter(listAdapter);

            if (Utility.getBoolean("isindicator", getActivity())) {
                try {
                    Constants.id = Integer.parseInt(Utility.getsubid("Subid", getActivity()));
                    Constants.subList = true;
                    Intent intent = new Intent(getActivity(), ReadingActivity.class);
                    intent.putExtra("position", "" + (Constants.id));
                    intent.putExtra("catId", "" + bookArrayList.get(Constants.id).getCatId());
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
                }
                Utility.setBoolean("isindicator", false, getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onPause() {
        super.onPause();
        currentVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }


    RadioGroup lang_select;


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition((int) currentVisiblePosition);
        currentVisiblePosition = 0;

        //in Constant.filter is not empty  then onResume fragment show filterable list
        //other wise set all lst
        if (!Constants.filter.equals("")) {
            init();
            SearchFilter(Constants.filter, ListAdapter.bookArrayList);
        } else {
            init();
        }


    }

    // public  SearchView searchView;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.search_menu, menu);
        try {
            searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));

            //in Constant.filter is  empty  then search from all list
            if (Constants.filter.equals("")) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String newText) {

                        //we will set search query in Constant.filter . So, we can highlight that word in list and display list
                        Constants.filter = newText;
                        SearchFilter(newText, bookArrayList);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                });
            }
            // if Constant.filter is not empty then adapter set with filter text
            else if (!Constants.filter.equals("")) {

                searchView.setIconified(false);
                searchView.setQuery(Constants.filter, true);
                // SearchFilter(Constants.filter, ListAdapter.bookArrayList);
                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        init();
                        return false;
                    }
                });

            }
            //if we search from all chapter . set search view always open
            //otherwise collapse search view.
            if (Constants.category.equals("all_search")) {
                searchView.setIconified(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //Write your logic here
            //finish();
            case android.R.id.home:

                Intent intent = new Intent(getActivity(), MainActivity.class);
                Constants.filter = "";
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                getActivity().finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void SearchFilter(String str, ArrayList<Book> bookList) {
        try {
            if (bookList != null) {
                final ArrayList<Book> filteredModelList = filter(bookList, str);

                ListAdapter listAdapter = new ListAdapter(mActivity, filteredModelList);
                listAdapter.setFilter(filteredModelList, str);
                recyclerView.setAdapter(listAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> filter(ArrayList<Book> models, String query) {
        query = query.toLowerCase();
        final ArrayList<Book> filteredModelList = new ArrayList<>();
        for (Book model : models) {
            final String text = model.getTitle().toLowerCase();
            final String des = model.getDescription().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            } else if (des.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}

