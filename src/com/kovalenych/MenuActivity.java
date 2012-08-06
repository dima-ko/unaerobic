package com.kovalenych;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.*;
import com.kovalenych.ranking.RankingActivity;
import com.kovalenych.tables.TablesActivity;


public class MenuActivity extends Activity {

//    GoogleAnalyticsTracker tracker;


    /**
     * Called when the activity is first created.
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.resolvePlatform(this);
        int buttonDim = Utils.dpToPix(52);
        int buttonMarg = Utils.dpToPix(12);
        int smDim = (Utils.width < Utils.height) ? Utils.width : Utils.height;
        setContentView(R.layout.main);
        RelativeLayout parent = (RelativeLayout) findViewById(R.id.parent);

        Button button1 = new Button(this);
        button1.setBackgroundResource(R.drawable.qeust);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(buttonDim, buttonDim);
        params1.setMargins(buttonMarg, buttonMarg, smDim - buttonDim - buttonMarg, buttonMarg);
        params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        parent.addView(button1, params1);

        Button button2 = new Button(this);
        button2.setBackgroundResource(R.drawable.taable);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(buttonDim, buttonDim);
        params2.setMargins(buttonMarg, buttonMarg, smDim / 2 - buttonDim / 2, buttonMarg );
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        parent.addView(button2, params2);

        Button button3 = new Button(this);
        button3.setBackgroundResource(R.drawable.vidos);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(buttonDim, buttonDim);
        params3.setMargins(buttonMarg, buttonMarg, buttonMarg, buttonMarg);
        params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        parent.addView(button3, params3);

        Button button4 = new Button(this);
        button4.setBackgroundResource(R.drawable.book2);
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams(buttonDim, buttonDim);
        params4.setMargins(buttonMarg, buttonMarg, buttonMarg, smDim - buttonDim - buttonMarg);
        params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        parent.addView(button4, params4);

        Button button5 = new Button(this);
        button5.setBackgroundResource(R.drawable.infoblack);
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(buttonDim, buttonDim);
        params5.setMargins(buttonMarg, buttonMarg, buttonMarg , smDim / 2 - buttonDim / 2);
        params5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        parent.addView(button5, params5);

        Button button6 = new Button(this);
        button6.setBackgroundResource(R.drawable.heart_beat);
        RelativeLayout.LayoutParams params6 = new RelativeLayout.LayoutParams(buttonDim, buttonDim);

        int rad = (smDim - buttonDim) /2;
        params6.setMargins(buttonMarg, buttonMarg, rad, rad);
        params6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params6.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        parent.addView(button6, params6);

    }

//    private void startActivities() {
//        int act = 0;
//        Intent intent;
//        switch (act) {
//            case 0:
////                intent = new Intent(MenuActivity.this, InfoActivity.class);
//                break;
//            case 1:
//                Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.under_construction), Toast.LENGTH_SHORT).show();
//                break;
//            case 2:
//                intent = new Intent(MenuActivity.this, TablesActivity.class);
//                break;
//            case 3:
//                if (haveInternet())
//                    intent = new Intent(MenuActivity.this, NostraVideoActivity.class);
//                else
//                    Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.noConnect), Toast.LENGTH_SHORT).show();
//                break;
//            case 4:
////                if (haveInternet())
//                intent = new Intent(MenuActivity.this, RankingActivity.class);
////                else
////                    Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.noConnect), Toast.LENGTH_SHORT).show();
//                break;
//            case 5:
//                if (haveInternet())
//                    intent = new Intent(MenuActivity.this, ArticlesActivity.class);
//                else
//                    Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.noConnect), Toast.LENGTH_SHORT).show();
//                break;
//
//        }
//    }

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

}
