package com.kovalenych;


import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ArticleViewBinder implements SimpleAdapter.ViewBinder {


//    private Typeface tf;

    public ArticleViewBinder(/*Typeface tf*/) {
//        this.tf = tf;
    }

    @Override
    public boolean setViewValue(View view, Object data,String textRepresentation) {

        if ((view instanceof TextView) & (data instanceof String)) {

            TextView iv = (TextView) view;
//            iv.setTypeface(tf);
            iv.setText((String) data);

            if (view.getId()==R.id.art_domain)
                iv.setTextColor(0xFF8888FF);

            return true;
        }
        return false;

    }

}
