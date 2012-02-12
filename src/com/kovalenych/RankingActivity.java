package com.kovalenych;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingActivity extends Activity {

    ListView lv;
    ArrayList<Record> recordsList;
    Context context;

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
        recordsList.add(new Record("breath-hold divers (Ama)", "Tamaki H & co", "http://www.ncbi.nlm.nih.gov/pubmed/20737928?dopt=Abstract"));
    }

    private void invalidateList() {

        SimpleAdapter adapter = new SimpleAdapter(this, createCyclesList(), R.layout.ranking,
                new String[]{"flag", "name", "result"},
                new int[]{R.id.art_name, R.id.art_author, R.id.art_domain});
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

}
