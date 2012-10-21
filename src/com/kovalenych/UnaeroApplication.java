package com.kovalenych;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.kovalenych.media.*;
import com.kovalenych.ranking.DBHelper;
import com.nostra13.universalimageloader.imageloader.ImageLoader;
import com.nostra13.universalimageloader.imageloader.ImageLoaderConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class UnaeroApplication extends Application {


    SharedPreferences preferences;
    private MediaDBHelper dbHelper;
    public static volatile boolean updLock = false;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences("mediaUpdate", Context.MODE_PRIVATE);
        final long lastUpdTime = preferences.getLong("lastUpd", 0);

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .maxImageWidthForMemoryCache(displayMetrics.widthPixels)
                .maxImageHeightForMemoryCache(displayMetrics.heightPixels)
                .threadPoolSize(3)
                .discCacheDir("UniversalImageLoaderApp/Cache")
                .memoryCacheSize(1500000)
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        if (haveInternet())
            new Thread() {
                @Override
                public void run() {
                    updLock = true;
                    updateVideo(lastUpdTime);
                    updateArticles(lastUpdTime);
                    updLock = false;
                    Log.d("Unaer", "updated");

                }
            }.start();

    }


    private void updateArticles(long lastUpdTime) {

        InputStream source = retrieveStream(articleUrl + "?lasttime=" + lastUpdTime);
        if (source == null)
            return;

        Gson gson = new Gson();
        Reader reader = new InputStreamReader(source);
        ArticleResponse response = gson.fromJson(reader, ArticleResponse.class);

        List<Article> articles = response.articles;

        dbHelper = new MediaDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Article article : articles) {
            ContentValues cv = new ContentValues();
            cv.put(MediaDBHelper.C_ART_NAME, article.getName());
            cv.put(MediaDBHelper.C_ART_URL, article.getUri());
            cv.put(MediaDBHelper.C_ART_AUTHOR, article.getAuthor());
            db.insert(MediaDBHelper.ARTICLES_TABLE, null, cv);
        }
        db.close();
        dbHelper.close();

        preferences.edit().putLong("lastUpd", new Date().getTime());

        //todo: notifyDataSetChanged();
    }

    final String videoUrl = "http://unaerobic.appspot.com/co2gaevideo";
    final String articleUrl = "http://unaerobic.appspot.com/co2gaearticle";

    private void updateVideo(long lastUpdTime) {

        InputStream source = retrieveStream(videoUrl + "?lasttime=" + lastUpdTime);
        if (source == null)
            return;
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(source);
        VideoResponse response = gson.fromJson(reader, VideoResponse.class);

        List<Video> videos = response.videos;


        dbHelper = new MediaDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Video video : videos) {
            ContentValues cv = new ContentValues();
            cv.put(MediaDBHelper.C_VIDEO_NAME, video.getTitle());
            cv.put(MediaDBHelper.C_VIDEO_URL, video.getUri());
            db.insert(MediaDBHelper.VIDEO_TABLE, null, cv);
        }
        db.close();
        dbHelper.close();


        preferences.edit().putLong("lastUpd", new Date().getTime());

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


}