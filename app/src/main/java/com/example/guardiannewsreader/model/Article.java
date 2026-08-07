package com.example.guardiannewsreader.model;

import java.io.Serializable;

/**
 * Represents one Guardian news article.
 */
public class Article implements Serializable {

    private long id;
    private String title;
    private String url;
    private String section;

    /**
     * Creates an article returned from the Guardian API.
     *
     * @param title article title
     * @param url article URL
     * @param section article section
     */
    public Article(String title, String url, String section) {
        this.id = 0;
        this.title = title;
        this.url = url;
        this.section = section;
    }

    /**
     * Creates an article loaded from SQLite.
     *
     * @param id database ID
     * @param title article title
     * @param url article URL
     * @param section article section
     */
    public Article(long id, String title, String url, String section) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.section = section;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSection() {
        return section;
    }

    @Override
    public String toString() {
        return title;
    }
}