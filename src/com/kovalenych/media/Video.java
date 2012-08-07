package com.kovalenych.media;


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
        int picIdBegin = uri.indexOf("v=") + 2;
        String youTubeID = uri.substring(picIdBegin, picIdBegin + 11);
        pictureUri = "http://i3.ytimg.com/vi/" + youTubeID + "/default.jpg";


    }


}
