package com.kovalenych.tables;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.kovalenych.R;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class ClockView extends SurfaceView {

    public static final float pi = (float) Math.PI;
    float angle;
    Bitmap bg = null;
    private int dim;


    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void invalidateClock(boolean isDark) {

        if (bg == null) {
            int id = isDark ? R.drawable.progress_dark_blue : R.drawable.progress_blue;
            Bitmap src = BitmapFactory.decodeResource(getResources(), id);
            bg = Bitmap.createScaledBitmap(src, dim, dim, true);
        }

        SurfaceHolder surfaceHolder = getHolder();

        if (surfaceHolder.getSurface().isValid()) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            Canvas canvas = surfaceHolder.lockCanvas();
            //... actual drawing on canvas
//            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bg, 0, 0, paint);
            final int verticesColors[] = {
                    Color.BLACK, Color.BLACK, Color.BLACK,
                    0xFF000000, 0xFF000000, 0xFF000000
            };

            drawsegmentI(paint, canvas, verticesColors);
            drawsegmentII(paint, canvas, verticesColors);
            drawsegmentIII(paint, canvas, verticesColors);
            drawsegmentIV(paint, canvas, verticesColors);
            drawsegmentV(paint, canvas, verticesColors);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawsegmentI(Paint paint, Canvas canvas, int[] verticesColors) {

        float x3, y3 = 0;
        if (angle > pi / 4) {
            x3 = dim;
        } else {
            x3 = dim / 2 + dim / 2 * tg(angle);
        }
        float verts[] = {
                dim, 0,
                dim / 2, dim / 2,
                x3, y3
        };
        canvas.drawVertices(Canvas.VertexMode.TRIANGLES,
                verts.length, verts, 0, null, 0, verticesColors, 0,
                null, 0, 0, paint);
    }

    private void drawsegmentII(Paint paint, Canvas canvas, int[] verticesColors) {

        float x3 = dim, y3;
        if (angle < pi / 4) {
            y3 = 0;
        } else if (angle > 3 * pi / 4) {
            y3 = dim;
        } else {
            y3 = dim / 2 - dim / 2 * tg(pi / 2 - angle);
        }
        float verts[] = {
                dim, dim,
                dim / 2, dim / 2,
                x3, y3
        };
        canvas.drawVertices(Canvas.VertexMode.TRIANGLES,
                verts.length, verts, 0, null, 0, verticesColors, 0,
                null, 0, 0, paint);
    }

    private void drawsegmentIII(Paint paint, Canvas canvas, int[] verticesColors) {

        float x3, y3 = dim;
        if (angle < 3 * pi / 4) {
            x3 = dim;
        } else if (angle > 5 * pi / 4) {
            x3 = 0;
        } else {
            x3 = dim / 2 + dim / 2 * tg(pi - angle);
        }
        float verts[] = {
                0, dim,
                dim / 2, dim / 2,
                x3, y3
        };
        canvas.drawVertices(Canvas.VertexMode.TRIANGLES,
                verts.length, verts, 0, null, 0, verticesColors, 0,
                null, 0, 0, paint);
    }

    private void drawsegmentIV(Paint paint, Canvas canvas, int[] verticesColors) {

        float x3 = 0, y3;
        if (angle < 5 * pi / 4) {
            y3 = dim;
        } else if (angle > 7 * pi / 4) {
            y3 = 0;
        } else {
            y3 = dim / 2 + dim / 2 * tg(3 * pi / 2 - angle);
        }
        float verts[] = {
                0, 0,
                dim / 2, dim / 2,
                x3, y3
        };
        canvas.drawVertices(Canvas.VertexMode.TRIANGLES,
                verts.length, verts, 0, null, 0, verticesColors, 0,
                null, 0, 0, paint);
    }

    private void drawsegmentV(Paint paint, Canvas canvas, int[] verticesColors) {

        Log.d("zzzzzz", "angle" + angle);

        float x3, y3 = 0;
        if (angle < 7 * pi / 4) {
            x3 = 0;
        } else {
            x3 = dim / 2 - dim / 2 * tg(2 * pi - angle);
        }
        float verts[] = {
                dim / 2, 0,
                dim / 2, dim / 2,
                x3, y3
        };
        canvas.drawVertices(Canvas.VertexMode.TRIANGLES,
                verts.length, verts, 0, null, 0, verticesColors, 0,
                null, 0, 0, paint);
    }

    static float tg(float angle) {
        if (angle == pi / 2 || angle == 3 * pi / 2)
            return 2;
        else return (float) Math.tan(angle);

    }

    public void setDimensions(int w) {
        dim = w;
    }
}
