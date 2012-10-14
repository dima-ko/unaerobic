package com.kovalenych.roboto;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import com.kovalenych.Utils;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class ButtonRoboto extends Button{
    public ButtonRoboto(Context context) {
        super(context);
        setTypeface(Utils.roboto_light);
    }

    public ButtonRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Utils.roboto_light);
    }

    public ButtonRoboto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Utils.roboto_light);
    }
}
