package com.kovalenych.stats;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.kovalenych.Const;
import com.kovalenych.R;
import org.achartengine.chartdemo.demo.chart.SalesStackedBarChart;

import java.util.ArrayList;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class SessionChooserActivity extends Activity implements Const {

    String tableName;
    Context context;
    StatsDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        tableName = getIntent().getStringExtra("tableName");
        setContentView(R.layout.session_chooser);
        dao = new StatsDAO(this, tableName, false);
        StatsAdapter adapter = new StatsAdapter(this, dao);
        ListView list = (ListView) findViewById(R.id.session_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPlot(i);
            }
        });

        Toast.makeText(this,"long click to add comment",Toast.LENGTH_LONG).show();

    }

    private void showPlot(int position) {
        SalesStackedBarChart chart = new SalesStackedBarChart();
        long sessioId = dao.getItemId(position);
        Cursor cursor = dao.getSessionTimeLine(sessioId);

        ArrayList<Double> breathList = new ArrayList<Double>();
        ArrayList<Double> holdList = new ArrayList<Double>();

        while (cursor.moveToNext()) {

            switch (cursor.getInt(cursor.getColumnIndex(C_EVENT_TYPE))) {
                case BREATH_FINISHED:
                    int object = -cursor.getInt(cursor.getColumnIndex(C_EVENT_TIME));
                    breathList.add((double) object);
                    if (object < chart.yMin)
                        chart.yMin = object;
                    break;
                case HOLD_FINISHED:
                    int anInt = cursor.getInt(cursor.getColumnIndex(C_EVENT_TIME));
                    holdList.add((double) anInt);
                    if (anInt > chart.yMax)
                        chart.yMax = anInt;
                    break;
            }
        }
        cursor.close();

        chart.values.add(toArray(breathList));
        chart.values.add(toArray(holdList));
        chart.xMax = breathList.size()+0.5;

        startActivity(chart.execute(context));
    }

    private double[] toArray(ArrayList<Double> doubleList) {
        Double[] breathArr = new Double[doubleList.size()];
        breathArr = doubleList.toArray(breathArr);
        double[] breathArrPrim = new double[doubleList.size()];
        for (int i = 0; i < breathArr.length; i++) {
            breathArrPrim[i] = breathArr[i];
        }

        return breathArrPrim;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.onDestroy();
    }

    //todo: destroy
}
