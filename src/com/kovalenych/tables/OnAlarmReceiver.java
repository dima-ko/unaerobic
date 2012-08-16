package com.kovalenych.tables;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class OnAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
//        clockService.onClock();
        Toast.makeText(context, "Service work!", Toast.LENGTH_SHORT).show();
        Log.d(this.getClass().getName(), "zzzzService catch alarm message at: "
                + new java.util.Date().toString());
    }

    //FOR SERVICE
//    public static final int FIRST_RUN = 1000; // 5 seconds
//    public static final int INTERVAL = 5000; // 1 sec
//
//    AlarmManager alarmManager;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        Intent intent = new Intent(this, OnAlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//
//        this.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        this.alarmManager.setRepeating(
//                AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + FIRST_RUN,
//                INTERVAL, pendingIntent);
//
//        Log.v(this.getClass().getName(), "zzzAlarmManger binding at "
//                + new java.util.Date().toString());
//        Log.v(this.getClass().getName(), "zzzzService created.");
//    }
//
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.d(this.getClass().getName(), "zzzzzService binded.");
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        if (alarmManager != null) {
//            Intent intent = new Intent(this, OnAlarmReceiver.class);
//            alarmManager.cancel(PendingIntent.getBroadcast(this, 0, intent, 0));
//            Log.v(this.getClass().getName(), "AlarmManger unbinding at "
//                    + new java.util.Date().toString());
//        }
//        Log.v(this.getClass().getName(), "Service destoyed.");
//    }
}