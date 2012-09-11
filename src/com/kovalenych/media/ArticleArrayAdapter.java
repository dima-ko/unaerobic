package com.kovalenych.media;


import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.kovalenych.R;

import java.util.ArrayList;

public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    private final Activity context;
    private ArrayList<Article> articles;


    public ArticleArrayAdapter(FragmentActivity context, ArrayList<Article> articles1) {
        super(context, R.layout.cycle_item, articles1);
        this.context = context;
        this.articles = articles1;
    }

//    Класс для сохранения во внешний класс и для ограничения доступа
//    из потомков класса
    static class ViewHolder {
        public TextView nameView;
        public TextView domainView;          //TODO:KILL!!!!
        public TextView authorView;
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
                rowView = inflater.inflate(R.layout.article_item, null, true);
            holder = new ViewHolder();
            holder.nameView = (TextView) rowView.findViewById(R.id.art_name);
            holder.domainView = (TextView) rowView.findViewById(R.id.art_domain);
            holder.authorView = (TextView) rowView.findViewById(R.id.art_author);
            rowView.setTag(holder);

        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.nameView.setText(articles.get(position).getName());
        holder.domainView.setText(articles.get(position).getDomain());
        holder.authorView.setText(articles.get(position).getAuthor());

        return rowView;
    }
}