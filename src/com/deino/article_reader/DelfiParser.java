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
 * Created by Inwhite on 07.04.2015..
 */
public class DelfiParser extends RSSFeedParser {
    private static final String FEED_URL = "http://www.delfi.lv/rss.php";

    public DelfiParser() {
        super(FEED_URL);
        messages = new HashMap<String, Article>();
         category_urls.put("local", "http://www.delfi.lv/latvija_rss.php");
        category_urls.put("business", "http://www.delfi.lv/bizness/rss.php");
        category_urls.put("abroad", "http://www.delfi.lv/arzemes_rss.php");
        category_urls.put("sport", "http://www.delfi.lv/sports/rss.php");
        category_urls.put("culture", "http://www.delfi.lv/izklaide_rss.php");
        category_urls.put("car", "http://www.delfi.lv/auto/rss.php");
        category_urls.put("technology", "http://www.delfi.lv/tehnika/rss.php");
        category_urls.put("entertainment", "http://www.delfi.lv/izklaide/rss.php");
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
                art.setImg_url(htmlParser.getLastImgURL());

                art.setPublication_date(getValue(item, PUB_DATE));
                art.setCategory(getValue(item, CATEGORY));
                art.setPredefined_category(getUrl_category());
                art.setURL(getValue(item, LINK));
                art.setSource(FeedManager.APOLLO);
                addMessage(art);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
