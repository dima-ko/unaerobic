package com.kovalenych.tables;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fragments.TablesFragment;
import com.kovalenych.R;
import com.kovalenych.Table;

import java.util.ArrayList;

public class TablesArrayAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private ArrayList<String> cycles;


    public TablesArrayAdapter(Activity context, ArrayList<String> cycles) {
        super(context, R.layout.table_item, cycles);
        this.context = context;
        this.cycles = cycles;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.table_item, null, true);
        }
        ((TextView) rowView.findViewById(R.id.table_name)).setText(cycles.get(position));
        if (position == TablesFragment.posOfCurTable) {
            (rowView.findViewById(R.id.table_pic)).setVisibility(View.VISIBLE);
        } else
            (rowView.findViewById(R.id.table_pic)).setVisibility(View.GONE);
        return rowView;
    }
}