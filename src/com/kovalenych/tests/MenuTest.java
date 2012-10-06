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

    public void testSettings() throws Exception {


//        solo.clickOnText("Таблицы");
//        solo.sleep(1000);
//        solo.clickOnText("Рекорды");
//        solo.sleep(1000);
//        solo.clickOnText("Статьи");
//        solo.sleep(1000);
////        solo.clickOnText();
//        clickOnButtonByID(R.id.settings);
//        solo.sleep(1000);
//        View settingsDialog = solo.getView(SmsActivity.SETDIALOG_ID);
//        Assert.assertTrue(settingsDialog.getVisibility() == View.VISIBLE);

    }

    public void testLifeCycle() throws Exception {

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