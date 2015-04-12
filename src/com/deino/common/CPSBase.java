package com.deino.common;

import java.text.SimpleDateFormat;

/**
 * Created by Inwhite on 12.04.2015..
 */
public abstract class CPSBase {
    public static final String FIRST_DATE = "first_date";
    public static final String LAST_DATE = "last_date";
    public static final String CAT_ID = "category_id";
    public static final String ARTICLE = "article";
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRED_CAT = "predefined_category";
    public static final String IMG_URL = "img_url";
    public static final String URL = "url";
    public static final String SRC = "source";
    public static final String CLUST_ID = "cluster_id";
    public static final String TOKEN = "token";
    public static final String FREQ = "frequency";
    public static final String TYPE = "type";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public abstract String toXML();
    private String id;
    private String type;
}
