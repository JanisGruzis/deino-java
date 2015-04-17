package com.deino.article_reader;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public abstract class RSSFeedParser {
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String CHANNEL = "channel";
    static final String LANGUAGE = "language";
    static final String COPYRIGHT = "copyright";
    static final String LINK = "link";
    static final String AUTHOR = "author";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String CATEGORY = "category";
    protected HashMap<String, Article> messages;


    protected URL url;
    protected String url_category;
    protected HashMap<String, String> category_urls;


    public RSSFeedParser(String feedUrl) {
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        category_urls = new HashMap<>();
         /*
        category_urls.put("local", "");
        category_urls.put("business", "");
        category_urls.put("abroad", "");
        category_urls.put("sport", "");
        category_urls.put("culture", "");
        category_urls.put("car", "");
        category_urls.put("technology", "");
        category_urls.put("entertainment", "");
     */
    }

    public void setUrl(String feedUrl) {
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract boolean readFeed();

    public HashMap<String, Article> getMessages() {
        for (Map.Entry<String, String> entry : category_urls.entrySet()) {
            setUrl_category(entry.getKey());
            setUrl(entry.getValue());
            readFeed();
        }
        return messages;
    }


    public String getValue(Element parent, String nodeName) {
        String result = parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
        result = result.trim();
        return result;
    }

    public String getValue(Element parent, String nodeName, int index) {
        NodeList list = parent.getElementsByTagName(nodeName).item(0).getChildNodes();
        Node item = list.item(index);
        String result = item.getNodeValue();
        result = result.trim();
        return result;
    }

    public String getAttribute(Element parent, String nodeName, String attrName) {
        String result = "";
        NodeList list = parent.getElementsByTagName(nodeName);
        if(list.getLength() >0){
           result =  list.item(0).getAttributes().getNamedItem(attrName).getNodeValue();
        }
        return result     ;
    }


    protected void addMessage(Article art) {
        messages.put(art.getURL(), art);
    }


    public String getUrl_category() {
        return url_category;
    }

    public void setUrl_category(String url_category) {
        this.url_category = url_category;
    }
}
