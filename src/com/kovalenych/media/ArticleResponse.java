package com.kovalenych.media;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ArticleResponse {

    public List<Article> articles;

    @SerializedName("max_id")
    public long maxId;

    @SerializedName("since_id")
    public long sinceId;

    public String query;

}
