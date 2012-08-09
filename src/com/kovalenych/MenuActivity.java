package com.kovalenych;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.fragments.*;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;


public class MenuActivity extends FragmentActivity {

    private static String[] CONTENT ;
    protected FragmentPagerAdapter mAdapter;
    protected ViewPager mPager;
    protected PageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.resolvePlatform(this);

        CONTENT = getResources().getStringArray(R.array.tabs);
        setContentView(R.layout.simple_tabs);

        mAdapter = new FreeDivingAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }


    public boolean haveInternet() {
        NetworkInfo info = ((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to disable internet while roaming, just return false
            return true;
        }
        return true;
    }

    class FreeDivingAdapter extends FragmentPagerAdapter {
        public FreeDivingAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return TablesFragment.newInstance();
                case 1:
                    return RankingFragment.newInstance();
                case 2:
                    return ArticlesFragment.newInstance();
                case 3:
                    return VideoFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return MenuActivity.CONTENT.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return MenuActivity.CONTENT[position % MenuActivity.CONTENT.length];
        }
    }

}