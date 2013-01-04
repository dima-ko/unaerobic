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
public class MenuTest extends GeneralTest {

    public void testMenu() throws Exception {

        solo.clickOnText("VIDEO");
        solo.sleep(1000);

        solo.clickOnText("ARTICLES");
        solo.sleep(1000);

        solo.clickOnText("RANKING");
        solo.sleep(1000);

        solo.clickOnText("TABLES");
        solo.sleep(1000);
    }
}