package com.deino;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Created by Inwhite on 08.04.2015..
 */
public class LSMParser extends RSSFeedParser {
    private static final String FEED_URL = "http://www.lsm.lv/rss/?lang=lv&catid=20";

    public LSMParser() {
        super(FEED_URL);
        messages = new HashMap<String, Article>();
        category_urls.put("local", "http://www.lsm.lv/rss/?lang=lv&catid=20");
        category_urls.put("business", "http://www.lsm.lv/rss/?lang=lv&catid=22");
        category_urls.put("abroad", "http://www.lsm.lv/rss/?lang=lv&catid=21");
        category_urls.put("sport", "http://www.lsm.lv/rss/?lang=lv&catid=15");
        category_urls.put("culture", "http://www.lsm.lv/rss/?lang=lv&catid=67");
        category_urls.put("car", "http://www.lsm.lv/rss/?lang=lv&catid=269");
        category_urls.put("technology", "http://www.lsm.lv/rss/?lang=lv&catid=61");
        category_urls.put("entertainment", "http://www.lsm.lv/rss/?lang=lv&catid=23");
    }

    @Override
    public boolean readFeed() {
        try {
            URLConnection urlConnection = url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in);

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);
                Article art = new Article();
                art.setTitle(getValue(item, TITLE));
                String raw_description = getValue(item, DESCRIPTION);
                HTMLParser htmlParser = new HTMLParser(raw_description);
                art.setDescription(htmlParser.getText());
                art.setImg_url(getAttribute(item,"enclosure","url"));

                art.setPublication_date(getValue(item, PUB_DATE));
                art.setPredefined_category(getUrl_category());
                art.setURL(getValue(item, LINK));
                art.setSource(FeedManager.LSM);
                addMessage(art);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
