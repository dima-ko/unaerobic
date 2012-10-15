package com.kovalenych;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.kovalenych.media.Article;
import com.kovalenych.media.Video;
import com.kovalenych.media.VideoResponse;
import com.kovalenych.tables.SoundManager;
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
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class UnaeroApplication extends Application {

    ArrayDeque<Video> videoQueue;
    ArrayDeque<Article> articleQueue;

    @Override
    public void onCreate() {
        super.onCreate();

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

        videoQueue = new ArrayDeque<Video>();
        articleQueue = new ArrayDeque<Article>();

        if (haveInternet())
            new Thread() {
                @Override
                public void run() {

                    updateVideo();
                    //  updateArticles();

                }
            }.start();

    }

    String videoUrl = "http://unaerobic.appspot.com/co2gae";

    private void updateVideo() {

        InputStream source = retrieveStream(videoUrl);

        Gson gson = new Gson();

        Reader reader = new InputStreamReader(source);

        VideoResponse response = gson.fromJson(reader, VideoResponse.class);

//        Toast.makeText(this, response.query, Toast.LENGTH_SHORT).show();

        List<Video> videos = response.videos;

        for (Video video : videos) {
            Log.d("added new video ", "" + video.getTitle() + "    uri " + video.getUri());
//            Toast.makeText(this, video.fromUser, Toast.LENGTH_SHORT).show();
            videoQueue.add(video);
        }

    }

    public ArrayDeque<Video> getVideos() {
        return videoQueue;
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