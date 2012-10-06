package com.kovalenych.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.jayway.android.robotium.solo.Solo;
import com.kovalenych.MenuActivity;
import junit.framework.Assert;

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
//        super.setUp();
//        this.activity = this.getActivity();
        solo = new Solo(getInstrumentation(), getActivity());
    }

//     solo.sendKey(Solo.MENU);
//    solo.clickOnText("More");
//    solo.clickOnText("Preferences");
//    solo.clickOnText("Edit File Extensions");
//    Assert.assertTrue(solo.searchText("rtf"));
//
//    solo.clickOnText("txt");
//    solo.clearEditText(2);
//    solo.enterText(2, "robotium");
//    solo.clickOnButton("Save");
//    solo.goBack();
//    solo.clickOnText("Edit File Extensions");
//    Assert.assertTrue(solo.searchText("application/robotium"));

    public void testSettings() throws Exception {


        solo.waitForFragmentById(1);
////        solo.clickOnText();
//        clickOnButtonByID(R.id.settings);
//        solo.sleep(1000);
//        View settingsDialog = solo.getView(SmsActivity.SETDIALOG_ID);
//        Assert.assertTrue(settingsDialog.getVisibility() == View.VISIBLE);
//
//        clickOnButtonByID(R.id.settings);
//        solo.sleep(2000);
//        Assert.assertFalse(settingsDialog.getVisibility() != View.VISIBLE);

    }

    public void testLife() throws Exception {
//        EditText numberEdit = (EditText) solo.getView(R.id.number_edit);
//        solo.clickOnView(numberEdit);
//        solo.enterText(numberEdit, "0937062222");
//
//        EditText textEdit = (EditText) solo.getView(R.id.text_edit);
//        solo.clickOnView(textEdit);
//        solo.enterText(textEdit, "asdasdaasdasdasdasdasdasdasdasdas");
//        clickOnButtonByID(R.id.sendbutton);
//        solo.sleep(1000);
//        solo.assertCurrentActivity("Expected MainActivity to launch", SmsActivity.class);

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