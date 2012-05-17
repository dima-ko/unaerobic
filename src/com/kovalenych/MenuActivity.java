package com.kovalenych;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.kovalenych.ranking.RankingActivity;
import com.kovalenych.tables.TablesActivity;

import java.util.Locale;
import java.util.Random;

public class MenuActivity extends Activity {

//    GoogleAnalyticsTracker tracker;


    /**
     * Called when the activity is first created.
     */

    RadiusSurfaceView radiusSurfaceView;
    Bitmap infobitmap;


    private int mWidth;
    int centerX;
    private int mHeight;
    int centerY;
    int rad;
    int clocks = 12;
    final static float shiftAngle = 1.f / 7.5f;
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
    int[] labelsX;
    int[] labelsY;

    String[] texts;
    private long downTime;
    private boolean isSmallSreen;
    private Dialog infoDialog;
    private SharedPreferences _preferedTables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        texts = new String[]{getString(R.string.info_title),
                getString(R.string.heart),
                getString(R.string.tables),
                getString(R.string.videos),
                getString(R.string.ranking),
                getString(R.string.articles)};

        _preferedTables = getSharedPreferences("initPreferences", MODE_PRIVATE);

        showFrenchFilmDialog();

        resolvePlatform();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        isSmallSreen = (PlatformResolver.isHVGA);
        rad = PlatformResolver.getRadius();
        centerX = PlatformResolver.getCenterX();
        centerY = PlatformResolver.getCenterY();
        labelsX = PlatformResolver.getLabelsX();
        labelsY = PlatformResolver.getLabelsY();

