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
import com.kovalenych.media.ArticleResponse;
import com.kovalenych.media.Video;
import com.kovalenych.media.VideoResponse;
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

    ArrayList<Video> videoQueue;
    ArrayList<Article> articleQueue;

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

        videoQueue = new ArrayList<Video>();
        articleQueue = new ArrayList<Article>();

        if (haveInternet())
            new Thread() {
                @Override
                public void run() {

                    updateVideo();
                    updateArticles();

                }
            }.start();

    }


    private void updateArticles() {
        Date now = new Date();
        InputStream source = retrieveStream(articleUrl + "?lasttime=" + now.getTime());
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(source);
        ArticleResponse response = gson.fromJson(reader, ArticleResponse.class);

        List<Article> articles = response.articles;

        for (Article article : articles) {
            Log.d("added new article ", "" + article.getName() + "    uri " + article.getUri());
//            Toast.makeText(this, video.fromUser, Toast.LENGTH_SHORT).show();
            article.toDomain();
            articleQueue.add(article);
        }

    }

    final String videoUrl = "http://unaerobic.appspot.com/co2gaevideo";
    final String articleUrl = "http://unaerobic.appspot.com/co2gaearticle";

    private void updateVideo() {

        InputStream source = retrieveStream(videoUrl);
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(source);
        VideoResponse response = gson.fromJson(reader, VideoResponse.class);

        List<Video> videos = response.videos;

        for (Video video : videos) {
            Log.d("added new video ", "" + video.getTitle() + "    uri " + video.getUri());
//            Toast.makeText(this, video.fromUser, Toast.LENGTH_SHORT).show();
            videoQueue.add(video);
        }


    }

    public ArrayList<Video> getVideos() {
        return videoQueue;
    }

    public ArrayList<Article> getArticles() {
        return articleQueue;
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