package com.kovalenych.ranking;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kovalenych.PlatformResolver;
import com.kovalenych.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RankingActivity extends Activity {


    PullToRefreshListView mPullRefreshListView;

    Context context;
    private Dialog filterDialog;
    private Dialog progressDialog;
    protected URL url;
    protected HttpURLConnection conn;


    LinearLayout sendingRequestView;
    RankingManager rManager;
    public boolean exitFlag = false; //if true goes to filterdialog on backpressed
    private AsyncTask<Void, Void, String[]> sendTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        fillList();
        setContentView(R.layout.ranking);

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.ranking_list);
        rManager = new RankingManager(this, mPullRefreshListView);
        sendTask = new SendTask(true).execute();

    }


    private void fillList() {

        filterDialog = new Dialog(context);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCancelable(true);
        filterDialog.setContentView(PlatformResolver.getFilterDialogLayout());

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
//        showProgressDialog.show();

        filterDialog.setOnCancelListener(onCancelListener);
        progressDialog.setOnCancelListener(onCancelListener);


        initDialog();
    }

    Dialog.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            sendTask.cancel(true);
            if (exitFlag) {
                filterDialog.show();
                exitFlag = false;
            } else {
                RankingActivity.this.finish();
            }
        }
    };

    @Override
    public void onBackPressed() {
        sendTask.cancel(true);
        filterDialog.show();
        exitFlag = false;
    }

    private void initDialog() {
//        http://www.aida-international.org/aspportal1/code/page.asp?ObjectID=39&CountryID=4&actID=3
        (filterDialog.findViewById(R.id.disc_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.aida-international.org/aspportal1/code/page.asp?ObjectID=39&CountryID=4&actID=3")));
            }
        });
        Spinner s = (Spinner) filterDialog.findViewById(R.id.discipline_spinner);
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

        filterDialog.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.dismiss();
                exitFlag = true;
                rManager.asmFilter();
                rManager.getRecords();
//                showProgressDialog(true);
//                sendTask = new SendTask(false).execute();
            }
        });

    }

    public void showProgressDialog(boolean show) {
        if (show)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }

    public void showFilterDialog(boolean show) {
        if (show)
            filterDialog.show();
        else
            filterDialog.dismiss();
    }

    public void getFromApneaCZ() throws IOException {
        url = new URL("http://apnea.cz/ranking.html?");
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        rManager.setTypicalRequestProps(conn);
        conn.connect();
        String headerName = null;
        System.out.println("zzzz" + conn.getResponseMessage());
        for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
            if (headerName.equals("Set-Cookie")) {

                String stringwithcool = conn.getHeaderField(i);
                String cookie = (stringwithcool.substring(0, stringwithcool.indexOf(";")));
                System.out.println("zzzzcookie" + cookie);
                rManager.setCookie(cookie);
            }
        }

    }

    private class SendTask extends AsyncTask<Void, Void, String[]> {

        private boolean get;

        public SendTask(boolean isGet) {
            get = isGet;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog(true);
        }

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
//                if (get)
                getFromApneaCZ();
//                else
//                    rManager.sendPost();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
//            if (get)
            filterDialog.show();
//            else
//                rManager.invalidateList();
            showProgressDialog(false);
        }
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
