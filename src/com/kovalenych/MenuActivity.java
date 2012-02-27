package com.kovalenych;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.AbsoluteLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import java.util.Locale;
import java.util.Random;

public class MenuActivity extends Activity {

//    GoogleAnalyticsTracker tracker;





    /**
     * Called when the activity is first created.
     */

    RadiusSurfaceView radiusSurfaceView;
    Bitmap infobitmap;


    private int mWidth = 320;
    int centerX = mWidth - 40;
    private int mHeight = 480;
    int centerY = mHeight - 15;
    int rad = 200;
    int clocks = 12;
    float shiftAngle = 1.f / 7.5f;
    float currAngle = 0.f;
    float lastAngle = 0.f;
    float downAngle = 0.f;
    private Bitmap heartbitmap;
    private Bitmap tablebitmap;
    private Bitmap videobitmap;
    private Bitmap bookbitmap;
    private Bitmap rankebitmap;
    private TextView[] labels;
    private RotTask task;
    int nextPosInt;
    boolean allVisible = true;

    String[] texts = new String[]{"info", "heart", "tables", "videos", "ranking", "articles"};
    private long downTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("language", Locale.getDefault().getLanguage());
        if (Locale.getDefault().getLanguage().equals("fr")) {

        }
        resolvePlatform();

        AbsoluteLayout abs = new AbsoluteLayout(this);

        radiusSurfaceView = new RadiusSurfaceView(this);

        infobitmap = BitmapFactory.decodeResource(getResources(), R.drawable.infoblack);
        heartbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart_beat);
        tablebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.taable);
        videobitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vidos);
        bookbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.book2);
        rankebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_ranking2);

        abs.addView(radiusSurfaceView, new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT, AbsoluteLayout.LayoutParams.FILL_PARENT, 0, 0));
        setContentView(abs);

        labels = new TextView[6];

        for (int i = 0; i < 6; i++) {
            labels[i] = new TextView(this);
            labels[i].setTextSize(16);
            labels[i].setGravity(Gravity.CENTER);
        }

//        LinearLayout child = new LinearLayout(this);
//        child.setBackgroundColor(Color.WHITE);
//        abs.addView(child, new AbsoluteLayout.LayoutParams(80, 80, 80, 380));

        abs.addView(labels[0], new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 158, 371));
        abs.addView(labels[1], new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 250, 320));
        abs.addView(labels[2], new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 15, 190));

        abs.addView(labels[3], new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 105, 145));
        abs.addView(labels[4], new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 165, 65));
        abs.addView(labels[5], new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100, 460));


        float a = (float) ((currAngle * 6) / Math.PI);
        nextPosInt = (a > 0) ? (int) (a + 0.5) : (int) (a - 0.5);

        setTexts();

    }

    private void setTexts() {
        for (int i = 0; i < 6; i++) {
            int z = (i - nextPosInt) % 6;
            try {
                labels[i].setText(texts[z]);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e("Arraa", "" + i + "zzz" + z);
            }
        }

    }

    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        radiusSurfaceView.onResumeMySurfaceView();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        radiusSurfaceView.onPauseMySurfaceView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float angle = ((float) -Math.atan((event.getX() - centerX) / (event.getY() - centerY)) + (float) Math.PI * ((centerY > event.getY() ? 1 : 0))) / 1.2f;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (task != null && task.getStatus() == AsyncTask.Status.RUNNING)
                task.cancel(true);
            lastAngle = angle;
            downAngle = angle;
            downTime = System.currentTimeMillis();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            currAngle += (angle - lastAngle) * 0.8;
            lastAngle = angle;
            if (Math.abs(lastAngle - downAngle) > 1.f / 12)  //treshold
                for (TextView labal : labels)
                    labal.setVisibility(View.INVISIBLE);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (System.currentTimeMillis() - downTime < 400 && Math.abs(lastAngle - downAngle) < 1.f / 10)
                startActivities(event.getX(), event.getY());
            startAnimation();
        }


        return super.onTouchEvent(event);

    }

    private void startActivities(float x, float y) {
        int zone = -1;

        if (isClickInZone(x, y, 158, 371))
            zone = 0;
        else if (isClickInZone(x, y, 250, 320))
            zone = 1;
        else if (isClickInZone(x, y, 15, 190))
            zone = 2;
        else if (isClickInZone(x, y, 105, 145))
            zone = 3;
        else if (isClickInZone(x, y, 165, 65))
            zone = 4;
        else if (isClickInZone(x, y, 100, 460))
            zone = 5;

        int act = (zone - nextPosInt +666) % 6;        // ]:<

        Intent intent = null;

//        {"info", "heart", "tables", "videos", "ranking", "articles"};
        switch (zone) {
            case 0:
                intent = new Intent(MenuActivity.this, InfoActivity.class);
                break;
            case 1:
                Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.under_construction), Toast.LENGTH_SHORT).show();

                break;
            case 2:
                intent = new Intent(MenuActivity.this, TablesActivity.class);

                break;
            case 3:
                intent = new Intent(MenuActivity.this, NostraVideoActivity.class);

                break;
            case 4:
                intent = new Intent(MenuActivity.this, RankingActivity.class);
                break;
            case 5:
                intent = new Intent(MenuActivity.this, ArticlesActivity.class);
                break;

        }
        if (intent != null)
            startActivity(intent); //TODO: troubles


    }

    private boolean isClickInZone(float x, float y, int vx, int vy) {
        return vx - 10 < x && x < vx + 80 && vy - 80 < y && y < vy + 20;
    }

    private void startAnimation() {
        float a = (float) ((currAngle * 6) / Math.PI);


        nextPosInt = (a > 0) ? (int) (a + 0.5) : (int) (a - 0.5);

        float end = (nextPosInt * (float) Math.PI / 6);
        task = new RotTask();
        task.execute(end);

        Log.d("temp", a + "    " + nextPosInt + "");

    }

    private boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to disable internet while roaming, just return false
            return true;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        tracker.dispatch();
