package com.kovalenych.stats;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.kovalenych.R;


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

    //Далее следуют обязательные к перегрузке методы адаптера

    public StatsAdapter(Context context, StatsDAO dao) {
        super();
        this.context = context;
        this.dao = dao;
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

        holder.startView.setText(" start:" + getItem(position).start);
        holder.lengthView.setText("length:" + (getItem(position).start - getItem(position).end));
        holder.commentView.setText("comment:" + (getItem(position).comment));

        return rowView;
    }

    @Override
    public int getCount() {
        return dao.getSessionsCount();
    }

    @Override
    public Session getItem(int i) {
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