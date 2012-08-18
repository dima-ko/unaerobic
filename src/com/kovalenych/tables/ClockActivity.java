package com.kovalenych.tables;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.kovalenych.Fonts;
import com.kovalenych.R;
import com.kovalenych.Table;
import com.kovalenych.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ClockActivity extends Activity implements Soundable {


    Cycle curCycle;

    boolean isBreathing = false;
    boolean isAnimPlaying = false;

    private static final String LOG_TAG = "zzClockActivity";

//    RotImageView breathBar;
    //    ImageView breathBar_left;
//    ImageView breathBar_right;
//    RotImageView holdBar;
//    ImageView holdBar_left;
//    ImageView holdBar_right;

    TextView breathTimeText, holdTimeText;


    RelativeLayout parent;
    Activity ptr;


    public final static String PARAM_CYCLES = "cycles";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_PROGRESS = "progress";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_BREATHING = "breathing";
    public static final int STATUS_BREATH = 1;
    public static final int STATUS_HOLD = 2;
    public static final int STATUS_FINISH = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ptr = this;
        Bundle bun = getIntent().getExtras();

        initViews();

        setContentView(parent);    //TODO: show in status bar checkbox

        PendingIntent pi;
        Intent intent;

        // Создаем PendingIntent для Task1
        pi = createPendingResult(1, null, 0);
        // Создаем Intent для вызова сервиса, кладем туда параметр времени
        // и созданный PendingIntent
        intent = new Intent(this, ClockService.class)
                .putExtra(PARAM_CYCLES, bun)
                .putExtra(PARAM_PINTENT, pi);
        // стартуем сервис
        startService(intent);

    }

    Button stopButton;

    public void initViews() {

        parent = new RelativeLayout(this);
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout leftCircle = (RelativeLayout) inflater.inflate(R.layout.clocks_left, null, false);
        RelativeLayout rightCircle = (RelativeLayout) inflater.inflate(R.layout.clocks_right, null, false);

        int w = Utils.smaller2dim - 30;

        RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(w, w);
        parent.addView(leftCircle, paramsLeft);

        RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(w, w);
        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        parent.addView(rightCircle, paramsRight);

        stopButton = new Button(this);
        stopButton.setText("STOP");
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(ptr, ClockService.class));
                finish();
            }
        });

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        parent.addView(stopButton, params);


//        breathBar = (RotImageView) leftCircle.findViewById(R.id.run_ventilate_progress);
//        AssetManager mngr = getAssets();
//        // Create an input stream to read from the asset folder
//        InputStream ins = null;
//        try {
//            ins = mngr.open("progress_green_left_png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Convert the input stream into a bitmap
//        Bitmap map = BitmapFactory.decodeStream(ins);
//        breathBar.setBitmap(map);
//        breathBar_left = (ImageView) leftCircle.findViewById(R.id.run_ventilate_progress_left);
//        breathBar_right = (ImageView) leftCircle.findViewById(R.id.run_ventilate_progress_right);
//        holdBar = (RotImageView) rightCircle.findViewById(R.id.run_static_progress);
//        holdBar_left = (ImageView) rightCircle.findViewById(R.id.run_static_progress_left);
//        holdBar_right = (ImageView) rightCircle.findViewById(R.id.run_static_progress_right);


//        topTimeText = (TextView) findViewById(R.id.topTime);
        breathTimeText = (TextView) leftCircle.findViewById(R.id.run_time_breath);
        holdTimeText = (TextView) rightCircle.findViewById(R.id.run_time_hold);

//        breathTimeText.setTypeface(Fonts.BELIGERENT);
//        holdTimeText.setTypeface(Fonts.BELIGERENT);
//        topTimeText.setTypeface(Fonts.BELIGERENT);

        setListeners();

    }


    public void setListeners() {


//        breathBar.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//
//                isBreathing = true;
//                return true;
//
//            }
//        });
//
//        holdBar.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                isBreathing = false;
//                return true;
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int time = data.getIntExtra(ClockActivity.PARAM_TIME, 0);
        int percent = data.getIntExtra(ClockActivity.PARAM_PROGRESS, 0);
        Log.d("zzzzzzz", percent + "");
        if (resultCode == STATUS_BREATH) {
            if (breathTimeText.getVisibility() != View.VISIBLE)
                breathTimeText.setVisibility(View.VISIBLE);
            if (holdTimeText.getVisibility() == View.VISIBLE)
                holdTimeText.setVisibility(View.INVISIBLE);
//            breathBar.angle = percent;
            breathTimeText.setText(timeToString(time));
        } else if (resultCode == STATUS_HOLD) {
            if (holdTimeText.getVisibility() != View.VISIBLE)
                holdTimeText.setVisibility(View.VISIBLE);
            if (breathTimeText.getVisibility() == View.VISIBLE)
                breathTimeText.setVisibility(View.INVISIBLE);
            holdTimeText.setText(timeToString(time));
//            holdBar.angle = percent;
        } else if (resultCode == STATUS_FINISH) {
            finish();
        }
    }

    public String timeToString(int time) {
        int min = time / 60;
        int sec = time - min * 60;
        return String.format("%02d:%02d", min, sec);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK &&
//                event.getAction() == KeyEvent.ACTION_DOWN) {
//            timer.stopThread();
//            Intent intent = new Intent(ClockActivity.this, CyclesActivity.class);
//            setResult(2, intent);
//
//            ClockActivity.this.finish();
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}
