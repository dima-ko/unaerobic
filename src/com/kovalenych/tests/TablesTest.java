package com.kovalenych.tests;

import android.view.View;
import android.widget.ListView;
import com.kovalenych.R;
import junit.framework.Assert;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class TablesTest extends
        GeneralTest {


    public void testTables() throws Exception {

        solo.clickOnText("ТАБЛИЦЫ");
        solo.sleep(1000);

        solo.clickLongOnText("CO2");

        ListView cyclesList = (ListView) solo.getView(R.id.cycles_list);
        solo.clickInList(2);

        solo.sleep(5000);

        solo.goBack();

        View view = solo.getView(R.id.stop_button);
        Assert.assertTrue(view.getVisibility() == View.VISIBLE);

        solo.clickInList(1);

        solo.sleep(5000);

        view = solo.getView(R.id.stop_button);
        solo.clickOnView(view);

        view = solo.getView(R.id.stop_button);
        Assert.assertTrue(view.getVisibility() == View.VISIBLE);




    }
}