package com.kovalenych;


import android.graphics.Bitmap;
import android.util.Log;

public class Video {

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    private final String title;
    private final String uri;
    private final String pictureUri;



    public Video(String title, String uri) {

        this.title = title;
        this.uri = uri;
        String youTubeID = uri.substring(31,42);
        Log.d("zzzz", youTubeID);

        pictureUri = "http://i3.ytimg.com/vi/" + youTubeID + "/default.jpg";


    }


}
