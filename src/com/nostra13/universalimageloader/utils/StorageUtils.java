package com.nostra13.universalimageloader.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Provides application storage paths
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public final class StorageUtils {

    private StorageUtils() {
    }

    /**
     * Returns application cache directory. Cache directory will be created on SD card if card is mounted. Else -
     * Android defines cache directory on device's file system.
     *
     * @param context      Application context
     * @param cacheDirPath Cache directory path for SD card (if SD card is mounted). <b>i.e.:</b> "AppDir_cache",
     *                     "AppDir/Cache/Images"
     * @return Cache {@link File directory}
     */
    public static File getCacheDirectory(Context context, String cacheDirPath) {
        File appCacheDir;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            appCacheDir = new File(context.getExternalCacheDir(), cacheDirPath);
            Log.d("zzzzz", "MEDIA_MOUNTED" + appCacheDir.getAbsolutePath());
        } else {
            appCacheDir = context.getCacheDir();
            Log.d("zzzzz", "MEDIA_NOTMOUNTED");
        }
        if (!appCacheDir.exists()) {
            appCacheDir.mkdirs();
            Log.d("zzzzz", "DONTEXIST");
        }
        return appCacheDir;
    }
}
