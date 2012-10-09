package com.kovalenych.tables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class ClockView extends SurfaceView {

    float angle;

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void invalidateClock() {
        SurfaceHolder surfaceHolder = getHolder();
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            //... actual drawing on canvas
            canvas.drawColor(Color.BLACK);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
