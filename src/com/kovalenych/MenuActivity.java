package com.kovalenych;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import java.util.Locale;

public class MenuActivity extends Activity {

//    GoogleAnalyticsTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("language", Locale.getDefault().getLanguage());
        if (Locale.getDefault().getLanguage().equals("fr")) {

        }
        resolvePlatform();



        setContentView(R.layout.main_menu);
        findViewById(R.id.menu_art).setOnClickListener(activListener);
        findViewById(R.id.menu_heart).setOnClickListener(activListener);
        findViewById(R.id.menu_info).setOnClickListener(activListener);
        findViewById(R.id.menu_rank).setOnClickListener(activListener);
        findViewById(R.id.menu_tables).setOnClickListener(activListener);
        findViewById(R.id.menu_videos).setOnClickListener(activListener);

    }

    View.OnClickListener activListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;


            switch (view.getId()) {
                case R.id.menu_art:
                    intent = new Intent(MenuActivity.this, ArticlesActivity.class);
                    break;
                case R.id.menu_videos:
                    intent = new Intent(MenuActivity.this, NostraVideoActivity.class);
                    break;
                case R.id.menu_info:
                    intent = new Intent(MenuActivity.this, ArticlesActivity.class);
                    break;
                case R.id.menu_rank:
                    intent = new Intent(MenuActivity.this, RankingActivity.class);
                    break;
                case R.id.menu_tables:
                    intent = new Intent(MenuActivity.this, TablesActivity.class);
                    break;
                case R.id.menu_heart:
                    Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.under_construction), Toast.LENGTH_SHORT).show();
                    break;

            }
            if (intent != null)
                startActivity(intent); //TODO: troubles
        }
    };

    private boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to disable internet while roaming, just return false
            return true;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        tracker.dispatch();
//        tracker.stopSession();
    }

    private void resolvePlatform() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (metrics.heightPixels == 480)
            PlatformResolver.isHVGA = true;

        if (metrics.heightPixels == 400)
            PlatformResolver.isHVGA400 = true;

        if (Build.VERSION.SDK_INT < 5)
            PlatformResolver.isDONUT = true;
    }
}
