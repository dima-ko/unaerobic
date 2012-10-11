package com.kovalenych.tests;

import android.view.View;
import android.widget.EditText;
import com.kovalenych.R;
import com.kovalenych.tables.ClockActivity;
import junit.framework.Assert;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class DeleteTableTest extends GeneralTest {


    public void testTables() throws Exception {

        solo.clickOnText("TABLES");
        solo.sleep(200);
        solo.clickLongInList(3);
        solo.clickOnText("Delete");
        clickOnViewByID(R.id.add_table);

        EditText nameEdit  = (EditText) solo.getView(R.id.new_table_edit);
        solo.enterText(nameEdit,"blat");
        solo.clickOnText("add");

        solo.clickInList(3);
        solo.sleep(3000);

        solo.goBack();
        solo.goBack();

    }
}