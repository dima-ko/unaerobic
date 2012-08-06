package com.kovalenych;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import com.sample.TestFragment;
import com.sample.TestFragmentAdapter;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;


public class MenuActivity extends FragmentActivity {

    private static final String[] CONTENT = new String[]{"Tables", "Videos", "Articles", "Settings"};

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
            return TestFragment.newInstance(MenuActivity.CONTENT[position % MenuActivity.CONTENT.length]);
        }

        @Override
        public int getCount() {
            return MenuActivity.CONTENT.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return MenuActivity.CONTENT[position % MenuActivity.CONTENT.length].toUpperCase();
        }
    }

}