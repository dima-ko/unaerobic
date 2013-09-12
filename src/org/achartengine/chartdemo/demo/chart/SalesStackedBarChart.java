/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.achartengine.chartdemo.demo.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.kovalenych.R;
import com.kovalenych.Utils;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * Sales demo bar chart.
 */
public class SalesStackedBarChart extends AbstractDemoChart {
    public int yMin;
    public int yMax;
    public double xMax;

    public List<double[]> values = new ArrayList<double[]>();
    public String[] titles;
    public int[] colors;


    public Intent execute(Context context) {

        Collections.reverse(values);
        Utils.reverseIntArray(colors);
        Utils.reverseStringArray(titles);
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);

        setChartSettings(renderer, context.getString(R.string.statistics),
                context.getString(R.string.cycles), context.getString(R.string.time_s), 0.5,
                xMax, yMin * 1.1, yMax * 1.1, Color.GRAY, Color.LTGRAY);
        for (int i = 0; i < colors.length; i++) {
            renderer.getSeriesRendererAt(i).setDisplayChartValues(true);
        }
        renderer.setXLabels(12);
        renderer.setYLabels(10);
        renderer.setXLabelsAlign(Align.LEFT);
        renderer.setYLabelsAlign(Align.LEFT);
        renderer.setPanEnabled(true, true);
        // renderer.setZoomEnabled(false);
        renderer.setZoomRate(1.1f);
        renderer.setBarSpacing(0.5f);
        return ChartFactory.getBarChartIntent(context, buildBarDataset(titles, values), renderer,
                Type.STACKED);
    }


    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Sales stacked bar chart";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The monthly sales for the last 2 years (stacked bar chart)";
    }

    /**
     * Executes the chart demo.
     *
     * @param context the context
     * @return the built intent
     */


}
