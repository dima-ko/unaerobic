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
        map.put("au", R.drawable.au);
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
        map.put("jm", R.drawable.jm);
        map.put("nl", R.drawable.nl);
        map.put("no", R.drawable.no);
        map.put("nz", R.drawable.nz);
        map.put("pl", R.drawable.pl);
        map.put("rs", R.drawable.rs);
        map.put("ru", R.drawable.ru);
        map.put("se", R.drawable.se);
        map.put("si", R.drawable.si);
        map.put("sk", R.drawable.sk);
        map.put("ua", R.drawable.ua);
        map.put("us", R.drawable.us);
        map.put("ve", R.drawable.ve);

                      //todo zvuk slide

        //todo good adapter
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
//
//        if (convertView == null) {
//            LayoutInflater inflater = context.getLayoutInflater();
//            convertView = inflater.inflate(R.layout.task_item, null, true);
//            viewHolder = new TViewHolder();
//            viewHolder.textView = (TextView) convertView.findViewById(R.id.task_text);
//            viewHolder.dateView = (TextView) convertView.findViewById(R.id.date_text);
//            viewHolder.position= position;
//            convertView.setTag(viewHolder);
//
//        } else {
//            viewHolder = (TViewHolder) convertView.getTag();
//        }
//
//        viewHolder.textView .setText(tasks.get(tasks.size()-1 - position).text);
//        viewHolder.dateView .setText(tasks.get(tasks.size()-1 - position).dateString);
//        viewHolder.position= position;
//
//        return convertView;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.record_item, null, true);
            viewHolder = new ViewHolder();
            viewHolder.placeView = (TextView) convertView.findViewById(R.id.ranking_place);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.ranking_name_surname);
            viewHolder.resultView = (TextView) convertView.findViewById(R.id.ranking_result);
            viewHolder.flagView = (TextView) convertView.findViewById(R.id.ranking_country);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.placeView.setText((position + 1) + "");
        viewHolder.nameView.setText(records.get(position).getName());
        viewHolder.resultView.setText(records.get(position).getResult());

        Integer resId = map.get(records.get(position).getCountry());
        if (resId == null) {
            viewHolder.flagView.setText(records.get(position).getCountry());
            viewHolder.flagView.setBackgroundColor(0xff222222);
        } else {
            viewHolder.flagView.setBackgroundResource(resId);
            viewHolder.flagView.setText("");
        }
        return convertView;
    }

    static class ViewHolder {
        TextView placeView;
        TextView nameView;
        TextView resultView;
        TextView flagView;
    }

}