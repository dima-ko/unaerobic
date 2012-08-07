package com.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.kovalenych.Fonts;
import com.kovalenych.R;
import com.kovalenych.tables.CyclesActivity;
import com.kovalenych.tables.TableViewBinder;

import java.util.*;

public final class TablesFragment extends Fragment {

    Map<String, ?> mapa;                   //Table , file
    SharedPreferences _preferedTables;
    ArrayList<String> tableList;

    Dialog newDialog;
    Dialog infoDialog;
    Dialog delDialog;
    Button add_button, info_button, ok_button, del_button;
    EditText edit;
    int chosenTable;
    ListView lv;

    public static TablesFragment newInstance() {
        return new TablesFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _preferedTables = getActivity().getSharedPreferences("sharedTables", getActivity().MODE_PRIVATE);
        mapa = _preferedTables.getAll();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View tables = inflater.inflate(R.layout.tables, null);

        tableList = new ArrayList<String>();
        Set<String> tableSet = mapa.keySet();
        if (tableSet.size() == 0) {
            tableList.add("O2 Table");
            tableList.add("CO2 Table");
        } else
            tableList.addAll(tableSet);

        initDialogs();

        lv = (ListView) tables.findViewById(R.id.tables_list);
        lv.setTextFilterEnabled(true);
        lv.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.table_item, tableList));
        lv.setVisibility(View.VISIBLE);
//        invalidateList();

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
//
        add_button = (Button) tables.findViewById(R.id.add_table);
        info_button = (Button) tables.findViewById(R.id.info_button);

        setButtonListeners();

//        tracker = GoogleAnalyticsTracker.getInstance();
//
//        // Start the tracker in manual dispatch mode...
//        tracker.startNewSession("UA-28633429-1", this);
//        tracker.setAnonymizeIp(true);

        return tables;
    }

    private void invalidateList() {

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), createTablesList(), R.layout.table_item,
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



    void initDialogs() {
        delDialog = new Dialog(getActivity());
        delDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delDialog.setCancelable(true);
        delDialog.setContentView(R.layout.delete_dialog);

        newDialog = new Dialog(getActivity());
        newDialog.setTitle(getResources().getString(R.string.new_table));
        newDialog.setCancelable(true);
        newDialog.setContentView(R.layout.new_table_dialog);

        infoDialog = new Dialog(getActivity());
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setCancelable(true);
        infoDialog.setContentView(R.layout.info_dialog);

        edit = (EditText) newDialog.findViewById(R.id.new_table_edit);
        ok_button = (Button) newDialog.findViewById(R.id.new_table_ok);
        ok_button.setTypeface(Fonts.BELIGERENT);
        del_button = (Button) delDialog.findViewById(R.id.delete_button);

        ((TextView) infoDialog.findViewById(R.id.infot)).setTypeface(Fonts.BELIGERENT);
        ((TextView) infoDialog.findViewById(R.id.title)).setTypeface(Fonts.BELIGERENT);
    }


    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.info_button:
                    infoDialog.show();
                    break;
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
        info_button.setOnClickListener(buttonListener);
        del_button.setOnClickListener(buttonListener);
        ok_button.setOnClickListener(buttonListener);
    }

}
