package com.kovalenych.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import com.jayway.android.robotium.solo.Solo;
import com.kovalenych.MenuActivity;

import java.util.List;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class GeneralTest extends
        ActivityInstrumentationTestCase2<MenuActivity> {

    protected Solo solo;
//    private Activity activity;


    public GeneralTest() {
        super("com.kovalenych",
                MenuActivity.class);

    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    protected void clickOnViewByID(int ID) {
        // get a list of all ImageButtons on the current activity
        List<View> views = solo.getCurrentViews();
        for (View view : views) {
            // find button by id
            if (view.getId() == ID) {
                // click on the button using index (not id !!!)
                solo.clickOnView(view);
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