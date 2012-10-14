package com.kovalenych;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.fragments.*;
import com.google.gson.Gson;
import com.kovalenych.media.SearchResponse;
import com.kovalenych.media.Video;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;


public class MenuActivity extends FragmentActivity implements Const {

    private static String[] CONTENT;
    public static final int TABLE_TAB = 0;
    public static final int RANK_TAB = 1;
    public static final int ART_TAB = 2;
    public static final int VIDEO_TAB = 3;
    protected FragmentPagerAdapter mAdapter;
    protected ViewPager mPager;
    protected PageIndicator mIndicator;
    public int curTab = 0;
    private static final String LOG_TAG = "MenuActivity";

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
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                curTab = i;
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });


        updateVideo();
    }

    String url = "http://search.twitter.com/search.json?q=javacodegeeks";

    private void updateVideo() {

        InputStream source = retrieveStream(url);

        Gson gson = new Gson();

        Reader reader = new InputStreamReader(source);

        SearchResponse response = gson.fromJson(reader, SearchResponse.class);

        Toast.makeText(this, response.query, Toast.LENGTH_SHORT).show();

        List<Video> videos = response.videos;

        for (Video video : videos) {
//            Toast.makeText(this, video.fromUser, Toast.LENGTH_SHORT).show();
        }

    }

    private InputStream retrieveStream(String url) {

        DefaultHttpClient client = new DefaultHttpClient();

        HttpGet getRequest = new HttpGet(url);

        try {

            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(),
                        "Error " + statusCode + " for URL " + url);
                return null;
            }

            HttpEntity getResponseEntity = getResponse.getEntity();
            return getResponseEntity.getContent();

        } catch (IOException e) {
            getRequest.abort();
            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }

        return null;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "onActivityResult" + resultCode);
        tablesFragment.onUpdateCurTable(data.getStringExtra(PARAM_TABLE));

        if (resultCode == STATUS_FINISH) {
            Log.d(LOG_TAG, "onActivityResult STATUS_FINISH");
            tablesFragment.onTableFinish();
        }
    }


    TablesFragment tablesFragment;

    class FreeDivingAdapter extends FragmentPagerAdapter {
        public FreeDivingAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case TABLE_TAB:
                    tablesFragment = TablesFragment.newInstance();
                    return tablesFragment;
                case RANK_TAB:
                    return RankingFragment.newInstance();
                case ART_TAB:
                    return ArticlesFragment.newInstance();
                case VIDEO_TAB:
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