package com.kovalenych;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import com.fragments.*;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;


public class MenuActivity extends FragmentActivity {

    private static final String[] CONTENT = new String[]{"TABLES", "RANKING", "ARTICLES", "VIDEO"};
    protected TestFragmentAdapter mAdapter;
    protected ViewPager mPager;
    protected PageIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);

        mAdapter = new FreeDivingAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }

    class FreeDivingAdapter extends TestFragmentAdapter {
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