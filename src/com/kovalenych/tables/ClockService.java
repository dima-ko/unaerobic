package com.kovalenych.tables;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.widget.RemoteViews;
import com.kovalenych.Const;
import com.kovalenych.R;
import com.kovalenych.Table;
import com.kovalenych.Utils;

import java.util.ArrayList;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class ClockService extends Service implements Soundable, Const {

    private SoundManager mSoundManager;
    private int position;
    Table table;
    Vibrator v;
    PendingIntent pi;

    final String LOG_TAG = "myLogs";
    private static final int NOTIFY_ID = 1;
    private boolean vibrationEnabled;
    private ArrayList<Integer> voices;

    public void onCreate() {
        super.onCreate();
        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        mSoundManager = new SoundManager(this);
        Log.d(LOG_TAG, "MyService onCreate");
    }

    private void showProgressInTray(int progress, int max, boolean breathing) {
        Log.d("showProgressInTray", "zzzzzzzzz");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // Создаем экземпляр менеджера уведомлений
        int icon = R.drawable.tray_icon; // Иконка для уведомления, я решил воспользоваться стандартной иконкой для Email
        long when = System.currentTimeMillis(); // Выясним системное время
        Intent notificationIntent = new Intent(this, ClockActivity.class); // Создаем экземпляр Intent
        Notification notification = new Notification(icon, null, when); // Создаем экземпляр уведомления, и передаем ему наши параметры
        PendingIntent contentIntent = PendingIntent.getActivity(this, 5, notificationIntent, 0); // Подробное описание смотреть в UPD к статье
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notif); // Создаем экземпляр RemoteViews указывая использовать разметку нашего уведомления
        contentView.setProgressBar(R.id.tray_progress, max, progress, false);
        contentView.setTextViewText(R.id.tray_text, (breathing ? "breath  " : "hold  ") + Utils.timeToString(progress)); // Привязываем текст к TextView в нашей разметке
        notification.contentIntent = contentIntent; // Присваиваем contentIntent нашему уведомлению
        notification.contentView = contentView; // Присваиваем contentView нашему уведомлению
        mNotificationManager.notify(NOTIFY_ID, notification); // Выводим уведомление в строку

    }


    public void onDestroy() {
        super.onDestroy();
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(NOTIFY_ID);
        task.cancel(true);
        Log.d(LOG_TAG, "MyService onDestroy");
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService onStartCommand" + flags);

        String destination = intent.getStringExtra(FLAG);
        if (destination.equals(FLAG_CREATE)) {

            Bundle cyclesBundle = intent.getBundleExtra(ClockActivity.PARAM_CYCLES);
            pi = intent.getParcelableExtra(ClockActivity.PARAM_PINTENT);
            int size = cyclesBundle.getInt("tablesize");
            table = new Table();

            for (int i = 0; i < size; i++) {
                table.getCycles().add(
                        new Cycle(cyclesBundle.getInt("breathe" + Integer.toString(i)), cyclesBundle.getInt("hold" + Integer.toString(i)))
                );
            }

            position = cyclesBundle.getInt("number");
            vibrationEnabled = cyclesBundle.getBoolean("vibro");
            voices = cyclesBundle.getIntegerArrayList("voices");
            task = new ClockTask(table, true);
            task.execute(position);
        } else if (FLAG.equals(FLAG_EXIT)) {


        } else if (FLAG.equals(FLAG_LONG_CLICK)) {


        }

        return super.onStartCommand(intent, flags, startId);
    }

    ClockTask task;

    private void onTic(int time, int cycle, boolean breathing) {

        //evaluate percent progress
        int breathe = table.getCycles().get(cycle).breathe;
        int hold = table.getCycles().get(cycle).hold;
        int all = breathing ? breathe : hold;
        int percent = time * 360 / all;
        Log.d(LOG_TAG, "zzzzonTic  time: " + time);
        Intent intent = new Intent()
                .putExtra(ClockActivity.PARAM_TIME, time)
                .putExtra(ClockActivity.PARAM_PROGRESS, percent)
                .putExtra(ClockActivity.PARAM_BREATHING, breathing);
        try {
            int stat = breathing ? ClockActivity.STATUS_BREATH : ClockActivity.STATUS_HOLD;
            pi.send(ClockService.this, stat, intent);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        //vibrate  or sound

        if (time == 0 && vibrationEnabled)
            v.vibrate(300);
        if (breathing) {
            showProgressInTray(time, breathe, breathing);
            //breath
            int relatTime = time - breathe;
            if (time == 0 && voices.contains(BREATHE))
                mSoundManager.playSound(BREATHE);
            else if (voices.contains(relatTime))
                mSoundManager.playSound(relatTime);
        } else {
            showProgressInTray(time, hold, breathing);
            if (time == 0 && voices.contains(START))                 //hold
                mSoundManager.playSound(START);
            else if (voices.contains(time))
                mSoundManager.playSound(time);
        }
    }

    public void onTableFinish() {

        if (voices.contains(BREATHE))
            mSoundManager.playSound(BREATHE);
        if (vibrationEnabled)
            v.vibrate(300);

        Intent intent = new Intent()
                .putExtra(ClockActivity.PARAM_TIME, 0)
                .putExtra(ClockActivity.PARAM_PROGRESS, 0)
                .putExtra(ClockActivity.PARAM_BREATHING, false);
        try {
            pi.send(ClockService.this, ClockActivity.STATUS_FINISH, intent);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    class ClockTask extends AsyncTask<Integer, Integer, Void> {

        Table table;
        boolean breathing;

        public ClockTask(Table table, boolean breathing) {
            this.table = table;
            this.breathing = breathing;
        }

        @Override
        protected Void doInBackground(Integer... params) {

            for (int i = params[0]; i < table.getCycles().size(); i++) {
                if (breathing)
                    for (int t = 0; t < table.getCycles().get(params[0]).breathe; t++) {
                        publishProgress(t, params[0]);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return null;

                        }
                    }
                breathing = false;
                for (int t = 0; t < table.getCycles().get(params[0]).hold; t++) {
                    publishProgress(t, params[0]);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                breathing = true;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            onTic(values[0], values[1], breathing);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onTableFinish();
        }
    }
//
//
//    }
//
//        private void ifBreathing(Message msg) {
//            if (msg.arg1 == curCycle.breathe + 1) {
//                isBreathing = false;
//                timer.stopThread();
//                timer = new TimerThread(this);
//                timer.start();
//                breathBar.clearAnimation();
//                isAnimPlaying = false;
//                if (vibrationEnabled)
//                    v.vibrate(300);
//
//            } else {
//                breathTimeText.setVisibility(View.VISIBLE);
//                breathTimeText.setText(timeToString(msg.arg1));
//                holdTimeText.setVisibility(View.INVISIBLE);
//                if (msg.arg1 == curCycle.breathe / 2) {
//                    breathBar_left.setVisibility(View.INVISIBLE);
//                    breathBar_right.setVisibility(View.VISIBLE);
//                }
//                if (!isAnimPlaying) {
////                    breathBar.startAnimation(rot);
//                    isAnimPlaying = true;
//                }
////                Log.d("zzz", (msg.arg1 - curCycle.breathe) + "");
//                if (voices.contains(msg.arg1 - curCycle.breathe))
//                    mSoundManager.playSound(msg.arg1 - curCycle.breathe);
//
//            }
//        }
//
//        private void ifHolding(Message msg) {
//            if (msg.arg1 == curCycle.hold + 1) {
//                isBreathing = true;
//                timer.stopThread();
//                holdBar.clearAnimation();
//                isAnimPlaying = false;
//                mSoundManager.playSound(BREATHE);
//
//                position++;
//                if (position == table.getCycles().size())
//                    finish();
//                else {
//                    curCycle = table.getCycles().get(position);
//                    startCycle();
//                }
//                if (vibrationEnabled)
//                    v.vibrate(300);
//
//            } else {
//                holdTimeText.setVisibility(View.VISIBLE);
//                holdTimeText.setText(timeToString(msg.arg1));
//                breathTimeText.setVisibility(View.INVISIBLE);
//                if (msg.arg1 == curCycle.hold / 2) {
//                    holdBar_left.setVisibility(View.INVISIBLE);
//                    holdBar_right.setVisibility(View.VISIBLE);
//                }
//
//                if (!isAnimPlaying) {
////                    holdBar.startAnimation(rot2);
//                    isAnimPlaying = true;
//                }
////                Log.d("zzz", (msg.arg1) + "");
//                if (voices.contains(msg.arg1) && msg.arg1 != 0)
//                    mSoundManager.playSound(msg.arg1);
//            }
//        }
//    };
//
//    private boolean vibrationEnabled;
//

}
