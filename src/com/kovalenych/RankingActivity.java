package com.kovalenych;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

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
    private String cookie;
    private String postMessage;
    private ArrayList<String> htmlList;

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
        getFromApneaCZ();
//        invalidateList();


    }


    private void fillList() {

        filterDialog = new Dialog(context);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCancelable(true);
        filterDialog.setContentView(PlatformResolver.getFilterDialogLayout());
        initDialog();
//        filterDialog.show();


//        recordsList.add(new Record("Goran", "Colak", "273", "CR", "101"));
    }

    private void initDialog() {
        Spinner s = (Spinner) filterDialog.findViewById(R.id.discipline_spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.disciplines, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        filterDialog.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillPost();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sendPost(postMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 50);

                filterDialog.dismiss();
            }
        });

    }

    private void fillPost() {
        postMessage = getString(R.string.post);
    }

    private void invalidateList() {

        SimpleAdapter adapter = new SimpleAdapter(this, createCyclesList(), PlatformResolver.getRecordItemLayout(),
                new String[]{"place", "flag", "name", "result"},
                new int[]{R.id.ranking_place,R.id.ranking_country, R.id.ranking_name_surname, R.id.ranking_result});
        adapter.setViewBinder(new ArticleViewBinder());
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }

    private List<? extends Map<String, ?>> createCyclesList() {

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

        for (int i = 0; i < recordsList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("place", i);
            map.put("flag", recordsList.get(i).getCountry());
            map.put("name", recordsList.get(i).getName() );
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
        sendInitGet();
    }


    protected void sendInitGet() throws IOException {

        conn.connect();
        String headerName = null;
        System.out.println("zzzz" + conn.getResponseMessage());
        for (int i = 1; (headerName = conn.getHeaderFieldKey(i)) != null; i++) {
            if (headerName.equals("Set-Cookie")) {

                String stringwithcool = conn.getHeaderField(i);
                cookie = (stringwithcool.substring(0, stringwithcool.indexOf(";")));
                System.out.println("zzzzcookie" + cookie);
            }
        }
        filterDialog.show();
    }

    String boundary = "boundary=---------------------------1397366148113562428080587968";

    protected void setTypicalRequestProps() {
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setAllowUserInteraction(true);

        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux i686; rv:2.0) Gecko/20100101 Firefox/4.0");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.setRequestProperty("Accept-Language", "en-us,en;q=0.5");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("Connection", "keep-alive");


    }

    protected void setTypicalRequestPropsForPost() {
        conn.setRequestProperty("Referer", "http://apnea.cz/ranking.html?STA");
        conn.setRequestProperty("Cookie", cookie);
        conn.setRequestProperty("Content-Type", "multipart/form-data; " + boundary);
        conn.setRequestProperty("Content-Length", Integer.toString(postMessage.length()));
//        Referer: http://apnea.cz/ranking.html?STA
//        Cookie: htscallerid=12a14ac343c799cf87cadbd3778b45d1

    }

    protected void sendPost(String content) throws IOException {
        htmlList = new ArrayList<String>();

        url = new URL("http://apnea.cz/ranking.html?DYN");
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        setTypicalRequestProps();
        setTypicalRequestPropsForPost();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//        System.out.println(content);
        out.writeBytes(content);
        out.flush();
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        int i = 0;
        while ((line = in.readLine()) != null) {
//            Log.d("zzzPostResponce", line);
            if (line.contains("rankRow")) {
                    htmlList.add(line);
            }
            i++;
        }
        in.close();

        for (int j = 0; j < htmlList.size(); j++)
            recordsList.add(extractRecoedFromString(htmlList.get(j)));

        invalidateList();
    }

    private Record extractRecoedFromString(String fullString) {

        int nameStartIndex = fullString.indexOf("<td>",26)+4;
        int nameEndIndex = fullString.indexOf("</td>",nameStartIndex);
        int surnameStartIndex = fullString.indexOf("\">",nameEndIndex+50)+2;
        int surnameEndIndex = fullString.indexOf("</a>",surnameStartIndex);
        String name = fullString.substring(nameStartIndex,nameEndIndex) + " " + fullString.substring(surnameStartIndex, surnameEndIndex) ;

        int altFlagStartIndex = fullString.indexOf("flag")-13;
        String country =  fullString.substring(altFlagStartIndex,altFlagStartIndex+3);


        int resultStartIndex = fullString.indexOf("<b>")+3;
        int resultEndIndex = fullString.indexOf("</b>",resultStartIndex);
        String res =  fullString.substring(resultStartIndex,resultEndIndex);

//        Log.d("zzzname","name " + name +  "  res   " + res + "country" + country);

        return new Record(name, res, country);

    }


}
