package com.kovalenych.stats;

import android.app.Activity;
import android.content.Context;

import java.text.DateFormat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.kovalenych.R;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class StatsAdapter extends BaseAdapter {

    // Для удобства выполнения sql-запросов
    // создадим константы с именами полей таблицы
    // и номерами соответсвующих столбцов

    Context context;
    private StatsDAO dao;
    DateFormat dateFormat;
    DateFormat timeFormat;

    //Далее следуют обязательные к перегрузке методы адаптера

    public StatsAdapter(Context context, StatsDAO dao) {
        super();
        this.context = context;
        this.dao = dao;
        dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parrent) {
        ViewHolder holder;
        // Очищает сущетсвующий шаблон, если параметр задан
        // Работает только если базовый шаблон для всех классов один и тот же
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rowView = inflater.inflate(R.layout.session_item, null, true);
            holder = new ViewHolder();
            holder.startView = (TextView) rowView.findViewById(R.id.session_start);
            holder.lengthView = (TextView) rowView.findViewById(R.id.session_length);
            holder.commentView = (TextView) rowView.findViewById(R.id.session_comment);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        Session item = getItem(position);
        Date startDate = new Date(item.start);
        holder.startView.setText(dateFormat.format(startDate) + "\n" + timeFormat.format(startDate));
        holder.lengthView.setText((item.end - item.start) / 1000 + " s");
        holder.commentView.setText((item.comment));

        Log.d("stats getView", "start" + item.start);

        return rowView;
    }

    @Override
    public int getCount() {
        Log.d("stats getCount", "" + dao.getSessionsCount());
        return dao.getSessionsCount();
    }

    @Override
    public Session getItem(int i) {
        Log.d("stats getItem", "start");
        return dao.getItem(i);
    }

    @Override
    public long getItemId(int position) {
        return dao.getItemId(position);
    }

    static class ViewHolder {
        public TextView startView;
        public TextView lengthView;
        public TextView commentView;
    }

    // Класс-помошник отвечающий за создание/отктрытие
    // базы и осуществляющий контроль ее версий
}