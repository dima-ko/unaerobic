package com.kovalenych.media;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {

    public List<Video> videos;

    @SerializedName("max_id")
    public long maxId;

    @SerializedName("since_id")
    public int sinceId;

    public String query;

}
