package com.deino.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Inwhite on 4/12/2015.
 */
public class Cluster {
    String type;
    String category;
    Date first_date;
    Date last_date;
    List<String> article_ids;

    public Cluster() {
        setType("");
        setCategory("");
        setFirst_date(new Date());
        setLast_date(new Date());
        setArticle_ids(new ArrayList<String>());
    }

    public void addArticleId(String id){
        article_ids.add(id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getFirst_date() {
        return first_date;
    }

    public void setFirst_date(Date first_date) {
        this.first_date = first_date;
    }

    public Date getLast_date() {
        return last_date;
    }

    public void setLast_date(Date last_date) {
        this.last_date = last_date;
    }

    public Integer getSize() {
        return getArticle_ids().size();
    }

    public List<String> getArticle_ids() {
        return article_ids;
    }

    public void setArticle_ids(List<String> article_ids) {
        this.article_ids = article_ids;
    }
}
