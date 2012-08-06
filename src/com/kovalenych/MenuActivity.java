package com.kovalenych;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import com.sample.BaseSampleActivity;
import com.sample.TestFragment;
import com.sample.TestFragmentAdapter;
import com.viewpagerindicator.TabPageIndicator;


public class MenuActivity extends BaseSampleActivity {

    private static final String[] CONTENT = new String[]{"Tables", "Videos", "Articles", "Settings"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_tabs);

        mAdapter = new GoogleMusicAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }

    class GoogleMusicAdapter extends TestFragmentAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
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