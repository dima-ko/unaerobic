package com.kovalenych.tests;

import android.view.View;
import android.widget.EditText;
import com.kovalenych.R;
import com.kovalenych.tables.ClockActivity;
import junit.framework.Assert;

import java.util.Random;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class TableTest extends GeneralTest {


    public String NEW_TABLE_NAME = "newTable";

    public TableTest() {
        super();
        NEW_TABLE_NAME += new Random().nextInt(12345);
    }

    public void testAddDeleteTables() throws Exception {

        solo.clickOnText("TABLES");

        clickOnViewByID(R.id.add_table);

        EditText nameEdit = (EditText) solo.getView(R.id.new_table_edit);
        solo.enterText(nameEdit, NEW_TABLE_NAME);
        solo.clickOnText("add");

        solo.clickLongOnText(NEW_TABLE_NAME);
        solo.clickOnText("Delete");

    }


    public void testAll() throws Exception {

        solo.clickOnText("TABLES");

        clickOnViewByID(R.id.add_table);

        EditText nameEdit = (EditText) solo.getView(R.id.new_table_edit);
        solo.enterText(nameEdit, NEW_TABLE_NAME);
        solo.clickOnText("add");
        solo.clickOnText(NEW_TABLE_NAME);
        solo.sleep(500);
        //---------------------------------------------------------
        {

            /*Adding 2 identical cycles*/
            for (int i = 0; i < 2; i++) {
                clickOnViewByID(R.id.add_cycle);
                EditText breathEdit = (EditText) solo.getView(R.id.breath_edit);
                solo.clearEditText(breathEdit);
                solo.enterText(breathEdit, "10");
                EditText holdEdit = (EditText) solo.getView(R.id.hold_edit);
                solo.clearEditText(holdEdit);
                solo.enterText(holdEdit, "10");
                solo.clickOnText("add");
            }

            Assert.assertTrue(solo.searchText("x2"));

            /*Deleting this 2 cycles*/
            solo.clickLongOnText("x2");
            solo.clickOnText("Delete");

            Assert.assertFalse(solo.searchText("10"));

        }
        //---------------------------------------------------------
        solo.goBack();
        solo.clickLongOnText(NEW_TABLE_NAME);
        solo.clickOnText("Delete");

    }

}