package com.kovalenych.tests;

import android.widget.EditText;
import com.kovalenych.R;
import junit.framework.Assert;

import java.util.Random;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class editCycleTest extends GeneralTest {


    public String NEW_TABLE_NAME = "newTable";

    public editCycleTest() {
        super();
        NEW_TABLE_NAME += new Random().nextInt(12345);
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

            clickOnViewByID(R.id.add_cycle);
            EditText breathEdit = (EditText) solo.getView(R.id.breath_edit);
            EditText holdEdit = (EditText) solo.getView(R.id.hold_edit);
            EditText timesEdit = (EditText) solo.getView(R.id.repeat_edit);

            solo.clearEditText(breathEdit);
            solo.enterText(breathEdit, "10");
            solo.clearEditText(holdEdit);
            solo.enterText(holdEdit, "10");
            solo.clearEditText(timesEdit);
            solo.enterText(timesEdit, "2");
            solo.clickOnText("add");

            clickOnViewByID(R.id.add_cycle);
            solo.clearEditText(breathEdit);
            solo.enterText(breathEdit, "11");
            solo.clearEditText(holdEdit);
            solo.enterText(holdEdit, "11");
            solo.clearEditText(timesEdit);
            solo.enterText(timesEdit, "2");
            solo.clickOnText("add");

            clickOnViewByID(R.id.add_cycle);
            solo.clearEditText(breathEdit);
            solo.enterText(breathEdit, "12");
            solo.clearEditText(holdEdit);
            solo.enterText(holdEdit, "12");
            solo.clearEditText(timesEdit);
            solo.enterText(timesEdit, "2");
            solo.clickOnText("add");

            /*Deleting this 2 cycles*/
            solo.clickLongOnText("11");
            solo.clickOnText("Edit");

            solo.clearEditText(breathEdit);
            solo.enterText(breathEdit, "13");
            solo.clearEditText(holdEdit);
            solo.enterText(holdEdit, "13");
            solo.clearEditText(timesEdit);
            solo.enterText(timesEdit, "3");
            solo.clickOnText("add");

            solo.sleep(10000);

        }
        //---------------------------------------------------------
        solo.goBack();
        solo.clickLongOnText(NEW_TABLE_NAME);
        solo.clickOnText("Delete");

    }

}