package com.kovalenych.tests;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;
import com.kovalenych.R;
import junit.framework.Assert;

import java.util.Random;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class RankingTest extends GeneralTest {

    public RankingTest() {
        super();
    }

    public void testRank() throws Exception {

        solo.clickOnText("RANKING");

        clickOnViewByID(R.id.discipline_button);
        solo.clickOnText("DNF");

        solo.clickOnText("Show");
        solo.sleep(800);

        if (!haveInternet())
            return;
        if (!solo.searchText("Mullins"))
            solo.sleep(20000);

        solo.scrollToBottom();

        clickOnViewByID(R.id.filter_triangle);

        solo.sleep(800);

        solo.clickOnText("Show");
        Assert.assertTrue(solo.searchText("Mullins"));

    }

    public boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to disable internet while roaming, just return false
            return true;
        }
        return true;
    }

}