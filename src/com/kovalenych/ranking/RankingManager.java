package com.kovalenych.ranking;


import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kovalenych.ArticleViewBinder;
import com.kovalenych.PlatformResolver;
import com.kovalenych.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingManager {
    private Context context;
    private PullToRefreshListView mPullToRefreshListView;
    ListView lv;
    ArrayList<Record> recordsList;
    private String cookie;
    private String postMessage;
    private ArrayList<String> htmlList;
    public int chosenDisciplNumber = 0;
    protected URL url;
    protected HttpURLConnection conn;
    private static final String BOUNDARY = "boundary=---------------------------1397366148113562428080587968";
    private String[] mDisciplinesArray;

    public RankingManager(Context context, PullToRefreshListView pullToRefreshListView) {
        this.context = context;
        this.mPullToRefreshListView = pullToRefreshListView;
        recordsList = new ArrayList<Record>();
        mDisciplinesArray = this.context.getResources().getStringArray(R.array.disciplines);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshListView.setLastUpdatedLabel("last update: " + DateUtils.formatDateTime(RankingManager.this.context.getApplicationContext(),
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL));

                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        lv = mPullToRefreshListView.getRefreshableView();

        //        DBHelper dbHelper = new DBHelper(RankingActivity.this,"records_STA_all_all_all.db");
//        SQLiteDatabase db  = dbHelper.getWritableDatabase();
//
//        db.close();
//        dbHelper.close();
    }

    private List<? extends Map<String, ?>> createCyclesList() {

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

        Map<String, Object> titleMap = new HashMap<String, Object>();
        titleMap.put("place", "");
        titleMap.put("flag", "from");
        titleMap.put("name", "who");
        titleMap.put("result", "result");
        items.add(titleMap);

        for (int i = 0; i < recordsList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("place", i + 1);
            map.put("flag", recordsList.get(i).getCountry());
            map.put("name", recordsList.get(i).getName());
            map.put("result", recordsList.get(i).getResult());
            items.add(map);
        }

        return items;
    }


    public void invalidateList() {

        SimpleAdapter adapter = new SimpleAdapter(context, createCyclesList(), PlatformResolver.getRecordItemLayout(),
                new String[]{"place", "flag", "name", "result"},
                new int[]{R.id.ranking_place, R.id.ranking_country, R.id.ranking_name_surname, R.id.ranking_result});
        adapter.setViewBinder(new ArticleViewBinder());
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }

    public void setTypicalRequestProps(HttpURLConnection conn) {
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(true);

        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux i686; rv:2.0) Gecko/20100101 Firefox/4.0");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("Connection", "keep-alive");


    }

    public void setTypicalRequestPropsForPost(HttpURLConnection conn) {
        conn.setRequestProperty("Referer", "http://apnea.cz/ranking.html?STA");
        conn.setRequestProperty("Cookie", cookie + "; Vars=Lang{EN}");
        conn.setRequestProperty("Content-Type", "multipart/form-data; " + BOUNDARY);
        conn.setRequestProperty("Content-Length", Integer.toString(postMessage.length()));

    }


    public void sendPost() throws IOException {
        Log.d("zzzzzzzzzzzzzzpost","asd");
        fillPost();
        htmlList = new ArrayList<String>();

        url = new URL("http://apnea.cz/ranking.html?" + mDisciplinesArray[chosenDisciplNumber]);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        setTypicalRequestProps(conn);
        setTypicalRequestPropsForPost(conn);
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(postMessage);
        out.flush();
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        int i = 0;
        while ((line = in.readLine()) != null) {
            if (line.contains("rankRow")) {
                htmlList.add(line);
            }
            i++;
        }
        in.close();
        recordsList.clear();
        Log.d("parsing", "start " + System.currentTimeMillis());
        for (int j = 0; j < htmlList.size(); j++)
            recordsList.add(extractRecoedFromString(htmlList.get(j)));
        Log.d("parsing", "end " + System.currentTimeMillis());


    }

    private Record extractRecoedFromString(String fullString) {

        int nameStartIndex = fullString.indexOf("<td>", 26) + 4;
        int nameEndIndex = fullString.indexOf("</td>", nameStartIndex);
        int surnameStartIndex = fullString.indexOf("\">", nameEndIndex + 50) + 2;
        int surnameEndIndex = fullString.indexOf("</a>", surnameStartIndex);
        String name = fullString.substring(nameStartIndex, nameEndIndex) + " " + fullString.substring(surnameStartIndex, surnameEndIndex);

        int altFlagStartIndex = fullString.indexOf("flag") - 13;
        String country = fullString.substring(altFlagStartIndex, altFlagStartIndex + 2);


        int resultStartIndex = fullString.indexOf("<b>") + 3;
        int resultEndIndex = fullString.indexOf("</b>", resultStartIndex);
        String res = fullString.substring(resultStartIndex, resultEndIndex);

//        Log.d("zzzname","name " + name +  "  res   " + res + "country" + country);

        return new Record(name, res, country);

    }

    private void fillPost() {
        postMessage = context.getString(R.string.post);
    }


    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                sendPost();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {

            invalidateList();
            // Call onRefreshComplete when the list has been refreshed.
            mPullToRefreshListView.onRefreshComplete();
            Log.d("zzzzz", "refersh");
            super.onPostExecute(result);
        }
    }


}