        infoDialog = new Dialog(MenuActivity.this);
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setCancelable(true);
        infoDialog.setContentView(R.layout.info_dialog);


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
            labels[i].setTextSize(PlatformResolver.getTextSize());
            labels[i].setGravity(Gravity.CENTER);
            abs.addView(labels[i], new AbsoluteLayout.LayoutParams(PlatformResolver.getTextWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, labelsX[i], labelsY[i]));
        }

        float a = (float) ((currAngle * 6) / Math.PI);
        nextPosInt = (a > 0) ? (int) (a + 0.5) : (int) (a - 0.5);

        setTexts();
        setVisibilityLabels(false);
        nextPosInt = -1;
        float end = (nextPosInt * (float) Math.PI / 6);
        task = new RotTask();
        task.execute(end);

    }

    private void showFrenchFilmDialog() {
        boolean needShowFrenchFilm = _preferedTables.getBoolean("frenchfilm", true);
        Log.d("language", Locale.getDefault().getLanguage());

        if (Locale.getDefault().getLanguage().equals("fr")) {
            if (needShowFrenchFilm) {
                Dialog frenchDialog = new Dialog(MenuActivity.this);
                TextView textView = new TextView(MenuActivity.this);
                textView.setPadding(30, 20, 20, 20);
                textView.setText(getString(R.string.frenchRequest));
                frenchDialog.setContentView(textView);
                frenchDialog.show();
                frenchDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        SharedPreferences.Editor edito = _preferedTables.edit();
                        edito.putBoolean("frenchfilm", false);
                        edito.commit();
                    }
                });

            }
        }
    }

    private void setTexts() {
        for (int i = 0; i < 6; i++) {
            int z = (i - nextPosInt + 666) % 6;
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
                setVisibilityLabels(false);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (System.currentTimeMillis() - downTime < 400 && Math.abs(lastAngle - downAngle) < 1.f / 10)
                startActivities(event.getX(), event.getY());
            startAnimation();
        }


        return super.onTouchEvent(event);

    }

    private void startActivities(float x, float y) {
        int zone = -1;

        for (int i = 0; i < 6; i++) {
            if (isClickInZone(x, y, labelsX[i], labelsY[i]))
                zone = i;
        }

        int act = (zone - nextPosInt + 666) % 6;        // ]:<

        Intent intent = null;

//        {"info", "heart", "tables", "videos", "ranking", "articles"};
        switch (act) {
            case 0:
//                intent = new Intent(MenuActivity.this, InfoActivity.class);
                infoDialog.show();
//
                break;
            case 1:
                Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.under_construction), Toast.LENGTH_SHORT).show();
                break;
            case 2:
                intent = new Intent(MenuActivity.this, TablesActivity.class);
                break;
            case 3:
                if (haveInternet())
                    intent = new Intent(MenuActivity.this, NostraVideoActivity.class);
                else
                    Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.noConnect), Toast.LENGTH_SHORT).show();
                break;
            case 4:
                if (haveInternet())
                    intent = new Intent(MenuActivity.this, RankingActivity.class);
                else
                    Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.noConnect), Toast.LENGTH_SHORT).show();
                break;
            case 5:
                if (haveInternet())
                    intent = new Intent(MenuActivity.this, ArticlesActivity.class);
                else
                    Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.noConnect), Toast.LENGTH_SHORT).show();
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
        if (metrics.heightPixels == 1280)
            PlatformResolver.is720p = true;

        if (metrics.heightPixels == 1024)
            PlatformResolver.isTabI = true;

        if (metrics.heightPixels == 480)
            PlatformResolver.isHVGA = true;

        if (metrics.heightPixels == 854)
            PlatformResolver.is854 = true;

        if (metrics.heightPixels == 320)
            PlatformResolver.isQVGA = true;


        if (Build.VERSION.SDK_INT < 5)
            PlatformResolver.isDONUT = true;

        mHeight = metrics.heightPixels;
        mWidth = metrics.widthPixels;
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
            setVisibilityLabels(true);
            setTexts();
        }
    }

    private void setVisibilityLabels(boolean visibility) {
        for (TextView labal : labels)
            labal.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }


    class RadiusSurfaceView extends SurfaceView implements Runnable {

        Thread thread = null;
        SurfaceHolder surfaceHolder;
        volatile boolean running = false;

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Random random;
        int cx, cy;

        public RadiusSurfaceView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            cx = PlatformResolver.getCircleShiftX();
            cy = PlatformResolver.getCircleShiftY();
            surfaceHolder = getHolder();
            random = new Random();
            paint.setColor(Color.BLACK);
            this.setBackgroundResource(PlatformResolver.getMenuBG());
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

                    canvas.drawCircle(cx, cy, rad + 180 + (isSmallSreen ? -30 : 0), paint);
                    canvas.drawCircle(centerX, centerY, rad + 100, paint);


                    for (int j = 0; j < clocks; j++) {


                        for (int i = 0; i < clocks; i++) {
                            float x = (float) (centerX + rad * Math.cos(((float) i / clocks + shiftAngle) * 2 * Math.PI + currAngle));
                            float y = (float) (centerY + rad * Math.sin(((float) i / clocks + shiftAngle) * 2 * Math.PI + currAngle));
                            if (x < mWidth && y < mHeight) {
                                drawIcons(canvas, i, x, y);
                            }
                        }

                        for (int i = 0; i < clocks; i++) {
                            float x = (float) (cx + rad * Math.cos(((float) i / clocks + shiftAngle) * 2 * Math.PI - currAngle - Math.PI / 3));
                            float y = (float) (cy + rad * Math.sin(((float) i / clocks + shiftAngle) * 2 * Math.PI - currAngle - Math.PI / 3));
                            if (x > -100 && y > -100) {
                                drawIcons2(canvas, i, x, y);
                            }
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
                    canvas.drawBitmap(videobitmap, x + 3, y, paint);
                    break;
                case 4:
                case 10:
                    canvas.drawBitmap(rankebitmap, x, y, paint);
                    break;
                case 5:
                case 11:
                    canvas.drawBitmap(bookbitmap, x + 1, y - 5, paint);
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
                    canvas.drawBitmap(videobitmap, x + 3, y, paint);
                    break;
                case 1:
                case 7:
                    canvas.drawBitmap(rankebitmap, x, y, paint);
                    break;
                case 0:
                case 6:
                    canvas.drawBitmap(bookbitmap, x + 1, y - 5, paint);
                    break;

            }
        }

    }
}
