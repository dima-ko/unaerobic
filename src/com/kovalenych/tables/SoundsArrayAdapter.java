package com.kovalenych.tables;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kovalenych.R;

import java.util.ArrayList;

public class SoundsArrayAdapter extends ArrayAdapter<Sound> {
    private final Activity context;
    private ArrayList<Sound> sounds;


    public SoundsArrayAdapter(Activity context, ArrayList<Sound> multiCycles1) {
        super(context, R.layout.sound_item, multiCycles1);
        this.context = context;
        this.sounds = multiCycles1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.sound_item, null, true);
        }

        ((TextView) rowView.findViewById(R.id.sound_name)).setText(sounds.get(position).name);
        ((TextView) rowView.findViewById(R.id.sound_path)).setText(sounds.get(position).path);

        return rowView;
    }
}