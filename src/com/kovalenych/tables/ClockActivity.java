package com.kovalenych.tables;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.kovalenych.*;

public class ClockActivity extends Activity implements Const {
    public static final int STOP_CLOCK_ID = 500;
    public static final int TOP_CIRCLE_ID = 234;
    private static final int BOTTOM_CIRCLE_ID = 4123;
    ClockView breathBar;

    ClockView holdBar;

    TextView breathTimeText, holdTimeText;
    RelativeLayout parent;
    Activity ptr;

    public boolean addTray = true;
    public boolean countDown;
    public boolean prefTray;
    private SharedPreferences _preferences;


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
                stopService(new Intent(ptr, ClockService.class));
                Log.d(LOG_TAG, "stopButt");
                addTray = false;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        contrButton = (RelativeLayout) tabHost.findViewById(R.id.stop_button_host);
        contrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        holdBar = (ClockView) rightCircle.findViewById(R.id.run_static_progress);
        holdBar.setDimensions(w);
        breathBar = (ClockView) leftCircle.findViewById(R.id.run_ventilate_progress);
        breathBar.setDimensions(w);


//        topTimeText = (TextView) findViewById(R.id.topTime);
        breathTimeText = (TextView) leftCircle.findViewById(R.id.run_time_breath);
        holdTimeText = (TextView) rightCircle.findViewById(R.id.run_time_hold);
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
        if (resultCode == STATUS_BREATH) {
            if (breathTimeText.getVisibility() != View.VISIBLE) {
                breathTimeText.setVisibility(View.VISIBLE);
                holdBar.angle = 0;
                holdBar.invalidateClock();
            }
            if (holdTimeText.getVisibility() == View.VISIBLE)
                holdTimeText.setVisibility(View.INVISIBLE);
            breathBar.angle = (float) (time * 2 * Math.PI) / wholeTime;
            breathBar.invalidateClock();
            String showTime = Utils.timeToString(countDown ? (wholeTime - time) : time);
            breathTimeText.setText(showTime);
        } else if (resultCode == STATUS_HOLD) {
            if (holdTimeText.getVisibility() != View.VISIBLE) {
                holdTimeText.setVisibility(View.VISIBLE);
                breathBar.angle = (float) (2 * Math.PI);
                breathBar.invalidateClock();
            }
            if (breathTimeText.getVisibility() == View.VISIBLE)    //TODO rotate timer when portrait
                breathTimeText.setVisibility(View.INVISIBLE);
            String showTime = Utils.timeToString(countDown ? (wholeTime - time) : time);
            holdTimeText.setText(showTime);
            holdBar.angle = (float) (time * 2 * Math.PI) / wholeTime;
            holdBar.invalidateClock();
        } else if (resultCode == STATUS_FINISH) {
            Log.d(LOG_TAG, "onActivityResult STATUS_FINISH");
            addTray = false;
            finish();
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
