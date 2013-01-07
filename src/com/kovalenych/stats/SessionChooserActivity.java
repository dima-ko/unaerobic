package com.kovalenych.stats;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
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
    ListView list;
    private int tmpKeyPosition;
    private Dialog dialog;
    StatsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        tableName = getIntent().getStringExtra("tableName");
        setContentView(R.layout.session_chooser);
        dao = new StatsDAO(this, tableName, false);

        adapter = new StatsAdapter(this, dao);
        list = (ListView) findViewById(R.id.session_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showPlot(i);
            }
        });
        Toast.makeText(this, "long click to add comment", Toast.LENGTH_LONG).show();

        ((TextView)findViewById(R.id.cycles_hist_ab_name)).setText(tableName);
        findViewById(R.id.cycles_all_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.clearHistoryOfTable();
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.session_dialog);
                dialog.show();
                tmpKeyPosition = i;

                dialog.findViewById(R.id.session_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dao.deleteSession(tmpKeyPosition);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.findViewById(R.id.session_ok_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newComment = ((EditText) dialog.findViewById(R.id.session_comment_edit)).getText().toString();
                        dao.updateComment(tmpKeyPosition, newComment);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });

    }


    private void showPlot(int position) {
        SalesStackedBarChart chart = new SalesStackedBarChart();
        long sessioId = dao.getItemId(position);

        Cursor cursor = dao.getSessionTimeLine(sessioId);


        ArrayList<ArrayList<Double>> statList = new ArrayList<ArrayList<Double>>();

        int cycleNum = 0;
        while (cursor.moveToNext()) {
            switch (cursor.getInt(cursor.getColumnIndex(C_EVENT_TYPE))) {
                case BREATH_FINISHED:
                    int object = -cursor.getInt(cursor.getColumnIndex(C_EVENT_TIME));
                    statList.add(new ArrayList<Double>());
                    statList.get(statList.size() - 1).add((double) object);
                    cycleNum++;
                    if (object < chart.yMin)
                        chart.yMin = object;
                    break;
                case HOLD_FINISHED:
                    int anInt = cursor.getInt(cursor.getColumnIndex(C_EVENT_TIME));
                    statList.get(statList.size() - 1).add((double) anInt);
                    if (anInt > chart.yMax)
                        chart.yMax = anInt;
                    break;
                case CONTRACTION:
                    int anInt2 = cursor.getInt(cursor.getColumnIndex(C_EVENT_TIME));
                    statList.get(statList.size() - 1).add((double) anInt2);
                    if (anInt2 > chart.yMax)
                        chart.yMax = anInt2;
                    break;
            }
        }
        cursor.close();
        int numberOfLayersMax = 0;
        for (ArrayList<Double> list : statList) {
            if (list.size() > numberOfLayersMax)
                numberOfLayersMax = list.size();
        }

        for (int i = 0; i < numberOfLayersMax; i++) {
            double[] layer = new double[cycleNum];
            int counter = 0;
            for (ArrayList<Double> list : statList) {
                if (i < list.size()) {
                    layer[counter] = list.get(i);
                } else
                    layer[counter] = 0;
                counter++;
            }
            chart.values.add(layer);
        }

        chart.colors = new int[numberOfLayersMax];
        chart.colors[0] = Color.CYAN;
        if (chart.colors.length > 1)
            chart.colors[1] = Color.BLUE;

        chart.titles = new String[numberOfLayersMax];
        chart.titles[0] = "Breathe";
        if (chart.titles.length > 1)
            chart.titles[1] = "Normal hold";


        for (int i = 2; i < numberOfLayersMax; i++) {
            chart.colors[i] = Color.rgb((i) * 60 + 30, 0, 250 - (i) * 50);
            chart.titles[i] = "after " + (i - 1) + ((i == 2) ? " contraction" : " c/a");
        }


        chart.xMax = statList.size() + 0.5;
        if (chart.xMax < 8) chart.xMax = 8;

        startActivity(chart.execute(context));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dao.onDestroy();
    }


}
