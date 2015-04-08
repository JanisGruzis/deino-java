package com.deino;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by Inwhite on 08.04.2015..
 */
public class HTMLParser {
    private   Document html_doc;
    public HTMLParser(String html) {
        html_doc = Jsoup.parse(html);
    }

    public Document getHtml_doc() {
        return html_doc;
    }

    public void setHtml_doc(Document html_doc) {
        this.html_doc = html_doc;
    }

    public String getLastImgURL() {
        if(html_doc != null){
            Element link = html_doc.select("img").last();
            if(link!= null) {
                String url = link.attr("src");
                return url;
            }
        }
        return "";
    }
    public String getFirstImgURL() {
        if(html_doc != null){
            Element link = html_doc.select("img").first();
            if(link!= null) {
                String url = link.attr("src");
                return url;
            }
        }
        return "";
    }

    public String getText() {
        if(html_doc != null){
            String text = html_doc.text();
            text = text.replaceAll("\\<.*?>","");
            text = text.trim();
            text = text.replaceAll("\\s+", " ");
            return text;
        }
        return "";
    }
}
