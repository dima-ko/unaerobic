package com.kovalenych;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;

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

public class RankingActivity extends Activity {

    ListView lv;
    ArrayList<Record> recordsList;
    Context context;
    private Dialog filterDialog;
    protected URL url;
    protected HttpURLConnection conn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordsList = new ArrayList<Record>();
        context = this;
        fillList();
        // http://www.aidainternational.org/freediving/history
        setContentView(R.layout.ranking);
//        setContentView(R.layout.ranking);

        lv = (ListView) findViewById(R.id.ranking_list);

        invalidateList();
    }


    private void fillList() {

        filterDialog = new Dialog(context);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCancelable(true);
        filterDialog.setContentView(PlatformResolver.getFilterDialogLayout());
        initDialog();
        filterDialog.show();


        recordsList.add(new Record("Goran", "Colak", "273", "CR", "101"));
    }

    private void initDialog() {
        Spinner s = (Spinner) filterDialog.findViewById(R.id.discipline_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.disciplines, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

    }

    private void invalidateList() {

        SimpleAdapter adapter = new SimpleAdapter(this, createCyclesList(), PlatformResolver.getRecordItemLayout(),
                new String[]{"flag", "name", "result"},
                new int[]{R.id.ranking_country, R.id.ranking_name_surname, R.id.ranking_result});
        adapter.setViewBinder(new ArticleViewBinder());
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }

    private List<? extends Map<String, ?>> createCyclesList() {

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

        for (int i = 0; i < recordsList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("flag", recordsList.get(i).getCountry());
            map.put("name", recordsList.get(i).getName() + recordsList.get(i).getSurname());
            map.put("result", recordsList.get(i).getResult());
            items.add(map);
        }

        return items;
    }

    public void getFromApneaCZ() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 100);

    }

    protected void start() throws IOException {
        url = new URL("http://apnea.cz/ranking.html?");
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        setTypicalRequestProps();
//        conn.setRequestProperty("Referer", kyivstar_referer_uri);
//        sendInitGet();
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    }

    protected void setTypicalRequestProps() {
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(true);

        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux i686; rv:2.0) Gecko/20100101 Firefox/4.0");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        conn.setRequestProperty("Keep-Alive", "300");
        conn.setRequestProperty("Connection", "keep-alive");


    }

//    POST /ranking.html? HTTP/1.1
//    Host: apnea.cz
//    User-Agent: Mozilla/5.0 (Ubuntu; X11; Linux i686; rv:9.0.1) Gecko/20100101 Firefox/9.0.1
//    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//    Accept-Language: en-us,en;q=0.5
//    Accept-Encoding: gzip, deflate
//    Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
//    Connection: keep-alive
//    Referer: http://apnea.cz/ranking.html?DYN+md:best
//    Cookie: htscallerid=e4f7b64c05ecfc836c12109d584c52e8
//    Content-Type: multipart/form-data; boundary=---------------------------457964961466790513485958903
//    Content-Length: 4158
//    -----------------------------457964961466790513485958903
//    Content-Disposition: form-data; name="Lang"

    protected void sendPost(String content) throws IOException {
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//        Log.d(LOG_TAG + "zzzzzzlength", "" + content.length());
        System.out.println(content);
        out.writeBytes(content);
        out.flush();
        out.close();

        System.out.println("zzPost" + conn.getResponseCode());
        System.out.println("zzPost" + conn.getResponseMessage());
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        int i = 0;
//        while ((line = in.readLine()) != null) {
//            if (haveSendConfirmation && line.contains("прийнято"))
//                Toast.makeText(this, R.string.succesfulySend, 2000).show();
//            Log.d("zzzPostResponce", line);
//            i++;
//        }
        in.close();
//        if (!haveSendConfirmation)
//            Toast.makeText(this, R.string.succesfulySend, 2000).show();

    }


}
