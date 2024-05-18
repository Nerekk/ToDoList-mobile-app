package com.example.todolist_mobile_app;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.todolist_mobile_app.Enums.Categories;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;

public class ToolbarManager {
    Spinner menuSpinner;
    SearchView menuSearch;
    MainActivity activity;
    RecyclerViewManager rvManager;
    CustomSpinnerAdapter adapter;

    public ToolbarManager(MainActivity activity, RecyclerViewManager recyclerViewManager) {
        this.activity = activity;
        this.rvManager = recyclerViewManager;

        Toolbar myToolbar = activity.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mapComponents();
        prepSpinnerFilter(menuSpinner);
        prepSearchViewFilter(menuSearch, menuSpinner);
    }

    private void mapComponents() {
        menuSpinner = activity.findViewById(R.id.filterMenu);
        menuSearch = activity.findViewById(R.id.action_view);
        menuSearch.setQueryHint("Search task..");

    }

    private void prepSearchViewFilter(SearchView searchView, Spinner spinner) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvManager.setLastQuery(newText);
                rvManager.filterData(spinner.getSelectedItem().toString());
                return false;
            }
        });
    }

    private void prepSpinnerFilter(Spinner spinner) {
        String[] c = Categories.fillCategories(true);
        adapter = new CustomSpinnerAdapter(activity, R.layout.spinner_item_filter, c);
        spinner.setAdapter(adapter);

//        GradientDrawable gd = new GradientDrawable();
//        gd.setColor(activity.getResources().getColor(R.color.color_additional));
//        gd.setCornerRadius(20);
//        spinner.setBackground(gd);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCategory = (String) adapterView.getItemAtPosition(position);
                adapter.setSelectedItemPosition(position);
                rvManager.setLastCategory(selectedCategory);
                rvManager.filterData(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
