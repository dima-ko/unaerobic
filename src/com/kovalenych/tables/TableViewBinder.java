package com.kovalenych.tables;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.kovalenych.Utils;

public class TableViewBinder implements SimpleAdapter.ViewBinder {


    @Override
    public boolean setViewValue(View view, Object data,String textRepresentation) {

        if ((view instanceof TextView) & (data instanceof String)) {

            TextView iv = (TextView) view;
            iv.setGravity(Gravity.CENTER);
            iv.setTypeface(Utils.roboto_light);
            iv.setText((String) data);

            if (data.equals("CO2 Table"))
                iv.setTextColor(0xFFBB88FF);
            else if (data.equals("O2 Table"))
                iv.setTextColor(0xFF8888FF);

            return true;
        }
        return false;

    }

}
