package com.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.kovalenych.R;
import com.kovalenych.tables.CyclesActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public final class TablesFragment extends Fragment {
    private static final String KEY_CONTENT = "TablesFragment:Content";

    Map<String, ?> mapa;                   //Table , file
    SharedPreferences _preferedTables;
    ArrayList<String> tableList;

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
        final ListView lv;
        View tables = inflater.inflate(R.layout.tables, null);

        tableList = new ArrayList<String>();
        Set<String> tableSet = mapa.keySet();
        if (tableSet.size() == 0) {
            tableList.add("O2 Table");
            tableList.add("CO2 Table");
        } else
            tableList.addAll(tableSet);

//        initDialogs();

//        edit = (EditText) newDialog.findViewById(R.id.new_table_edit);
//        ok_button = (Button) newDialog.findViewById(R.id.new_table_ok);
//        ok_button.setTypeface(Fonts.BELIGERENT);
//        del_button = (Button) delDialog.findViewById(R.id.delete_button);


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

//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.d("onLongClick", "zzz");
//                chosenTable = i;
//                delDialog.show();
//                return false;
//            }
//        });
//
//        add_button = (Button) findViewById(R.id.add_table);
//
////        info_button = (Button) findViewById(R.id.info);
//
//        setButtonListeners();

//        tracker = GoogleAnalyticsTracker.getInstance();
//
//        // Start the tracker in manual dispatch mode...
//        tracker.startNewSession("UA-28633429-1", this);
//        tracker.setAnonymizeIp(true);

        return tables;
    }

}
