package com.kovalenych.ranking;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kovalenych.PlatformResolver;
import com.kovalenych.R;

public class RankingActivity extends Activity {


    PullToRefreshListView mPullRefreshListView;

    Context context;
    private Dialog progressDialog;

    LinearLayout sendingRequestView;
    RankingManager rManager;
    private int screenWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        screenWidth = PlatformResolver.getWidth();
        setContentView(PlatformResolver.getRank());

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.ranking_list);
        rManager = new RankingManager(this, mPullRefreshListView);
        initFilterAndProgress();
    }


    private void initFilterAndProgress() {
        progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(true);

        sendingRequestView = new LinearLayout(this);
        sendingRequestView.setBackgroundColor(Color.BLACK);
        TextView sendText = new TextView(this);
        sendText.setGravity(Gravity.CENTER);
        sendingRequestView.addView(sendText, new LinearLayout.LayoutParams(220, 100));

        sendText.setText(getString(R.string.sendingRequest));
        progressDialog.setContentView(sendingRequestView);
        (findViewById(R.id.disc_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.aida-international.org/aspportal1/code/page.asp?ObjectID=39&CountryID=4&actID=3")));
            }
        });
        Spinner s = (Spinner) findViewById(R.id.discipline_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.disciplines, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rManager.chosenDisciplNumber = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rManager.asmFilter();
                rManager.getRecords();
            }
        });
    }


    @Override
    public void onBackPressed() {
        rManager.cancelTask();
        rManager.packSavedTables();
        if (rManager.recodsDBHelper != null)
            rManager.recodsDBHelper.close();

        if (rManager.requestsDBHelper != null)
            rManager.requestsDBHelper.close();

        RankingActivity.this.finish();
    }


    public void showProgressDialog(boolean show) {
        if (show)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }


    public boolean haveInternet() {
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
// POST /ranking.html? HTTP/1.1
//   Host: apnea.cz
//  User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0
//  Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
/* Accept-Language: ru-ru,ru;q=0.8,en-us;q=0.5,en;q=0.3
        Accept-Encoding: gzip, deflate
        Connection: keep-alive
        Referer: http://apnea.cz/ranking.html?STA+md:best
        Cookie: htscallerid=a85731576ce2b6351c85bde7391ef9ef; Vars=Lang{EN}
        Content-Type: multipart/form-data; BOUNDARY=---------------------------41184676334
        Content-Length: 3546
        -----------------------------41184676334
        Content-Disposition: form-data; name="Lang"

        EN
        -----------------------------41184676334
        Content-Disposition: form-data; name="sort"


        -----------------------------41184676334
        Content-Disposition: form-data; name="start"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkReport"

        REPORT
        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkScope"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkCompr"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkCompn"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkOrgNm"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkRelative"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkAllDisc"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkRgn2"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkFed2"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkDisc"

        STA
        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkGender"

        F
        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkMode"

        best
        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkRegn"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkClub"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkComprNm"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkCompnNm"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkYear"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkFede"


        -----------------------------41184676334
        Content-Disposition: form-data; name="rnkSrc"


        -----------------------------41184676334
        Content-Disposition: form-data; name="admin"


        -----------------------------41184676334
        Content-Disposition: form-data; name="pageID"


        -----------------------------41184676334
        Content-Disposition: form-data; name="pg"


        -----------------------------41184676334
        Content-Disposition: form-data; name="sid"


        -----------------------------41184676334
        Content-Disposition: form-data; name="lastSearch"


        -----------------------------41184676334
        Content-Disposition: form-data; name="cleanText"


        -----------------------------41184676334
        Content-Disposition: form-data; name="url"


        -----------------------------41184676334
        Content-Disposition: form-data; name="origLocation"


        -----------------------------41184676334
        Content-Disposition: form-data; name="origPageID"


        -----------------------------41184676334
        Content-Disposition: form-data; name="logOut"


        -----------------------------41184676334
        Content-Disposition: form-data; name="logInNow"


        -----------------------------41184676334
        Content-Disposition: form-data; name="mediaTp"


        -----------------------------41184676334
        Content-Disposition: form-data; name="langSel"


        -----------------------------41184676334
        Content-Disposition: form-data; name="catSel"


        -----------------------------41184676334
        Content-Disposition: form-data; name="pageSzt"


        -----------------------------41184676334--
*/
