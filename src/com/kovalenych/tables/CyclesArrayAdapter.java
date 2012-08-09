package com.kovalenych.tables;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kovalenych.R;

public class CyclesArrayAdapter extends ArrayAdapter<Cycle> {
    private final Activity context;
    private Cycle[] cycles;


    public CyclesArrayAdapter(Activity context, Cycle[] cycles) {
        super(context, R.layout.cycle_item, cycles);
        this.context = context;
        this.cycles = cycles;
    }

    // Класс для сохранения во внешний класс и для ограничения доступа
    // из потомков класса
    static class ViewHolder {
        public TextView dataView;
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
                rowView = inflater.inflate(R.layout.cycle_item, null, true);
            holder = new ViewHolder();
            holder.dataView = (TextView) rowView.findViewById(R.id.cycle_item);
            rowView.setTag(holder);
            holder.dataView.setText(cycles[position].convertToString());
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        return rowView;
    }
}