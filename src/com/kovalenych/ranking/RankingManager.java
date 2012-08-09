package com.kovalenych.ranking;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.fragments.RankingFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kovalenych.MenuActivity;
import com.kovalenych.media.ArticleViewBinder;
import com.kovalenych.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RankingManager {
    private Context context;
    private RankingFragment parent;
    private PullToRefreshListView mPullToRefreshListView;
    ListView lv;
    ArrayList<Record> recordsList;
    private String cookie;
    private String postMessage;
    private String filter;
    private ArrayList<String> htmlList;
    public int chosenDisciplNumber = 0;
    protected URL url;
    protected HttpURLConnection conn;
    private static final String BOUNDARY = "boundary=---------------------------1397366148113562428080587968";
    private String[] mDisciplinesArray;
    Map<String, String> savedTables = new HashMap<String, String>();
    DateFormat df = new SimpleDateFormat("dd MMM");
    private AsyncTask<Void, Void, String[]> getDataTask;


    public RankingManager(Context Context, final RankingFragment parent, PullToRefreshListView pullToRefreshListView) {
        this.context = Context;
        this.parent = parent;
        this.mPullToRefreshListView = pullToRefreshListView;
        recordsList = new ArrayList<Record>();


        mDisciplinesArray = this.context.getResources().getStringArray(R.array.disciplines);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mPullToRefreshListView.setLastUpdatedLabel("last update: " + DateUtils.formatDateTime(RankingManager.this.context.getApplicationContext(),
//                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
//                        | DateUtils.FORMAT_ABBREV_ALL));
                // Do work to refresh the list here.
                if (((MenuActivity) parent.getActivity()).haveInternet())
                    getDataTask = new GetDataTask(true).execute();
                else {
                    Toast.makeText(context, context.getString(R.string.noConnect), Toast.LENGTH_SHORT).show();
                    mPullToRefreshListView.onRefreshComplete();
                }
            }
        });
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        lv = mPullToRefreshListView.getRefreshableView();
        unpackSavedTables();
    }


    private List<? extends Map<String, ?>> createCyclesList() {

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

        Map<String, Object> titleMap = new HashMap<String, Object>();
        titleMap.put("place", "");
        titleMap.put("flag", "from");
        titleMap.put("name", "who");                                     //TODO: localize
        titleMap.put("result", "result");
        items.add(titleMap);

        for (int i = 0; i < recordsList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("place", i + 1);                              //TODO: more info in landscape mode
            map.put("flag", recordsList.get(i).getCountry());
            map.put("name", recordsList.get(i).getName());
            map.put("result", recordsList.get(i).getResult());
            items.add(map);
        }

        return items;
    }

    public void invalidateList() {

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
        long startParse = System.currentTimeMillis();
        for (int j = 0; j < htmlList.size(); j++)
            recordsList.add(extractRecoedFromString(htmlList.get(j)));
        Log.d("parsing", "time " + (System.currentTimeMillis() - startParse));
        saveToDB();

        savedTables.put(filter, df.format(new Date()));

    }

    DBHelper recodsDBHelper;
    DBHelper requestsDBHelper;

    public void asmFilter() {
        filter = "records_" + mDisciplinesArray[chosenDisciplNumber] + "_" + "all" + "_" + "all" + "_" + "all" + "_" + "all" + "_" + "all";
    }

    public void packSavedTables() {
        requestsDBHelper = new DBHelper(context, DBHelper.REQUESTS_DB);
        SQLiteDatabase db = requestsDBHelper.getWritableDatabase();
        db.execSQL("DROP TABLE " + DBHelper.REQUESTS_TABLE);
        db.execSQL(requestsDBHelper.createNewReqTable());

        Iterator<String> iter = savedTables.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.C_TABLENAME, key);
            cv.put(DBHelper.C_LASTUPD, savedTables.get(key));
            db.insert(DBHelper.REQUESTS_TABLE, null, cv);
        }
        db.close();
        requestsDBHelper.close();
    }

    private void unpackSavedTables() {
        requestsDBHelper = new DBHelper(context, DBHelper.REQUESTS_DB);
        SQLiteDatabase db = requestsDBHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.REQUESTS_TABLE, new String[]{DBHelper.C_ID, DBHelper.C_TABLENAME, DBHelper.C_LASTUPD},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            savedTables.put(cursor.getString(cursor.getColumnIndex(DBHelper.C_TABLENAME)), cursor.getString(cursor.getColumnIndex(DBHelper.C_LASTUPD)));
        }
        db.close();
        requestsDBHelper.close();

    }

    private void saveToDB() {
        recodsDBHelper = new DBHelper(context, DBHelper.RECORDS_DB);
        SQLiteDatabase db = recodsDBHelper.getWritableDatabase();
        db.delete(DBHelper.RECORDS_TABLE, DBHelper.C_FILTER + "=?", new String[]{filter});

        for (int i = 0; i < recordsList.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.C_NAME, recordsList.get(i).getName());
            cv.put(DBHelper.C_COUNTRY, recordsList.get(i).getCountry());
            cv.put(DBHelper.C_RESULT, recordsList.get(i).getResult());
            cv.put(DBHelper.C_FILTER, filter);
            db.insert(DBHelper.RECORDS_TABLE, null, cv);
        }

        db.close();

        recodsDBHelper.close();
    }

    private void readFromDB() {
        recordsList.clear();
        recodsDBHelper = new DBHelper(context, DBHelper.RECORDS_DB);
        SQLiteDatabase db = recodsDBHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.RECORDS_TABLE, new String[]{DBHelper.C_ID, DBHelper.C_NAME, DBHelper.C_COUNTRY, DBHelper.C_RESULT, DBHelper.C_FILTER},
                DBHelper.C_FILTER + " like " + "'%"
                        + filter + "%'", null, null, null, null);
        int nameColumn = cursor.getColumnIndex(DBHelper.C_NAME);
        int resultColumn = cursor.getColumnIndex(DBHelper.C_RESULT);
        int countryColumn = cursor.getColumnIndex(DBHelper.C_COUNTRY);

        while (cursor.moveToNext()) {
            recordsList.add(new Record(cursor.getString(nameColumn), cursor.getString(resultColumn), cursor.getString(countryColumn)));
        }
        db.close();
        recodsDBHelper.close();
    }

    public boolean isTableExists(String tableName) {
        return savedTables.containsKey(tableName);

    }

    public void closeDBHelpers() {
        if (recodsDBHelper != null)
            recodsDBHelper.close();

        if (requestsDBHelper != null)
            requestsDBHelper.close();
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
        return new Record(name, res, country);

    }

    private void fillPost() {
        postMessage = context.getString(R.string.post);
    }

    public void getRecords() {
        if (isTableExists(filter)) {
            readFromDB();
            parent.showFilter(false);
            invalidateList();
            refreshDateLabel();
        } else {
            if (((MenuActivity) parent.getActivity()).haveInternet())
                getDataTask = new GetDataTask(false).execute();
            else
                Toast.makeText(context, context.getString(R.string.noConnect), Toast.LENGTH_SHORT).show();
        }

    }

    public void cancelTask() {
        if (getDataTask != null)
            getDataTask.cancel(true);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {


        private boolean onPull;

        public GetDataTask(boolean onPull) {
            this.onPull = onPull;
        }

        @Override
        protected void onPreExecute() {
            if (!onPull)
                parent.showProgressDialog(true);
        }

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
            refreshDateLabel();

            if (onPull)
                mPullToRefreshListView.onRefreshComplete();
            else {
                parent.showProgressDialog(false);
                parent.showFilter(false);
            }
            // Call onRefreshComplete when the list has been refreshed.

            Log.d("zzzzz", "refersh");
        }
    }

    private void refreshDateLabel() {
        String nowTime = df.format(new Date());
        if (nowTime.equals(savedTables.get(filter)))
            mPullToRefreshListView.setLastUpdatedLabel(context.getString(R.string.updated) + context.getString(R.string.today));
        else
            mPullToRefreshListView.setLastUpdatedLabel(context.getString(R.string.updated) + savedTables.get(filter));
    }


}
