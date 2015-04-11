package com.deino.article_reader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Inwhite on 07.04.2015..
 */
public class Article {
    private Date publication_date;
    private String title;
    private String description;
    private String category;
    private String URL;
    private String Location;
    private String predefined_category;
    private String img_url;
    private String source;


    public Article() {
        title = description = category = URL = "";
        publication_date  = new Date();
    }
    public Date getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) throws ParseException {
        DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy H:m:s", Locale.ENGLISH);
        Date date = format.parse(publication_date);
        this.publication_date = date;
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

    public void setPredefined_category(String predefined_category) {
        this.predefined_category = predefined_category;
    }

    public String getPredefined_category() {
        return predefined_category;
    }

    public void setPublication_date(Date publication_date) {
        this.publication_date = publication_date;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}

