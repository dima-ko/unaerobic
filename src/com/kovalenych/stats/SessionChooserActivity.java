package com.kovalenych.stats;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.kovalenych.R;
import org.achartengine.chartdemo.demo.chart.SalesStackedBarChart;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class SessionChooserActivity extends Activity {

    String tableName;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        tableName = getIntent().getStringExtra("tableName");
        setContentView(R.layout.session_chooser);
        StatsDAO dao = new StatsDAO(this, tableName);
        StatsAdapter adapter = new StatsAdapter(this, dao);
        ListView list = (ListView) findViewById(R.id.session_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new SalesStackedBarChart().execute(context));
            }
        });

    }
}
