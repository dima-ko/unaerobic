package com.kovalenych.tables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class RotImageView extends ImageView {

    int angle = 90;
    private Bitmap bm;

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

        canvas.save();

        Matrix rotator = new Matrix();
        rotator.postRotate(angle);
        if (bm != null)
            canvas.drawBitmap(bm, rotator, null);

        super.onDraw(canvas);
        canvas.restore();

//        canvas.save();
//        canvas.rotate(45,<appropriate x pivot value>,<appropriate y pivot value>);
//        super.onDraw(canvas);
//        canvas.restore();

    }

    public void setBitmap(Bitmap bm) {

        this.bm = bm;
    }
}
