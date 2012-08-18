package com.kovalenych;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;

public class Utils {

    public static boolean isHoneyCombOrMore = false;
    public static int height;
    public static int width;
    public static int flowidth;
    public static int smaller2dim;
    public static int smallerDim;
    public static boolean isTab;
    public static boolean isSmall;
    public static boolean isPortrait;
    public static float scale;

    public static void resolvePlatform(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        width = metrics.widthPixels;
        int vers = Integer.parseInt(Build.VERSION.RELEASE.substring(0, 1));
        if (vers > 2) Utils.isHoneyCombOrMore = true;

        int screenSizeType = activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSizeType == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            isTab = true;
        } else if (screenSizeType == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            isSmall = true;
        }

        flowidth = isTab ? 400 : width;
        isPortrait = (height > width);
        smallerDim = (height > width) ? width : height;

        smaller2dim =  isPortrait ? height /2 : width/2;


        scale = activity.getResources().getDisplayMetrics().density;

    }


    public static int dpToPix(float dips) {

        return Math.round(dips * scale);

    }

    public static String timeToString(int time) {
        int min = time / 60;
        int sec = time - min * 60;
        return String.format("%02d:%02d", min, sec);
    }

}
