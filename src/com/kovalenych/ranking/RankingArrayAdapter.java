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


    // Класс для сохранения во внешний класс и для ограничения доступа
    // из потомков класса
    static class ViewHolder {
        public TextView placeView;
        public TextView nameView;
        public TextView resultView;
        public TextView countryView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder буферизирует оценку различных полей шаблона элемента

        ViewHolder holder;
        // Очищает сущетсвующий шаблон, если параметр задан
        // Работает только если базовый шаблон для всех классов один и тот же
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.record_item, null, true);
            holder = new ViewHolder();
            holder.placeView = (TextView) rowView.findViewById(R.id.ranking_place);
            holder.nameView = (TextView) rowView.findViewById(R.id.ranking_name_surname);
            holder.resultView = (TextView) rowView.findViewById(R.id.ranking_result);
            holder.countryView = (TextView) rowView.findViewById(R.id.ranking_country);
            rowView.setTag(holder);
            holder.placeView.setText(position+"");
            holder.nameView.setText(records.get(position).getName());
            holder.resultView.setText(records.get(position).getResult());
            holder.countryView.setText(records.get(position).getCountry());
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        return rowView;
    }
}