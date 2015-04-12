package com.deino.common;

import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.CPSException;
import com.clusterpoint.api.request.CPSInsertRequest;
import com.clusterpoint.api.request.CPSRetrieveRequest;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.request.CPSUpdateRequest;
import com.clusterpoint.api.response.CPSListLastRetrieveFirstResponse;
import com.clusterpoint.api.response.CPSSearchResponse;
import com.deino.article_reader.Article;
import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Rob the Bob on 11.04.2015..
 */
public class Database {

    public static final CPSConnection connection;
    static{
        CPSConnection tmp;
        try {
            Scanner in=new Scanner(new InputStreamReader(new FileInputStream(new File("db.conf"))));
            //System.out.printf("'%s' '%s' '%s' '%s'\n",in.next(),in.next(),in.next(),in.next(),in.next(),in.next());
            tmp=new CPSConnection(in.next(),in.next(),in.next(),in.next(),in.next(),in.next(),in.next());
        }catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        connection=tmp;
    }

    public static boolean isExistingArticle(String id) {
        CPSRetrieveRequest req=new CPSRetrieveRequest(id);
        CPSListLastRetrieveFirstResponse resp = null;
        try {
            resp = (CPSListLastRetrieveFirstResponse) connection.sendRequest(req);
        } catch (CPSException e) {
            if(e.getMessage().contains("[2824]")) //[2824]:Requested document does not exist
            {
                return false;
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resp.getDocuments().size()==1;
    }

    public static void update(Article article) {
        CPSUpdateRequest req=new CPSUpdateRequest();
        LinkedList<String> docs=new LinkedList<>();
        docs.add(article.toXML());
        req.setStringDocuments(docs);
        try {
            connection.sendRequest(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insert(Article article) {
        CPSInsertRequest req=new CPSInsertRequest();
        LinkedList<String> docs=new LinkedList<>();
        docs.add(article.toXML());
        req.setStringDocuments(docs);
        try {
            connection.sendRequest(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Article getUnclusterizedArticle() {
        try {
            CPSSearchRequest req = new CPSSearchRequest("<cluster_id>-1</cluster_id>", 0, 1);
            CPSSearchResponse resp = (CPSSearchResponse) connection.sendRequest(req);
            if (resp.getFound() == 0)
                return null;

            Article article=parseArticle(resp.getDocuments().get(0));

            return article;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Article> getSimilarArticles(Article article)
    {

        try {

            Calendar cal=Calendar.getInstance();
            cal.setTime(article.getPublication_date());
            cal.add(Calendar.DATE,-2); //minus two days;
            String date=Article.dateFormat.format(cal.getTime());

            StringBuilder builder=new StringBuilder();

            for(Map.Entry<String,Double> entry : article.getKeywords().entrySet())
            {
                builder.append(entry.getKey()).append(' ');
            }

            CPSSearchRequest req = new CPSSearchRequest(String.format(
                "<id>~%s</id>" +
                        "<source>~%s</source>" +
                        "<predefined_category>%s</predefined_category>" +
                        "<date> >= %s</date>" +
                        "<token>{%s}</token>",
                    StringEscapeUtils.escapeXml10(article.getId()),
                    StringEscapeUtils.escapeXml10(article.getSource()),
                    StringEscapeUtils.escapeXml10(date),
                    builder.toString()
            ), 0, 1000);

            CPSSearchResponse resp = (CPSSearchResponse) connection.sendRequest(req);
            if (resp.getFound() == 0)
                return null;

            ArrayList<Article> similar=new ArrayList<>();

            for(Element item : resp.getDocuments())
            {
                similar.add(parseArticle(item));
            }

            return similar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Article parseArticle(Element doc)
    {
        Article article=new Article();
        try {
            article.setPublication_date(Article.dateFormat.parse(doc.getElementsByTagName("date").item(0).getNodeValue()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        article.setTitle(doc.getElementsByTagName("title").item(0).getNodeValue());
        article.setDescription(doc.getElementsByTagName("description").item(0).getNodeValue());
        article.setCategory(doc.getElementsByTagName("category_id").item(0).getNodeValue());
        article.setURL(doc.getElementsByTagName("url").item(0).getNodeValue());
        article.setPredefined_category(doc.getElementsByTagName("predefined_category").item(0).getNodeValue());
        article.setImg_url(doc.getElementsByTagName("img_url").item(0).getNodeValue());
        article.setSource(doc.getElementsByTagName("source").item(0).getNodeValue());
        article.setCluster(doc.getElementsByTagName("cluster_id").item(0).getNodeValue());

        HashMap<String,Double> keywords=new HashMap<>();
        Element item;
        NodeList items=doc.getElementsByTagName("token");
        for (int i = 0; i < items.getLength(); i++) {
            item=(Element)items.item(i);
            keywords.put(item.getNodeValue(),Double.parseDouble(item.getAttribute("frequency")));
        }

        article.setKeywords(keywords);

        return article;
    }
    public static Cluster getCluster(String id){
        try {
            CPSSearchRequest req = new CPSSearchRequest(String.format(
                    "<type>cluster</type>" +
                    "<id>%s</id>",id), 0, 1);
            CPSSearchResponse resp = (CPSSearchResponse) connection.sendRequest(req);
            if (resp.getFound() == 0)
                return null;
            Cluster cl = parseCluster(resp.getDocuments().get(0));
            return cl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Cluster parseCluster(Element doc) {
        Cluster cl = new Cluster();

        try {
            cl.setFirst_date(Article.dateFormat.parse(doc.getElementsByTagName("first_date").item(0).getNodeValue()));
            cl.setLast_date(Article.dateFormat.parse(doc.getElementsByTagName("last_date").item(0).getNodeValue()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        cl.setCategory(doc.getElementsByTagName("category").item(0).getNodeValue());

        Element item;
        NodeList items=doc.getElementsByTagName("articles");
        for (int i = 0; i < items.getLength(); i++) {
            item=(Element)items.item(i);
            cl.getArticle_ids().add(item.getNodeValue());
        }
        return cl;
    }

    public static String mergeCluster(String first_id,String second_id) {
        Cluster first = getCluster(first_id);
        Cluster second = getCluster(second_id);
        if(first != null && second != null){
            first.getArticle_ids().addAll(second.getArticle_ids()); // i believed Rob on this , but i think he lied...
            setClusterForArticles(first.getId(),second.getArticle_ids());
            //remove second cluster from DB
        }
        return null;
    }

    public static void setClusterForArticles(String cluster_id, List<String> articles_id){
        //update all articles with ids in list, with provided cluster id
    }
}
