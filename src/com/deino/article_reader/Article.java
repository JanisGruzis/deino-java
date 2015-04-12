package com.deino.article_reader;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Inwhite on 07.04.2015..
 */
public class Article {

    public static String URLtoID(String URL)
    {
        return URL.replaceAll("\\W", "");
    }

    private Date publication_date;
    private String title;
    private String description;
    private String category;
    private String URL;
    private String predefined_category;
    private String img_url;
    private String source;
    private String id;
    private String cluster = "-1";
    private HashMap<String, Double> keywords;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    public Article() {
        title = description = category = URL = "";
        publication_date = new Date();
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
        id=URLtoID(URL);
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

    public String getId() {
        return id;
    }

    public HashMap<String, Double> getKeywords() {
        return keywords;
    }

    public void setKeywords(HashMap<String, Double> keywords) {
        this.keywords = keywords;
    }

    public String toXML() {
        StringBuilder s = new StringBuilder();
        s.append(String.format(
                "<document>" +
                        "<id>%s</id>" +
                        "<type>article</type>" +
                        "<date>%s</date>" +
                        "<title>%s</title>" +
                        "<description>%s</description>" +
                        "<category_id>%s</category_id>" +
                        "<url>%s</url>" +
                        "<predefined_category>%s</predefined_category>" +
                        "<img_url>%s</img_url>" +
                        "<source>%s</source>" +
                        "<cluster_id>%s</cluster_id>",
                StringEscapeUtils.escapeXml10(id),
                StringEscapeUtils.escapeXml10(dateFormat.format(publication_date)),
                StringEscapeUtils.escapeXml10(title),
                StringEscapeUtils.escapeXml10(description),
                StringEscapeUtils.escapeXml10(category),
                StringEscapeUtils.escapeXml10(URL),
                StringEscapeUtils.escapeXml10(predefined_category),
                StringEscapeUtils.escapeXml10(img_url),
                StringEscapeUtils.escapeXml10(source),
                StringEscapeUtils.escapeXml10(cluster)
        ));

        s.append("<tokens>");
        if (keywords != null) {
            for (Map.Entry<String, Double> entry : keywords.entrySet()) {
                s.append(String.format("<token frequency=\"%s\">%s</token>",
                        Double.toString(entry.getValue()),
                        StringEscapeUtils.escapeXml10(entry.getKey())));
            }
        }
        s.append("</tokens></document>");

        return s.toString();
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
}

