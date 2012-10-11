package com.kovalenych.ranking;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kovalenych.R;

import java.util.ArrayList;

public class RankingArrayAdapter extends ArrayAdapter<Record> {
    private final Activity context;
    private ArrayList<Record> records;


    public RankingArrayAdapter(Activity context, ArrayList<Record> records) {
        super(context, R.layout.record_item, records);
        this.context = context;
        this.records = records;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.record_item, null, true);

        }

        ((TextView) rowView.findViewById(R.id.ranking_place)).setText((position + 1) + "");
        ((TextView) rowView.findViewById(R.id.ranking_name_surname)).setText(records.get(position).getName());
        ((TextView) rowView.findViewById(R.id.ranking_result)).setText(records.get(position).getResult());
        if (records.get(position).getCountry().equals("at"))
            (rowView.findViewById(R.id.ranking_country)).setBackgroundResource(R.drawable.at);
        else {
            ((TextView) rowView.findViewById(R.id.ranking_country)).setText(records.get(position).getCountry());
            (rowView.findViewById(R.id.ranking_country)).setBackgroundColor(0xff222222);
        }

        return rowView;
    }

}