//        tracker.stopSession();
    }

    private void resolvePlatform() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (metrics.heightPixels == 480)
            PlatformResolver.isHVGA = true;

        if (metrics.heightPixels == 400)
            PlatformResolver.isHVGA400 = true;

        if (Build.VERSION.SDK_INT < 5)
            PlatformResolver.isDONUT = true;
    }

    class RotTask extends AsyncTask<Float, Integer, Integer> {


        @Override
        protected Integer doInBackground(Float... floats) {
            while (Math.abs(currAngle - floats[0]) > 0.01) {
                if (isCancelled())
                    break;
                currAngle += Math.signum(floats[0] - currAngle) * 0.008;
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            for (TextView labal : labels)
                labal.setVisibility(View.VISIBLE);
            setTexts();
        }
    }


    class RadiusSurfaceView extends SurfaceView implements Runnable {

        Thread thread = null;
        SurfaceHolder surfaceHolder;
        volatile boolean running = false;

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random random;

        public RadiusSurfaceView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            surfaceHolder = getHolder();
            random = new Random();
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            this.setBackgroundResource(R.drawable.bg24);
        }

        public void onResumeMySurfaceView() {
            running = true;
            thread = new Thread(this);
            thread.start();
        }

        public void onPauseMySurfaceView() {
            boolean retry = true;
            running = false;
            while (retry) {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (running) {
                if (surfaceHolder.getSurface().isValid()) {
                    Canvas canvas = surfaceHolder.lockCanvas();
                    //... actual drawing on canvas
                    canvas.drawColor(Color.TRANSPARENT);
//                    canvas.drawPoint(x, y, paint);
                    canvas.drawCircle(-35, -55, rad + 100, paint);
                    canvas.drawCircle(centerX, centerY, rad + 100, paint);
                    for (int i = 0; i < clocks; i++) {
                        float x = (float) (centerX + rad * Math.cos(((float) i / clocks + shiftAngle) * 2 * Math.PI + currAngle));
                        float y = (float) (centerY + rad * Math.sin(((float) i / clocks + shiftAngle) * 2 * Math.PI + currAngle));
                        if (x < mWidth && y < mHeight) {
                            drawIcons(canvas, i, x, y);
                        }
                    }

                    for (int i = 0; i < clocks; i++) {
                        float x = (float) (-35 + rad * Math.cos(((float) i / clocks + shiftAngle) * 2 * Math.PI - currAngle - Math.PI / 3));
                        float y = (float) (-55 + rad * Math.sin(((float) i / clocks + shiftAngle) * 2 * Math.PI - currAngle - Math.PI / 3));
                        if (x > -100 && y > -100) {
                            drawIcons2(canvas, i, x, y);
                        }
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        private void drawIcons(Canvas canvas, int i, float x, float y) {
            switch (i) {
                case 0:
                case 6:
                    canvas.drawBitmap(infobitmap, x, y - 5, paint);
                    break;
                case 1:
                case 7:
                    canvas.drawBitmap(heartbitmap, x + 5, y + 5, paint);
                    break;
                case 2:
                case 8:
                    canvas.drawBitmap(tablebitmap, x, y, paint);
                    break;
                case 3:
                case 9:
                    canvas.drawBitmap(videobitmap, x, y, paint);
                    break;
                case 4:
                case 10:
                    canvas.drawBitmap(rankebitmap, x, y, paint);
                    break;
                case 5:
                case 11:
                    canvas.drawBitmap(bookbitmap, x - 2, y - 5, paint);
                    break;

            }
        }

        private void drawIcons2(Canvas canvas, int i, float x, float y) {
            switch (i) {
                case 5:
                case 11:
                    canvas.drawBitmap(infobitmap, x, y - 5, paint);
                    break;
                case 4:
                case 10:
                    canvas.drawBitmap(heartbitmap, x + 5, y + 5, paint);
                    break;
                case 3:
                case 9:
                    canvas.drawBitmap(tablebitmap, x, y, paint);
                    break;
                case 2:
                case 8:
                    canvas.drawBitmap(videobitmap, x, y, paint);
                    break;
                case 1:
                case 7:
                    canvas.drawBitmap(rankebitmap, x, y, paint);
                    break;
                case 0:
                case 6:
                    canvas.drawBitmap(bookbitmap, x, y - 5, paint);
                    break;

            }
        }

    }
}
