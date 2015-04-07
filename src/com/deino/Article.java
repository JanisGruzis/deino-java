package com.deino;

import java.util.Date;

/**
 * Created by Inwhite on 07.04.2015..
 */
public class Article {
    private Date publication_date;
    private String title;
    private String description;
    private String category;
    private String URL;

    public Article() {
        title = description = category = URL = "";
        publication_date  = new Date();
    }

    public Date getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(Date publication_date) {
        this.publication_date = publication_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}

