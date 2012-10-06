package com.kovalenych.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import com.jayway.android.robotium.solo.Solo;
import com.kovalenych.MenuActivity;

import java.util.List;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class MenuTest extends
        ActivityInstrumentationTestCase2<MenuActivity> {

    private Solo solo;
//    private Activity activity;


    public MenuTest() {
        super("com.kovalenych",
                MenuActivity.class);

    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testTables() throws Exception {

        solo.clickOnText("ТАБЛИЦЫ");
        solo.sleep(1000);
    }

    private void clickOnButtonByID(int ID) {
        // get a list of all ImageButtons on the current activity
        List<Button> btnList = solo.getCurrentButtons();
        for (int i = 0; i < btnList.size(); i++) {
            Button btn = btnList.get(i);
            // find button by id
            if (btn.getId() == ID) {
                // click on the button using index (not id !!!)
                solo.clickOnButton(i);
                // check if new activity is the 'About'
            } else {
                // other code
            }
        }
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}