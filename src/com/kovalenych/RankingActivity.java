package com.kovalenych;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
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
    private Dialog sendingRequestDialog;
    protected URL url;
    protected HttpURLConnection conn;
    private String cookie;
    private String postMessage;
    private ArrayList<String> htmlList;
    private int chosenDisciplNumber = 0;
    private String[] mDisciplinesArray;

    LinearLayout sendingRequestView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordsList = new ArrayList<Record>();
        context = this;
        mDisciplinesArray = getResources().getStringArray(R.array.disciplines);
        fillList();
        setContentView(R.layout.ranking);

        lv = (ListView) findViewById(R.id.ranking_list);
        getFromApneaCZ();
//        DBHelper dbHelper = new DBHelper(RankingActivity.this,"records_STA_all_all_all.db");
//        SQLiteDatabase db  = dbHelper.getWritableDatabase();
//
//        db.close();
//        dbHelper.close();
    }


    private void fillList() {

        filterDialog = new Dialog(context);
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCancelable(true);
        filterDialog.setContentView(PlatformResolver.getFilterDialogLayout());

        sendingRequestDialog= new Dialog(context);
        sendingRequestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sendingRequestDialog.setCancelable(true);

        sendingRequestView = new LinearLayout(this);
        sendingRequestView.setBackgroundColor(Color.BLACK);
        TextView sendText = new TextView(this);
        sendText.setGravity(Gravity.CENTER);
        sendingRequestView.addView(sendText,new LinearLayout.LayoutParams(220,100));

        sendText.setText(getString(R.string.sendingRequest));
        sendingRequestDialog.setContentView(sendingRequestView);
        sendingRequestDialog.show();

        filterDialog.setOnCancelListener(onCancelListener);
        sendingRequestDialog.setOnCancelListener(onCancelListener);


        initDialog();
    }

    Dialog.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
              RankingActivity.this.finish();
//            startActivity(new Intent(RankingActivity.this,MenuActivity.class));
        }
    };

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
                chosenDisciplNumber = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menushka, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        filterDialog.show();
        return true;
    }

    private void fillPost() {
        postMessage = getString(R.string.post);
    }

    private void invalidateList() {

        SimpleAdapter adapter = new SimpleAdapter(this, createCyclesList(), PlatformResolver.getRecordItemLayout(),
                new String[]{"place", "flag", "name", "result"},
                new int[]{R.id.ranking_place, R.id.ranking_country, R.id.ranking_name_surname, R.id.ranking_result});
        adapter.setViewBinder(new ArticleViewBinder());
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }

    private List<? extends Map<String, ?>> createCyclesList() {

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

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
        conn.setRequestProperty("Cookie", cookie + "; Vars=Lang{EN}");
        conn.setRequestProperty("Content-Type", "multipart/form-data; " + boundary);
        conn.setRequestProperty("Content-Length", Integer.toString(postMessage.length()));

    }

    protected void sendPost(String content) throws IOException {

        sendingRequestDialog.show();

        htmlList = new ArrayList<String>();

        url = new URL("http://apnea.cz/ranking.html?" + mDisciplinesArray[chosenDisciplNumber]);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        setTypicalRequestProps();
        setTypicalRequestPropsForPost();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.writeBytes(content);
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
        Log.d("parsing","start " + System.currentTimeMillis());
        for (int j = 0; j < htmlList.size(); j++)
            recordsList.add(extractRecoedFromString(htmlList.get(j)));
        Log.d("parsing","end " + System.currentTimeMillis());
        invalidateList();
        sendingRequestDialog.dismiss();
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
        Content-Type: multipart/form-data; boundary=---------------------------41184676334
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