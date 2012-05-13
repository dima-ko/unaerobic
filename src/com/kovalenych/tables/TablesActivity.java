package com.kovalenych.tables;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.kovalenych.Fonts;
import com.kovalenych.PlatformResolver;
import com.kovalenych.R;

import java.util.*;


public class TablesActivity extends Activity {

    ArrayList<String> tableList;

    Map<String, ?> mapa;                   //Table , file
    SharedPreferences _preferedTables;
    ListView lv;
    Dialog newDialog;
    Dialog infoDialog;
    Dialog delDialog;
    Activity ptr;
    Button add_button/*, info_button*/, ok_button, del_button;
    EditText edit;
    int chosenTable;
    Spinner spinner;
    private View menu;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ptr = this;


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        menu = inflater.inflate(PlatformResolver.getMenuLayout(), null);

        setContentView(menu);
        _preferedTables = getSharedPreferences("sharedTables", MODE_PRIVATE);
        mapa = _preferedTables.getAll();


        Fonts.setFonts(Typeface.createFromAsset(getAssets(),
                "fonts/belligerent.ttf"));

        tableList = new ArrayList<String>();
        Set<String> tableSet = mapa.keySet();
        if (tableSet.size() == 0) {
            tableList.add("O2 Table");
            tableList.add("CO2 Table");
        } else
            tableList.addAll(tableSet);

        initDialogs();

        edit = (EditText) newDialog.findViewById(R.id.new_table_edit);
        ok_button = (Button) newDialog.findViewById(R.id.new_table_ok);
        ok_button.setTypeface(Fonts.BELIGERENT);
        del_button = (Button) delDialog.findViewById(R.id.delete_button);


        lv = (ListView) findViewById(R.id.tables_list);
        lv.setTextFilterEnabled(true);
//        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.table_item, tableList));
//        lv.setVisibility(View.VISIBLE);
        invalidateList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                tracker.trackEvent("Clicks", "Button", "clicked", 7);
                Intent intent = new Intent(lv.getContext(), CyclesActivity.class);
                Bundle bun = new Bundle();
                bun.putString("name", tableList.get(position));
                intent.putExtras(bun);
                startActivity(intent);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("onLongClick", "zzz");
                chosenTable = i;
                delDialog.show();
                return false;
            }
        });

        add_button = (Button) findViewById(R.id.add_table);

//        info_button = (Button) findViewById(R.id.info);

        setButtonListeners();

//        tracker = GoogleAnalyticsTracker.getInstance();
//
//        // Start the tracker in manual dispatch mode...
//        tracker.startNewSession("UA-28633429-1", this);
//        tracker.setAnonymizeIp(true);
    }



    void initDialogs() {
        delDialog = new Dialog(ptr);
        delDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delDialog.setCancelable(true);
        delDialog.setContentView(R.layout.delete_dialog);

        newDialog = new Dialog(ptr);
        newDialog.setTitle(getResources().getString(R.string.new_table));
        newDialog.setCancelable(true);
        newDialog.setContentView(R.layout.new_table_dialog);

        infoDialog = new Dialog(ptr);
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setCancelable(true);
        infoDialog.setContentView(R.layout.info_dialog);
//        spinner = (Spinner) infoDialog.findViewById(R.id.info_spinner);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(
//                this, R.array.languages, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        ((TextView) infoDialog.findViewById(R.id.infot)).setTypeface(Fonts.BELIGERENT);
        ((TextView) infoDialog.findViewById(R.id.title)).setTypeface(Fonts.BELIGERENT);
    }

    private void invalidateList() {

        SimpleAdapter adapter = new SimpleAdapter(this, createTablesList(), PlatformResolver.getTableItemLayout(),
                new String[]{"text"},
                new int[]{R.id.table_name});

        adapter.setViewBinder(new TableViewBinder(Fonts.BELIGERENT));
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }

    private List<? extends Map<String, ?>> createTablesList() {

        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();

        for (int i = 0; i < tableList.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", tableList.get(i));
            items.add(map);
        }

        return items;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            SharedPreferences.Editor edito = _preferedTables.edit();
            edito.clear();
            for (String s : tableList)
                edito.putString(s, "");
            edito.commit();
        }

        return super.onKeyDown(keyCode, event);
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_table:
//                    tracker.trackPageView("/addTable");
                    newDialog.show();
                    break;
                case R.id.delete_button:
                    tableList.remove(chosenTable);
                    delDialog.dismiss();
                    invalidateList();
                    break;
                case R.id.new_table_ok:
                    String name = edit.getText().toString();
                    if (!name.equals("")) {
                        tableList.add(name);
                        invalidateList();
                    }
                    newDialog.dismiss();
                    break;
            }
        }
    };

    public void setButtonListeners() {

        add_button.setOnClickListener(buttonListener);
//        info_button.setOnClickListener(buttonListener);
        del_button.setOnClickListener(buttonListener);
        ok_button.setOnClickListener(buttonListener);
    }





    /**
     * @return boolean return true if the application can access the internet
     */

}
