package com.kovalenych.tests;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class TablesTest extends
        GeneralTest {


    public void testTables() throws Exception {

        solo.clickOnText("ВИДЕО");
        solo.sleep(1000);

        solo.clickOnText("СТАТЬИ");
        solo.sleep(1000);

        solo.clickOnText("РЕКОРДЫ");
        solo.sleep(1000);

        solo.clickOnText("ТАБЛИЦЫ");
        solo.sleep(1000);
    }
}