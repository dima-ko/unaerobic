package com.kovalenych.tables;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
        Toast.makeText(context, "Service work!", Toast.LENGTH_SHORT).show();
        Log.d(this.getClass().getName(), "zzzzService catch alarm message at: "
                + new java.util.Date().toString());
    }
}