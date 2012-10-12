package com.kovalenych;

import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;
import com.kovalenych.tables.SoundManager;
import com.nostra13.universalimageloader.imageloader.ImageLoader;
import com.nostra13.universalimageloader.imageloader.ImageLoaderConfiguration;

public class UnaeroApplication extends Application {

    SoundManager soundManager;
    float volume;

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

        soundManager = new SoundManager(this);
    }

    public void playSound(int index) {
        Log.d("UnaeroApplication", "playSound  " + index + "  with volume " + volume);
        soundManager.playSound(index, volume);
    }

    public void setVolume(float volume) {
        Log.d("UnaeroApplication", "setVolume " + volume);
        this.volume = volume;
    }


}