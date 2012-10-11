package com.kovalenych.ranking;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kovalenych.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RankingArrayAdapter extends ArrayAdapter<Record> {
    private final Activity context;
    private ArrayList<Record> records;
    HashMap<String, Integer> map;

    public RankingArrayAdapter(Activity context, ArrayList<Record> records) {
        super(context, R.layout.record_item, records);
        this.context = context;
        this.records = records;
        fillMap();
    }

    private void fillMap() {
        map = new HashMap<String, Integer>();
        map.put("at", R.drawable.at);
        map.put("br", R.drawable.br);
        map.put("ca", R.drawable.ca);
        map.put("ch", R.drawable.ch);
        map.put("cz", R.drawable.cz);
        map.put("de", R.drawable.de);
        map.put("dk", R.drawable.dk);
        map.put("es", R.drawable.es);
        map.put("fi", R.drawable.fi);
        map.put("fr", R.drawable.fr);
        map.put("gb", R.drawable.gb);
        map.put("gr", R.drawable.gr);
        map.put("hr", R.drawable.hr);
        map.put("it", R.drawable.it);
        map.put("jp", R.drawable.jp);
        map.put("nl", R.drawable.nl);
        map.put("no", R.drawable.no);
        map.put("nz", R.drawable.nz);
        map.put("pl", R.drawable.pl);
        map.put("rs", R.drawable.rs);
        map.put("ru", R.drawable.ru);
        map.put("se", R.drawable.se);
        map.put("ua", R.drawable.ua);
        map.put("us", R.drawable.us);
        map.put("ve", R.drawable.ve);



        //todo good adapter
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

        Integer resId = map.get(records.get(position).getCountry());
        if (resId == null) {
            ((TextView) rowView.findViewById(R.id.ranking_country)).setText(records.get(position).getCountry());
            (rowView.findViewById(R.id.ranking_country)).setBackgroundColor(0xff222222);
        } else {
            (rowView.findViewById(R.id.ranking_country)).setBackgroundResource(resId);
            ((TextView) rowView.findViewById(R.id.ranking_country)).setText("");
        }
        return rowView;
    }

}