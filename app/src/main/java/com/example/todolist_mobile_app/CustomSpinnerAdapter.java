package com.example.todolist_mobile_app;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private List<String> items;
    private int selectedItemPosition = -1;

    public CustomSpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        TextView textView = (TextView) view;
        textView.setTextColor(Color.BLACK); // Ustawienie koloru czarnego dla elementów na liście rozwijanej

        if (position == selectedItemPosition) {
            textView.setTypeface(null, android.graphics.Typeface.BOLD);
            textView.setTextColor(Color.BLUE);
        } else {
            textView.setTypeface(null, android.graphics.Typeface.NORMAL);
            textView.setTextColor(Color.BLACK);
        }

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
//        TextView textView = (TextView) view;
//        textView.setTextColor(Color.WHITE); // Ustawienie koloru białego dla wybranego elementu
        view.setVisibility(View.GONE);
        return view;
    }

    public void setSelectedItemPosition(int position) {
        this.selectedItemPosition = position;
        notifyDataSetChanged();
    }
}
