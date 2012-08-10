package com.kovalenych.tables;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kovalenych.R;

import java.util.ArrayList;

public class CyclesArrayAdapter extends ArrayAdapter<Cycle> {
    private final Activity context;
    private ArrayList<Cycle> cycles;


    public CyclesArrayAdapter(Activity context, ArrayList<Cycle> cycles) {
        super(context, R.layout.cycle_item, cycles);
        this.context = context;
        this.cycles = cycles;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
                rowView = inflater.inflate(R.layout.cycle_item, null, true);
        }
       ((TextView) rowView.findViewById(R.id.cycle_item)).setText(cycles.get(position).convertToString());

        return rowView;
    }
}