package com.kovalenych.tables;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kovalenych.R;

import java.util.ArrayList;

public class CyclesArrayAdapter extends ArrayAdapter<MultiCycle> {
    private final Activity context;
    private ArrayList<MultiCycle> multiCycles;


    public CyclesArrayAdapter(Activity context, ArrayList<MultiCycle> multiCycles1) {
        super(context, R.layout.cycle_item, multiCycles1);
        this.context = context;
        this.multiCycles = multiCycles1;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.cycle_item, null, true);
        }

        ((TextView) rowView.findViewById(R.id.cycle_item)).setText(multiCycles.get(position).
                cycles.get(0).convertToString());

        int sameCycles = multiCycles.get(position).
                cycles.size();
        if (sameCycles == 1)
            ((TextView) rowView.findViewById(R.id.cycle_repeat)).setText("");
        else
            ((TextView) rowView.findViewById(R.id.cycle_repeat)).setText("x" + sameCycles);

        //progress
        if (position == CyclesActivity.curMultiCycle) {
            (rowView.findViewById(R.id.cycle_pic)).setVisibility(View.VISIBLE);
        } else
            (rowView.findViewById(R.id.cycle_pic)).setVisibility(View.GONE);
        return rowView;
    }
}