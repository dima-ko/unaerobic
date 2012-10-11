package com.kovalenych.tables;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.kovalenych.*;

public class ClockActivity extends Activity implements Const {
    public static final int STOP_CLOCK_ID = 500;


    ClockView breathBar;
    ClockView holdBar;
    ImageView holdBar_left;
    ImageView holdBar_right;

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
                PendingIntent pi = createPendingResult(1, null, 0);
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
        pi = createPendingResult(1, null, 0);
        // Создаем Intent для вызова сервиса, кладем туда параметр времени
        // и созданный PendingIntent
        intent = new Intent(this, ClockService.class)
                .putExtra(FLAG, FLAG_CREATE)
                .putExtra(PARAM_CYCLES, bun)
                .putExtra(PARAM_PINTENT, pi)
                .putExtra(PARAM_TABLE, bun.getString("table_name"));
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
        paramsLeft.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        parent.addView(leftCircle, paramsLeft);

        RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(w, w);
        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsRight.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        parent.addView(rightCircle, paramsRight);

        stopButton = new Button(this);
        stopButton.setId(STOP_CLOCK_ID);
        stopButton.setBackgroundResource(R.drawable.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w / 4, w / 4);
        params.setMargins(0, 0, 0, 5);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        parent.addView(stopButton, params);


        holdBar = (ClockView) rightCircle.findViewById(R.id.run_static_progress);
        breathBar = (ClockView) leftCircle.findViewById(R.id.run_ventilate_progress);


//        topTimeText = (TextView) findViewById(R.id.topTime);
        breathTimeText = (TextView) leftCircle.findViewById(R.id.run_time_breath);
        holdTimeText = (TextView) rightCircle.findViewById(R.id.run_time_hold);

        setListeners();

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
        PendingIntent pi = createPendingResult(1, null, 0);
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

    public void setListeners() {

        breathTimeText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startService(new Intent(ptr, ClockService.class)
                        .putExtra(FLAG, FLAG_CLICK_BREATH)
                );
                return true;
            }
        });

        holdTimeText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                startService(new Intent(ptr, ClockService.class)
                        .putExtra(FLAG, FLAG_CLICK_HOLD)
                );
                return true;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onActivityResult" + resultCode);
        int time = data.getIntExtra(ClockActivity.PARAM_TIME, 0);
        int wholeTime = data.getIntExtra(ClockActivity.PARAM_PROGRESS, 0);
        Log.d("zzzzzzz", wholeTime + "percent");
        if (resultCode == STATUS_BREATH) {
            if (breathTimeText.getVisibility() != View.VISIBLE)
                breathTimeText.setVisibility(View.VISIBLE);
            if (holdTimeText.getVisibility() == View.VISIBLE)
                holdTimeText.setVisibility(View.INVISIBLE);
            breathBar.angle = wholeTime;
            String showTime = Utils.timeToString(countDown ? (wholeTime - time) : time);
            breathTimeText.setText(showTime);
        } else if (resultCode == STATUS_HOLD) {
            if (holdTimeText.getVisibility() != View.VISIBLE)
                holdTimeText.setVisibility(View.VISIBLE);
            if (breathTimeText.getVisibility() == View.VISIBLE)    //TODO rotate timer when portrait
                breathTimeText.setVisibility(View.INVISIBLE);
            String showTime = Utils.timeToString(countDown ? (wholeTime - time) : time);
            holdTimeText.setText(showTime);
            holdBar.angle = wholeTime;
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
