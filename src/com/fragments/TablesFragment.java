package com.fragments;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.kovalenych.Const;
import com.kovalenych.R;
import com.kovalenych.Utils;
import com.kovalenych.tables.ClockService;
import com.kovalenych.tables.CyclesActivity;
import com.kovalenych.tables.TablesArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public final class TablesFragment extends Fragment implements Const {

    Map<String, ?> mapa;                   //Table , file
    SharedPreferences _preferedTables;
    ArrayList<String> tableList;

    Dialog newDialog;
    Dialog infoDialog;
    Dialog delDialog;
    Button ok_button, del_button;
    RelativeLayout add_button, info_button;
    EditText edit;
    int chosenTable;
    ListView lv;
    private RelativeLayout stopButton;

    public static int posOfCurTable = -1;

    public static TablesFragment newInstance() {
        return new TablesFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _preferedTables = getActivity().getSharedPreferences("sharedTables", Context.MODE_PRIVATE);
        mapa = _preferedTables.getAll();

        tableList = new ArrayList<String>();
        Set<String> tableSet = mapa.keySet();
        if (tableSet.size() == 0) {
            tableList.add("O2 Table");
            tableList.add("CO2 Table");
        } else
            tableList.addAll(tableSet);
        Collections.sort(tableList);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View tables = inflater.inflate(R.layout.tables, null);


        initDialogs();

        stopButton = (RelativeLayout) tables.findViewById(R.id.stop_button_tables);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().stopService(new Intent(getActivity(), ClockService.class));
                stopButton.setVisibility(View.GONE);
                posOfCurTable = -1;
                invalidateList();
            }
        });
        if (Utils.isMyServiceRunning(getActivity()))
            stopButton.setVisibility(View.VISIBLE);
        else
            stopButton.setVisibility(View.GONE);

        lv = (ListView) tables.findViewById(R.id.tables_list);
        lv.setVisibility(View.VISIBLE);
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
//
        add_button = (RelativeLayout) tables.findViewById(R.id.add_table);
        info_button = (RelativeLayout) tables.findViewById(R.id.info_button);

        setButtonListeners();

//        tracker = GoogleAnalyticsTracker.getInstance();
//
//        // Start the tracker in manual dispatch mode...
//        tracker.startNewSession("UA-28633429-1", this);
//        tracker.setAnonymizeIp(true);

//
        return tables;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utils.isMyServiceRunning(getActivity())) {
            stopButton.setVisibility(View.VISIBLE);
            subscribeToService();

        } else {
            stopButton.setVisibility(View.GONE);
            posOfCurTable = -1;
            invalidateList();
        }
    }

    private void invalidateList() {
        TablesArrayAdapter adapter = new TablesArrayAdapter(getActivity(), tableList);
        boolean showTotalTime = getActivity().getSharedPreferences("clockPrefs", Context.MODE_PRIVATE).getBoolean("showAddInfo", false);
        adapter.setShowTotalTIme(showTotalTime);
        lv.setAdapter(adapter);
        lv.setVisibility(View.VISIBLE);
    }


    void initDialogs() {
        delDialog = new Dialog(getActivity());
        delDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delDialog.setCancelable(true);
        delDialog.setContentView(R.layout.delete_dialog);

        newDialog = new Dialog(getActivity());
        newDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newDialog.setTitle(getResources().getString(R.string.new_table));
        newDialog.setCancelable(true);
        newDialog.setContentView(R.layout.new_table_dialog);

        infoDialog = new Dialog(getActivity());
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setCancelable(true);
        infoDialog.setContentView(R.layout.info_dialog);

        infoDialog.findViewById(R.id.stars).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String APP_PNAME = "com.kovalenych";
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
            }
        });

        infoDialog.findViewById(R.id.you_tubeman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=M-l043CyPOs")));
            }
        });

        infoDialog.findViewById(R.id.mailto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"insomniac.robot@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Unaerobic");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

                /* Send it off to the Activity-Chooser */
                getActivity().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        infoDialog.findViewById(R.id.translated_by).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.translated_email)});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Unaerobic");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

                /* Send it off to the Activity-Chooser */
                getActivity().startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });

        edit = (EditText) newDialog.findViewById(R.id.new_table_edit);
        ok_button = (Button) newDialog.findViewById(R.id.new_table_ok);
        del_button = (Button) delDialog.findViewById(R.id.delete_button);
    }


    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.info_button:
                    infoDialog.show();
                    break;
                case R.id.add_table:
                    newDialog.show();
                    break;
                case R.id.delete_button:
                    ContextWrapper cw = new ContextWrapper(getActivity());
                    File tablesDir = cw.getDir("tables", Context.MODE_PRIVATE);
                    File file = new File(tablesDir.getAbsolutePath() + "/" + tableList.get(chosenTable));
                    boolean deleted = file.delete();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = _preferedTables.edit();
        editor.clear();
        for (String s : tableList)
            editor.putString(s, "");
        editor.commit();
    }


    private void subscribeToService() {
        PendingIntent pi;
        Intent intent;

        // Создаем PendingIntent для Task1
        pi = getActivity().createPendingResult(1, new Intent(), 0);
        // Создаем Intent для вызова сервиса, кладем туда параметр времени
        // и созданный PendingIntent
        intent = new Intent(getActivity(), ClockService.class)
                .putExtra(FLAG, FLAG_SUBSCRIBE_TABLE)
                .putExtra(PARAM_PINTENT, pi);
        // стартуем сервис
        getActivity().startService(intent);
    }


    public void onUpdateCurTable(String curTableName) {
        for (String table : tableList) {
            if (table.equals(curTableName)) {
                posOfCurTable = tableList.indexOf(table);
                break;
            }
        }
        invalidateList();
    }


    public void onTableFinish() {

        posOfCurTable = -1;
        invalidateList();
        stopButton.setVisibility(View.GONE);

    }
}
