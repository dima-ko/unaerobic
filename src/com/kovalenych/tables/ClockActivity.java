package com.kovalenych.tables;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class ClockActivity extends Activity implements Soundable {


    Cycle curCycle;

    boolean isBreathing = false;
    boolean isAnimPlaying = false;

    private static final String LOG_TAG = "ClockActivity";

    ImageView breathBar;
    ImageView breathBar_left;
    ImageView breathBar_right;
    ImageView holdBar;
    ImageView holdBar_left;
    ImageView holdBar_right;

    TextView topTimeText, breathTimeText, holdTimeText;

    List voices;
    Activity ptr;


    TimerThread timer;
    Animation rot, rot2;
    private SoundManager mSoundManager;
    private int position;
    Table table;
    Vibrator v;
    RelativeLayout parent;
    private static final int NOTIFY_ID = 1; // Ун

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ptr = this;
        v = (Vibrator) getSystemService(ptr.VIBRATOR_SERVICE);
        mSoundManager = new SoundManager(this);
        curCycle = new Cycle(0, 0);
        Bundle bun = getIntent().getExtras();

        int size = bun.getInt("tablesize");

        table = new Table();

        for (int i = 0; i < size; i++) {
            table.getCycles().add(
                    new Cycle(bun.getInt("breathe" + Integer.toString(i)), bun.getInt("hold" + Integer.toString(i)))
            );
        }

        position = bun.getInt("number");
        vibrationEnabled = bun.getBoolean("vibro");
        voices = bun.getIntegerArrayList("voices");


        initViews();

        setContentView(parent);

        curCycle = table.getCycles().get(position);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // Создаем экземпляр менеджера уведомлений
        int icon = R.drawable.tray_icon; // Иконка для уведомления, я решил воспользоваться стандартной иконкой для Email
        long when = System.currentTimeMillis(); // Выясним системное время
        Intent notificationIntent = new Intent(this, ClockActivity.class); // Создаем экземпляр Intent
        Notification notification = new Notification(icon, null, when); // Создаем экземпляр уведомления, и передаем ему наши параметры
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0); // Подробное описание смотреть в UPD к статье
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notif); // Создаем экземпляр RemoteViews указывая использовать разметку нашего уведомления
//        contentView.setImageViewResource(R.id.image, R.drawable.tray_icon); // Привязываем нашу картинку к ImageView в разметке уведомления
//        contentView.setTextViewText(R.id.text,"Привет Habrahabr! А мы тут, плюшками балуемся..."); // Привязываем текст к TextView в нашей разметке
        notification.contentIntent = contentIntent; // Присваиваем contentIntent нашему уведомлению
        notification.contentView = contentView; // Присваиваем contentView нашему уведомлению
        mNotificationManager.notify(NOTIFY_ID, notification); // Выводим уведомление в строку

        startCycle();

        startService(new Intent(this,ClockService.class));


    }

    Button stopButton;

    public void initViews() {

        parent = new RelativeLayout(this);
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout leftCircle = (RelativeLayout) inflater.inflate(R.layout.clocks_left, null, false);
        RelativeLayout rightCircle = (RelativeLayout) inflater.inflate(R.layout.clocks_right, null, false);

        int w = Utils.smaller2dim - 30;

        RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(w, w);
//        paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        parent.addView(leftCircle, paramsLeft);

        RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(w, w);
//        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        parent.addView(rightCircle, paramsRight);

        stopButton = new Button(this);
        stopButton.setText("STOP");


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        parent.addView(stopButton, params);


        breathBar = (ImageView) leftCircle.findViewById(R.id.run_ventilate_progress);
        breathBar_left = (ImageView) leftCircle.findViewById(R.id.run_ventilate_progress_left);
        breathBar_right = (ImageView) leftCircle.findViewById(R.id.run_ventilate_progress_right);
        holdBar = (ImageView) rightCircle.findViewById(R.id.run_static_progress);
        holdBar_left = (ImageView) rightCircle.findViewById(R.id.run_static_progress_left);
        holdBar_right = (ImageView) rightCircle.findViewById(R.id.run_static_progress_right);


//        topTimeText = (TextView) findViewById(R.id.topTime);
        breathTimeText = (TextView) leftCircle.findViewById(R.id.run_time_breath);
        holdTimeText = (TextView) rightCircle.findViewById(R.id.run_time_hold);

        breathTimeText.setTypeface(Fonts.BELIGERENT);
        holdTimeText.setTypeface(Fonts.BELIGERENT);
//        topTimeText.setTypeface(Fonts.BELIGERENT);

        rot = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        rot2 = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);


        setListeners();

    }

    public void startCycle() {

        isBreathing = true;
        int itemTimeB = (short) curCycle.breathe;
        int itemTimeH = (short) curCycle.hold;
        rot.setDuration(itemTimeB * 1000);
        rot2.setDuration(itemTimeH * 1000);
//        topTimeText.setText(curCycle.convertToString());
        timer = new TimerThread(handler);
        timer.start();
        holdBar_left.setVisibility(View.VISIBLE);
        holdBar_right.setVisibility(View.INVISIBLE);
        breathBar_left.setVisibility(View.VISIBLE);
        breathBar_right.setVisibility(View.INVISIBLE);
        isAnimPlaying = false;
    }

    public void setListeners() {


        breathBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                isBreathing = true;
                timer.interrupt();
                timer = new TimerThread(handler);
                timer.start();
                holdBar.clearAnimation();
                isAnimPlaying = false;
                breathBar_left.setVisibility(View.VISIBLE);
                breathBar_right.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        holdBar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBreathing = false;
                timer.interrupt();
                timer = new TimerThread(handler);
                timer.start();
                breathBar.clearAnimation();
                isAnimPlaying = false;
                holdBar_left.setVisibility(View.VISIBLE);
                holdBar_right.setVisibility(View.INVISIBLE);
                return true;
            }
        });


    }

    public String timeToString(int time) {
        int min = time / 60;
        int sec = time - min * 60;
        return String.format("%02d:%02d", min, sec);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN) {
            timer.stopThread();
            Intent intent = new Intent(ClockActivity.this, CyclesActivity.class);
            setResult(2, intent);

            ClockActivity.this.finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean vibrationEnabled;

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            Log.d("handler1", msg.toString());

            if (isBreathing) {

                ifBreathing(msg);

            } else {

                ifHolding(msg);

            }
        }

        private void ifBreathing(Message msg) {
            if (msg.arg1 == curCycle.breathe + 1) {
                isBreathing = false;
                timer.stopThread();
                timer = new TimerThread(this);
                timer.start();
                breathBar.clearAnimation();
                isAnimPlaying = false;
                if (vibrationEnabled)
                    v.vibrate(300);

            } else {
                breathTimeText.setVisibility(View.VISIBLE);
                breathTimeText.setText(timeToString(msg.arg1));
                holdTimeText.setVisibility(View.INVISIBLE);
                if (msg.arg1 == curCycle.breathe / 2) {
                    breathBar_left.setVisibility(View.INVISIBLE);
                    breathBar_right.setVisibility(View.VISIBLE);
                }
                if (!isAnimPlaying) {
//                    breathBar.startAnimation(rot);
                    isAnimPlaying = true;
                }
//                Log.d("zzz", (msg.arg1 - curCycle.breathe) + "");
                if (voices.contains(msg.arg1 - curCycle.breathe))
                    mSoundManager.playSound(msg.arg1 - curCycle.breathe);

            }
        }

        private void ifHolding(Message msg) {
            if (msg.arg1 == curCycle.hold + 1) {
                isBreathing = true;
                timer.stopThread();
                holdBar.clearAnimation();
                isAnimPlaying = false;
                mSoundManager.playSound(BREATHE);

                position++;
                if (position == table.getCycles().size())
                    finish();
                else {
                    curCycle = table.getCycles().get(position);
                    startCycle();
                }
                if (vibrationEnabled)
                    v.vibrate(300);

            } else {
                holdTimeText.setVisibility(View.VISIBLE);
                holdTimeText.setText(timeToString(msg.arg1));
                breathTimeText.setVisibility(View.INVISIBLE);
                if (msg.arg1 == curCycle.hold / 2) {
                    holdBar_left.setVisibility(View.INVISIBLE);
                    holdBar_right.setVisibility(View.VISIBLE);
                }

                if (!isAnimPlaying) {
//                    holdBar.startAnimation(rot2);
                    isAnimPlaying = true;
                }
//                Log.d("zzz", (msg.arg1) + "");
                if (voices.contains(msg.arg1) && msg.arg1 != 0)
                    mSoundManager.playSound(msg.arg1);
            }
        }
    };

    private class TimerThread extends Thread {

        Handler handler;
        int i;

        TimerThread(Handler handler) {
            i = 0;
            this.handler = handler;
//            Log.d("zzzzthread", "TimerThread ");

        }

        boolean isRunning = true;

        public void stopThread() {
            isRunning = false;
        }

        public void run() {

            while (isRunning) {
                Message msg = new Message();
                msg.arg1 = i;
                handler.sendMessage(msg);
//                Log.d("zzzzthread", "tick " + i);
                i++;
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    Log.d("InterruptedException", "thread");
                    return;
                }


            }

        }


    }


}
