package com.example.todolist_mobile_app;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.example.todolist_mobile_app.Enums.Categories;

public class ToolbarManager {
//    Spinner filterSpinner;
    MainActivity activity;
    private String[] categories;
    private CustomSpinnerAdapter catAdapter;

    public ToolbarManager(MainActivity activity) {
        this.activity = activity;

        Toolbar myToolbar = activity.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mapComponents();
        setSpinner();
        setListeners();
    }


    private void mapComponents() {
//        filterSpinner = activity.findViewById(R.id.filterSpinner);
    }

    private void setSpinner() {
        categories = Categories.fillCategories(true);
        catAdapter = new CustomSpinnerAdapter(activity, R.layout.spinner_item_filter, categories);
//        filterSpinner.setAdapter(catAdapter);
    }

    private void setListeners() {

    }

}
