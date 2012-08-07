package com.kovalenych.media;


public class Article {

    private final String name;
    private final String author;
    private final String uri;
    private final String domain;

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
        this.domain = toDomain(uri);
    }


    /**
     * finding domain by url
     */
    private String toDomain(String uri) {
        String noHttp = uri.substring(7, uri.length());
        String noEnd;
        if (noHttp.contains("/"))
            noEnd = noHttp.substring(0, noHttp.indexOf("/"));
        else noEnd = noHttp;
        return noEnd;
    }


}
