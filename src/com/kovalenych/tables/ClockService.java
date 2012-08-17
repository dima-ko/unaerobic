package com.kovalenych.tables;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import com.kovalenych.Table;

import java.util.concurrent.TimeUnit;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class ClockService extends Service {

    private SoundManager mSoundManager;
    private int position;
    Table table;
    Vibrator v;

    final String LOG_TAG = "myLogs";

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "MyService onDestroy");
    }

    PendingIntent pi;

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "MyService onStartCommand");

        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        mSoundManager = new SoundManager(this);

        Bundle bundle = intent.getBundleExtra(ClockActivity.PARAM_CYCLES);

        pi = intent.getParcelableExtra(ClockActivity.PARAM_PINTENT);

        int size = bundle.getInt("tablesize");
        table = new Table();

        for (int i = 0; i < size; i++) {
            table.getCycles().add(
                    new Cycle(bundle.getInt("breathe" + Integer.toString(i)), bundle.getInt("hold" + Integer.toString(i)))
            );
        }

        position = bundle.getInt("number");
//        vibrationEnabled = bundle.getBoolean("vibro");
//        voices = bundle.getIntegerArrayList("voices");
        task = new ClockTask(table, true);
        task.execute(position);

        return super.onStartCommand(intent, flags, startId);
    }

    ClockTask task;

    private void onTic(Integer value, Integer cycle, boolean breathing) {
        Intent intent = new Intent().putExtra(ClockActivity.PARAM_PROGRESS, 0 * 100);
        try {
            int stat = breathing ? ClockActivity.STATUS_BREATH : ClockActivity.STATUS_HOLD;
            pi.send(ClockService.this, stat, intent);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

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
                        }
                    }
                else
                    for (int t = 0; t < table.getCycles().get(params[0]).hold; t++) {
                        publishProgress(t, params[0]);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

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
        }
    }

//    class MyRun implements
//    Runnable {
//
//        int time;
//        int startId;
//        PendingIntent pi;
//
//        public MyRun(int time, int startId, PendingIntent pi) {
//            this.time = time;
//            this.startId = startId;
//            this.pi = pi;
//            Log.d(LOG_TAG, "MyRun#" + startId + " create");
//        }
//
//        public void run() {
//            Log.d(LOG_TAG, "MyRun#" + startId + " start, time = " + time);
//            try {
//                // сообщаем об старте задачи
//                pi.send(MainActivity.STATUS_START);
//
//                // начинаем выполнение задачи
//                TimeUnit.SECONDS.sleep(1);
//
//                // сообщаем об окончании задачи
//                Intent intent = new Intent().putExtra(MainActivity.PARAM_RESULT, time * 100);
//                pi.send(MyService.this, MainActivity.STATUS_FINISH, intent);
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (PendingIntent.CanceledException e) {
//                e.printStackTrace();
//            }
//            stop();
//        }
//
//        void stop() {
//            Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelfResult("
//                    + startId + ") = " + stopSelfResult(startId));
//        }
//    }
//    List voices;
//    TimerThread timer;


//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        curCycle = new Cycle(0, 0);
//
//
//
//
//
//
//        curCycle = table.getCycles().get(position);
//
//
//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // Создаем экземпляр менеджера уведомлений
//        int icon = R.drawable.tray_icon; // Иконка для уведомления, я решил воспользоваться стандартной иконкой для Email
//        long when = System.currentTimeMillis(); // Выясним системное время
//        Intent notificationIntent = new Intent(this, ClockActivity.class); // Создаем экземпляр Intent
//        Notification notification = new Notification(icon, null, when); // Создаем экземпляр уведомления, и передаем ему наши параметры
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0); // Подробное описание смотреть в UPD к статье
//        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notif); // Создаем экземпляр RemoteViews указывая использовать разметку нашего уведомления
////        contentView.setImageViewResource(R.id.image, R.drawable.tray_icon); // Привязываем нашу картинку к ImageView в разметке уведомления
////        contentView.setTextViewText(R.id.text,"Привет Habrahabr! А мы тут, плюшками балуемся..."); // Привязываем текст к TextView в нашей разметке
//        notification.contentIntent = contentIntent; // Присваиваем contentIntent нашему уведомлению
//        notification.contentView = contentView; // Присваиваем contentView нашему уведомлению
//        mNotificationManager.notify(NOTIFY_ID, notification); // Выводим уведомление в строку
//
//    }
//
//    final Handler handler = new Handler() {
//
//        public void handleMessage(Message msg) {
//            Log.d("handler1", msg.toString());
//
//            if (isBreathing) {
//
//                ifBreathing(msg);
//
//            } else {
//
//                ifHolding(msg);
//
//            }
//        }
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
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    private class TimerThread extends Thread {
//
//        Handler handler;
//        int i;
//
//        TimerThread(Handler handler) {
//            i = 0;
//            this.handler = handler;
////            Log.d("zzzzthread", "TimerThread ");
//
//        }
//
//        boolean isRunning = true;
//
//        public void stopThread() {
//            isRunning = false;
//        }
//
//        public void run() {
//
//            while (isRunning) {
//                Message msg = new Message();
//                msg.arg1 = i;
//                handler.sendMessage(msg);
////                Log.d("zzzzthread", "tick " + i);
//                i++;
//                try {
//                    Thread.sleep(1000);
//
//                } catch (InterruptedException e) {
//                    Log.d("InterruptedException", "thread");
//                    return;
//                }
//
//
//            }
//
//        }
//
//
//    }

}