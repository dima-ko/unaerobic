package com.kovalenych.media;


import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.kovalenych.R;

public class ArticleViewBinder implements SimpleAdapter.ViewBinder {


//    private Typeface tf;

    public ArticleViewBinder(/*Typeface tf*/) {
//        this.tf = tf;
    }

    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {

        if ((view instanceof TextView) & (data instanceof String)) {

            TextView iv = (TextView) view;
//            iv.setTypeface(tf);
            iv.setText((String) data);

            if (view.getId() == R.id.art_domain)
                iv.setTextColor(0xFF88DDFF);
//            if (data.equals("No") || data.equals("from") || data.equals("who") || data.equals("result"))
//                iv.setTextColor(0xFF44AAFF);


                return true;
        }


        return false;

    }

}
