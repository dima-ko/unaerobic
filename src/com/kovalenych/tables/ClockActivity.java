package com.kovalenych.tables;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.kovalenych.*;

public class ClockActivity extends Activity implements Const {
    public static final int STOP_CLOCK_ID = 500;
    public static final int TOP_CIRCLE_ID = 234;
    private static final int BOTTOM_CIRCLE_ID = 4123;
    private static final int SMALL_CIRCLE_ID = 4124;
    ClockView smallBar;
    ClockView breathBar;
    ClockView holdBar;

    TextView breathTimeText, holdTimeText;
    RelativeLayout parent;
    Activity ptr;

    public boolean addTray = true;
    public boolean countDown;
    public boolean prefTray;
    private SharedPreferences _preferences;
    private TextView holdTimeTextHint;
    private TextView smallTimeText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ptr = this;
        Bundle bun = getIntent().getExtras();
        _preferences = getSharedPreferences("clockPrefs", MODE_PRIVATE);
        prefTray = _preferences.getBoolean("ShowTray", true);
        countDown = _preferences.getBoolean("countdown", false);

        initViews();
        Log.d(LOG_TAG, "onCreate");

        setContentView(parent);

        if (Utils.isMyServiceRunning(this)) {

            if (bun == null || bun.getBoolean("isRunning")) {
                PendingIntent pi = createPendingResult(1, new Intent(), 0);
                Intent intent = new Intent(this, ClockService.class)
                        .putExtra(FLAG, FLAG_HIDE_TRAY)
                        .putExtra(PARAM_PINTENT, pi);
                startService(intent);
                Log.d(LOG_TAG, "createService FLAG_SHOW_TRAY");
            } else {
                stopService(new Intent(ptr, ClockService.class));
                createService(bun);
            }
        } else {
            createService(bun);
        }

    }


    private void createService(Bundle bun) {
        PendingIntent pi;
        Intent intent;
        Log.d(LOG_TAG, "createService");

        // Создаем PendingIntent для Task1
        pi = createPendingResult(1, new Intent(), 0);
        // Создаем Intent для вызова сервиса, кладем туда параметр времени
        // и созданный PendingIntent
        intent = new Intent(this, ClockService.class)
                .putExtra(FLAG, FLAG_CREATE)
                .putExtra(PARAM_CYCLES, bun)
                .putExtra(PARAM_PINTENT, pi)
                .putExtra(PARAM_VOLUME, bun.getInt(PARAM_VOLUME))
                .putExtra(PARAM_TABLE, bun.getString("table_name"));
        // стартуем сервис
        startService(intent);
    }

    RelativeLayout stopButton;
    RelativeLayout contrButton;

    public void initViews() {

        parent = new RelativeLayout(this);
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout leftCircle = (RelativeLayout) inflater.inflate(R.layout.clocks_left, null, false);
        RelativeLayout rightCircle = (RelativeLayout) inflater.inflate(R.layout.clocks_right, null, false);
        RelativeLayout smallCircle = (RelativeLayout) inflater.inflate(R.layout.clocks_small, null, false);

        int w = (int) (Utils.height * 0.4);

        RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(w, w);
        paramsLeft.setMargins(0, (int) (Utils.smaller2dim * 0.04), 0, 0);
        paramsLeft.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        leftCircle.setId(TOP_CIRCLE_ID);
        parent.addView(leftCircle, paramsLeft);

        RelativeLayout.LayoutParams paramsBottom = new RelativeLayout.LayoutParams(w, w);
        paramsBottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        paramsBottom.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        rightCircle.setId(BOTTOM_CIRCLE_ID);
        paramsBottom.setMargins(0, 0, 0, (int) (Utils.smaller2dim * 0.04));
        parent.addView(rightCircle, paramsBottom);

        RelativeLayout.LayoutParams paramsSmall = new RelativeLayout.LayoutParams(w/3, w/3);
        smallCircle.setId(SMALL_CIRCLE_ID);
        parent.addView(smallCircle, paramsSmall);

        LinearLayout tabHost = (LinearLayout) inflater.inflate(R.layout.tab_host, null, false);
        RelativeLayout.LayoutParams paramsCenter = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        paramsCenter.addRule(RelativeLayout.BELOW, TOP_CIRCLE_ID);
        paramsCenter.addRule(RelativeLayout.ABOVE, BOTTOM_CIRCLE_ID);
        rightCircle.setId(BOTTOM_CIRCLE_ID);
        paramsCenter.setMargins(0, (int) (Utils.smaller2dim * 0.05),
                0, (int) (Utils.smaller2dim * 0.05));
        parent.addView(tabHost, paramsCenter);

        stopButton = (RelativeLayout) tabHost.findViewById(R.id.stop_button_host);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "stopButt");
                stopService(new Intent(ptr, ClockService.class));
                addTray = false;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        contrButton = (RelativeLayout) tabHost.findViewById(R.id.contraction);
        contrButton.setVisibility(View.GONE);
        contrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, FLAG_CONTRACTION);
                Intent intent = new Intent(ptr, ClockService.class)
                        .putExtra(FLAG, FLAG_CONTRACTION);
                // стартуем сервис
                startService(intent);
            }
        });

        holdBar = (ClockView) rightCircle.findViewById(R.id.run_static_progress);
        holdBar.setDimensions(w);
        rightCircle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(LOG_TAG, FLAG_IMMEDIATE_BREATH);
                Intent intent = new Intent(ptr, ClockService.class)
                        .putExtra(FLAG, FLAG_IMMEDIATE_BREATH);
                // стартуем сервис
                startService(intent);
                return true;
            }
        });
        breathBar = (ClockView) leftCircle.findViewById(R.id.run_ventilate_progress);
        breathBar.setDimensions(w);
        smallBar = (ClockView) smallCircle.findViewById(R.id.whole_time);
        smallBar.setDimensions(w/3);

        breathTimeText = (TextView) leftCircle.findViewById(R.id.run_time_breath);
        smallTimeText = (TextView) smallCircle.findViewById(R.id.small_text);
        holdTimeText = (TextView) rightCircle.findViewById(R.id.run_time_hold);
        holdTimeTextHint = (TextView) rightCircle.findViewById(R.id.run_time_hold_hint);
    }

    private static final String LOG_TAG = "CO2 ClockActivity";


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LOG_TAG, "onRestart");
        PendingIntent pi = createPendingResult(1, new Intent(), 0);
        Intent intent = new Intent(this, ClockService.class)
                .putExtra(FLAG, FLAG_HIDE_TRAY)
                .putExtra(PARAM_PINTENT, pi);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop");
        super.onStop();
        if (addTray && prefTray)
            startService(new Intent(this, ClockService.class)
                    .putExtra(FLAG, FLAG_SHOW_TRAY));
        SharedPreferences.Editor editor = _preferences.edit();
        editor.putBoolean("ShowTray", prefTray);
        editor.putBoolean("countdown", countDown);
        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onActivityResult" + resultCode);
        int time = data.getIntExtra(ClockActivity.PARAM_TIME, 0);
        int wholeTime = data.getIntExtra(ClockActivity.PARAM_PROGRESS, 0);
        Log.d("zzzzzzz", wholeTime + "percent");
        smallBar.angle = (float) (time * 2 * Math.PI) / wholeTime;
        smallBar.invalidateClock(R.drawable.progress_grey);
        if (resultCode == STATUS_BREATH) {
            contrButton.setVisibility(View.GONE);
            if (breathTimeText.getVisibility() != View.VISIBLE) {
                breathTimeText.setVisibility(View.VISIBLE);
                holdBar.angle = 0;
                holdBar.invalidateClock(R.drawable.progress_dark_blue);
            }
            if (holdTimeText.getVisibility() == View.VISIBLE) {
                holdTimeText.setVisibility(View.INVISIBLE);
                holdTimeTextHint.setVisibility(View.INVISIBLE);
            }
            breathBar.angle = (float) (time * 2 * Math.PI) / wholeTime;
            breathBar.invalidateClock(R.drawable.progress_blue);
            String showTime = Utils.timeToString(countDown ? (wholeTime - time) : time);
            breathTimeText.setText(showTime);
        } else if (resultCode == STATUS_HOLD) {
            contrButton.setVisibility(View.VISIBLE);
            if (holdTimeText.getVisibility() != View.VISIBLE) {
                holdTimeText.setVisibility(View.VISIBLE);
                holdTimeTextHint.setVisibility(View.VISIBLE);
                breathBar.angle = (float) (2 * Math.PI);
                breathBar.invalidateClock(R.drawable.progress_blue);
            }
            if (breathTimeText.getVisibility() == View.VISIBLE)
                breathTimeText.setVisibility(View.INVISIBLE);
            String showTime = Utils.timeToString(countDown ? (wholeTime - time) : time);
            holdTimeText.setText(showTime);
            holdBar.angle = (float) (time * 2 * Math.PI) / wholeTime;
            holdBar.invalidateClock(R.drawable.progress_dark_blue);
        } else if (resultCode == STATUS_FINISH) {
            Log.d(LOG_TAG, "onActivityResult STATUS_FINISH");
            addTray = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 300);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.clock_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.countdown:
                countDown = !countDown;
                return true;
            case R.id.menu_tray:
                prefTray = !prefTray;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
