package com.kovalenych.tables;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class RotImageView extends ImageView {

    int angle = 0;

    public RotImageView(Context context) {
        super(context);
    }

    public RotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(angle);
        super.onDraw(canvas);
    }
}
