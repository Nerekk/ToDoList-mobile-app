package com.example.todolist_mobile_app;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.todolist_mobile_app.AddingActivity.AddTaskActivity;
import com.example.todolist_mobile_app.Database.DatabaseManager;
import com.example.todolist_mobile_app.Database.TaskDatabase;
import com.example.todolist_mobile_app.Enums.Categories;
import com.example.todolist_mobile_app.Recycler.RecyclerViewManager;

public class MainActivity extends AppCompatActivity {
    public TaskDatabase db;
    public RecyclerViewManager rvManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = DatabaseManager.initDatabase(this);
        rvManager = new RecyclerViewManager(this);

        findViewById(R.id.fab).setOnClickListener(this::goToAddTaskActivity);

        new ToolbarManager(this);
        Log.i("ONCREATE", "ONCREATE");
    }

    public void goToAddTaskActivity(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rvManager.getDataFromDB();
        Log.i("ONRESUME", "ONRESUME");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu_main, menu);

        MenuItem item = menu.findItem(R.id.filterMenu);
        Spinner spinner = (Spinner) item.getActionView();

        MenuItem item2 = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) item2.getActionView();


        prepSpinnerFilter(spinner);


        prepSearchViewFilter(searchView, spinner);
        return super.onCreateOptionsMenu(menu);
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
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_item_filter, c);
        spinner.setAdapter(adapter);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getResources().getColor(R.color.color_additional));
        gd.setCornerRadius(20);
        spinner.setBackground(gd);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedCategory = (String) adapterView.getItemAtPosition(position);
                rvManager.filterData(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}