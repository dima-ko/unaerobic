package com.kovalenych.roboto;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import com.kovalenych.R;
import com.kovalenych.Utils;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class EditTextRoboto extends EditText {
    public EditTextRoboto(Context context) {
        super(context);
        setTypeface(Utils.roboto_light);
        setBackgroundResource(R.drawable.edit_bl);
        setTextColor(0xffffffff);
        setHintTextColor(0xff333333);
    }

    public EditTextRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Utils.roboto_light);
        setBackgroundResource(R.drawable.edit_bl);
        setTextColor(0xffffffff);
        setHintTextColor(0xff333333);
    }

    public EditTextRoboto(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(Utils.roboto_light);
        setBackgroundResource(R.drawable.edit_bl);
        setTextColor(0xffffffff);
        setHintTextColor(0xff333333);
    }
}
