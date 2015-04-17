package com.deino.article_reader;

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
public class DienaParser extends RSSFeedParser {
    private static final String FEED_URL = "http://www.diena.lv/latvija.xml";

    public DienaParser() {
        super(FEED_URL);
        messages = new HashMap<String, Article>();
        category_urls.put("local", "http://www.diena.lv/latvija.xml");
        category_urls.put("abroad", "http://www.diena.lv/pasaule.xml");
        category_urls.put("sport", "http://www.diena.lv/sports.xml");
        category_urls.put("culture", "http://www.diena.lv/kd.xml");
        category_urls.put("entertainment", "http://www.diena.lv/izklaide.xml");
        category_urls.put("business","http://www.db.lv/finanses.xml");
        category_urls.put("technology","http://www.db.lv/tehnologijas.xml");
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
                getValue(item, DESCRIPTION);
                String raw_description = getValue(item, DESCRIPTION,1);
                HTMLParser htmlParser = new HTMLParser(raw_description);
                art.setDescription(htmlParser.getText());
                art.setImg_url(getAttribute(item,"enclosure","url"));
                art.setPublication_date(getValue(item, PUB_DATE));
                art.setPredefinedCategory(getValue(item, CATEGORY));
                art.setCategory(getUrl_category());
                art.setURL(getValue(item, LINK));
                art.setSource(FeedManager.DIENA);
                addMessage(art);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}