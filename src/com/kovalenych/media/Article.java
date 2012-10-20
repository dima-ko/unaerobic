package com.kovalenych.media;


import android.util.Log;

public class Article {

    private final String name;
    private final String author;
    private final String uri;
    private String domain;

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getUri() {
        return uri;
    }

    public String getDomain() {
        return domain;
    }

    public Article(String name, String author, String uri) {
        this.name = name;
        this.author = author;
        this.uri = uri;
        toDomain();
    }


    /**
     * finding domain by url
     */
    public void toDomain() {
        String noHttp = "";
        try {
            noHttp = uri.substring(7, uri.length());
        } catch (Exception e) {
            Log.e("exceptionzzzzz", uri + "\n");
            e.printStackTrace();
        }
        String noEnd;
        if (noHttp.contains("/"))
            noEnd = noHttp.substring(0, noHttp.indexOf("/"));
        else noEnd = noHttp;
        domain = noEnd;
    }


}
