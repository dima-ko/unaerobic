package com.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.kovalenych.R;
import com.kovalenych.tables.CyclesActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public final class MediaFragment extends Fragment {
    private static final String KEY_CONTENT = "TablesFragment:Content";

    Map<String, ?> mapa;                   //Table , file
    SharedPreferences _preferedTables;
    ArrayList<String> tableList;

    public static MediaFragment newInstance() {

        return  new MediaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View tables = inflater.inflate(R.layout.tables, null);

        return tables;
    }

}
