package com.example.todolist_mobile_app;

import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.example.todolist_mobile_app.Enums.Categories;
import com.example.todolist_mobile_app.Enums.OrderType;
import com.example.todolist_mobile_app.Enums.TaskStatus;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;

public class ToolbarManager {
    Spinner spinnerCategory, spinnerTaskStatus, spinnerOrder;
    SearchView menuSearch;
    MainActivity activity;
    RecyclerViewManager rvManager;
    CustomSpinnerAdapter adapterCategory, adapterTaskStatus, adapterOrder;

    public ToolbarManager(MainActivity activity, RecyclerViewManager recyclerViewManager) {
        this.activity = activity;
        this.rvManager = recyclerViewManager;

        Toolbar myToolbar = activity.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(myToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        mapComponents();
        prepSpinnerCategoryFilter(spinnerCategory);
        prepSpinnerTaskStatusFilter(spinnerTaskStatus);
        prepSpinnerOrderFilter(spinnerOrder);
        prepSearchViewFilter(menuSearch, spinnerCategory);
    }

    private void mapComponents() {
        spinnerCategory = activity.findViewById(R.id.filterMenu);
        spinnerTaskStatus = activity.findViewById(R.id.filterTaskStatus);
        spinnerOrder = activity.findViewById(R.id.filterOrder);

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
//                rvManager.setLastQuery(newText);
                rvManager.filterDataOnQueryChange(newText);
                return false;
            }
        });
    }

    private void prepSpinnerCategoryFilter(Spinner spinner) {
        String[] c = Categories.fillCategories(true);
        adapterCategory = new CustomSpinnerAdapter(activity, R.layout.spinner_item_filter, c);
        spinner.setAdapter(adapterCategory);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCategory = (String) adapterView.getItemAtPosition(position);
                adapterCategory.setSelectedItemPosition(position);
//                rvManager.setLastCategory(selectedCategory);
                rvManager.filterDataOnCategoryChange(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void prepSpinnerTaskStatusFilter(Spinner spinner) {
        String[] t = TaskStatus.fillTaskStatus();
        adapterTaskStatus = new CustomSpinnerAdapter(activity, R.layout.spinner_item_filter, t);
        spinner.setAdapter(adapterTaskStatus);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedStatus = (String) adapterView.getItemAtPosition(position);
                adapterTaskStatus.setSelectedItemPosition(position);
//                rvManager.setLastCategory(selectedCategory);
                rvManager.filterDataOnTaskStatusChange(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void prepSpinnerOrderFilter(Spinner spinner) {
        String[] o = OrderType.fillOrderType();
        adapterOrder = new CustomSpinnerAdapter(activity, R.layout.spinner_item_filter, o);
        spinner.setAdapter(adapterOrder);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedOrder = (String) adapterView.getItemAtPosition(position);
                adapterOrder.setSelectedItemPosition(position);
//                rvManager.setLastCategory(selectedCategory);
                rvManager.filterDataOnOrderChange(selectedOrder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
