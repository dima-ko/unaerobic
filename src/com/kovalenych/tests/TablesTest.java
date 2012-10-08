package com.kovalenych.tests;

import android.view.View;
import android.widget.ListView;
import com.kovalenych.R;
import com.kovalenych.tables.ClockActivity;
import junit.framework.Assert;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class TablesTest extends GeneralTest {


    public void testTables() throws Exception {

        solo.clickOnText("TABLES");
        solo.sleep(1000);

        solo.clickOnText("CO2");

        solo.clickInList(2);

        solo.sleep(2000);

        solo.goBack();

        View view = solo.getView(R.id.stop_button_cycles);
        Assert.assertTrue(view.getVisibility() == View.VISIBLE);

        solo.clickInList(1);

        solo.sleep(2000);

        view = solo.getView(ClockActivity.STOP_CLOCK_ID);
        solo.clickOnView(view);

        view = solo.getView(R.id.stop_button_cycles);
        Assert.assertFalse(view.getVisibility() == View.VISIBLE);

        solo.clickInList(2);

        solo.sleep(2000);

        solo.goBack();
        solo.goBack();

        view = solo.getView(R.id.stop_button_tables);
        Assert.assertTrue(view.getVisibility() == View.VISIBLE);
        solo.clickOnView(view);


    }
}