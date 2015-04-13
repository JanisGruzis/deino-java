package com.deino.common;

import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.CPSException;
import com.clusterpoint.api.request.*;
import com.clusterpoint.api.response.CPSListLastRetrieveFirstResponse;
import com.clusterpoint.api.response.CPSModifyResponse;
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

    static {
        CPSConnection tmp;
        try {
            Scanner in = new Scanner(new InputStreamReader(new FileInputStream(new File("db.conf"))));
//            System.out.printf("'%s' '%s' '%s' '%s'\n",in.next(),in.next(),in.next(),in.next(),in.next(),in.next());
            tmp = new CPSConnection(in.next(), in.next(), in.next(), in.next(), in.next(), in.next(), in.next());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        connection = tmp;
    }

    public static boolean isExistingArticle(String id) {
        CPSRetrieveRequest req = new CPSRetrieveRequest(id);
        CPSListLastRetrieveFirstResponse resp = null;
        try {
            resp = (CPSListLastRetrieveFirstResponse) connection.sendRequest(req);
        } catch (CPSException e) {
            if (e.getMessage().contains("[2824]")) //[2824]:Requested document does not exist
            {
                return false;
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resp.getDocuments().size() == 1;
    }

    public static void update(CPSBase obj) {
        CPSUpdateRequest req = new CPSUpdateRequest();
        LinkedList<String> docs = new LinkedList<>();
        docs.add(obj.toXML());
        req.setStringDocuments(docs);
        try {
            connection.sendRequest(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void insert(CPSBase obj) {
        CPSInsertRequest req = new CPSInsertRequest();
        LinkedList<String> docs = new LinkedList<>();
        docs.add(obj.toXML());
        req.setStringDocuments(docs);
        try {
            connection.sendRequest(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Article getUnclusterizedArticle() {
        try {
            CPSSearchRequest req = new CPSSearchRequest("<" + CPSBase.CLUST_ID + ">-1</" + CPSBase.CLUST_ID + ">", 0, 1);
            CPSSearchResponse resp = (CPSSearchResponse) connection.sendRequest(req);
            if (resp.getFound() == 0)
                return null;

            Article article = parseArticle(resp.getDocuments().get(0));

            return article;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static List<Article> getSimilarArticles(Article article) {

        try {

            Calendar cal = Calendar.getInstance();
            cal.setTime(article.getPublication_date());
            cal.add(Calendar.DATE, -2); //minus two days;
            String date = CPSBase.dateFormat.format(cal.getTime());

            StringBuilder builder = new StringBuilder();

            for (Map.Entry<String, Double> entry : article.getKeywords().entrySet()) {
                builder.append(entry.getKey()).append(' ');
            }

            CPSSearchRequest req = new CPSSearchRequest(String.format(
                    "<id>~%s</id>" +
                            "<" + CPSBase.SRC + ">~%s</" + CPSBase.SRC + ">" +
                            "<" + CPSBase.PRED_CAT + ">%s</" + CPSBase.PRED_CAT + ">" +
                            "<" + CPSBase.DATE + "> >= %s</" + CPSBase.DATE + ">" +
                            "<" + CPSBase.TOKEN + ">{%s}</" + CPSBase.TOKEN + ">",
                    StringEscapeUtils.escapeXml10(article.getId()),
                    StringEscapeUtils.escapeXml10(article.getSource()),
                    StringEscapeUtils.escapeXml10(date),
                    builder.toString()
            ), 0, 1000);

            CPSSearchResponse resp = (CPSSearchResponse) connection.sendRequest(req);
            if (resp.getFound() == 0)
                return null;

            ArrayList<Article> similar = new ArrayList<>();

            for (Element item : resp.getDocuments()) {
                similar.add(parseArticle(item));
            }

            return similar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Article parseArticle(Element doc) {
        Article article = new Article();
        try {
            article.setPublication_date(CPSBase.dateFormat.parse(doc.getElementsByTagName(CPSBase.DATE).item(0).getChildNodes().item(0).getNodeValue()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        article.setTitle(doc.getElementsByTagName(CPSBase.TITLE).item(0).getChildNodes().item(0).getNodeValue());
        article.setDescription(doc.getElementsByTagName(CPSBase.DESCRIPTION).item(0).getChildNodes().item(0).getNodeValue());
        NodeList list = doc.getElementsByTagName(CPSBase.CAT_ID);
        if (list.getLength() > 0)
            article.setCategory(list.item(0).getNodeValue());

        article.setURL(doc.getElementsByTagName(CPSBase.URL).item(0).getChildNodes().item(0).getNodeValue());
        article.setPredefined_category(doc.getElementsByTagName(CPSBase.PRED_CAT).item(0).getChildNodes().item(0).getNodeValue());
        article.setImg_url(doc.getElementsByTagName(CPSBase.IMG_URL).item(0).getChildNodes().item(0).getNodeValue());
        article.setSource(doc.getElementsByTagName(CPSBase.SRC).item(0).getChildNodes().item(0).getNodeValue());
        article.setCluster(doc.getElementsByTagName(CPSBase.CLUST_ID).item(0).getChildNodes().item(0).getNodeValue());

        HashMap<String, Double> keywords = new HashMap<>();
        Element item;
        NodeList items = doc.getElementsByTagName(CPSBase.TOKEN);
        for (int i = 0; i < items.getLength(); i++) {
            item = (Element) items.item(i);
            Double frequency = Double.parseDouble(item.getAttribute(CPSBase.FREQ));
            String value = item.getChildNodes().item(0).getNodeValue();
            keywords.put(value, frequency);
        }

        article.setKeywords(keywords);

        return article;
    }

    public static Cluster getCluster(String id) {
        try {
            CPSSearchRequest req = new CPSSearchRequest(String.format(
                    "<" + CPSBase.TYPE + ">cluster</" + CPSBase.TYPE + ">" +
                            "<" + CPSBase.ID + ">%s</" + CPSBase.ID + ">"
                    , id), 0, 1);
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
            cl.setFirst_date(CPSBase.dateFormat.parse(doc.getElementsByTagName(CPSBase.FIRST_DATE).item(0).getChildNodes().item(0).getNodeValue()));
            cl.setLast_date(CPSBase.dateFormat.parse(doc.getElementsByTagName(CPSBase.LAST_DATE).item(0).getChildNodes().item(0).getNodeValue()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        cl.setCategory_id(doc.getElementsByTagName(CPSBase.CAT_ID).item(0).getChildNodes().item(0).getNodeValue());
        cl.setId(doc.getElementsByTagName(CPSBase.ID).item(0).getChildNodes().item(0).getNodeValue());

        Node item;
        NodeList items = doc.getElementsByTagName(CPSBase.ARTICLE);
        for (int i = 0; i < items.getLength(); i++) {
            item = items.item(i).getChildNodes().item(0);
            cl.getArticle_ids().add(item.getNodeValue());
        }
        return cl;
    }

    public static String mergeCluster(String first_id, String second_id) {
        Cluster first = getCluster(first_id);
        Cluster second = getCluster(second_id);
        if (first != null && second != null) {
            first.getArticle_ids().addAll(second.getArticle_ids()); // i believed Rob on this , but i think he lied...
            setClusterForArticles(first.getId(), second.getArticle_ids());
            delete(second.getId());
            first.setFirst_date(
                    first.getFirst_date().compareTo(second.getFirst_date()) == -1 ?
                            first.getFirst_date() : second.getFirst_date());

            first.setLast_date(
                    first.getLast_date().compareTo(second.getLast_date()) == 1 ?
                            first.getLast_date() : second.getLast_date());
            update(first);
            return first.getId();
        }
        return null;
    }

    public static String mergeCluster(Set<String> ids) {
        if(ids.size()<2)
            return null;

        Iterator<String> it=ids.iterator();
        String first_id=it.next();
        while(it.hasNext())
        {
            first_id=mergeCluster(first_id,it.next());
        }

        return first_id;
    }

    private static String[] delete(String id) {
        try {
            String ids[] = {id};
            CPSDeleteRequest delete_req = new CPSDeleteRequest(ids);
            CPSModifyResponse delete_resp = (CPSModifyResponse) connection.sendRequest(delete_req);
            //Print out deleted ids
            System.out.println("Deleted ids: " + Arrays.toString(delete_resp.getModifiedIds()));
            return delete_resp.getModifiedIds();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setClusterForArticles(String cluster_id, List<String> articles_id) {
        CPSUpdateRequest req = new CPSUpdateRequest();
        LinkedList<String> docs = new LinkedList<>();
        for (String id : articles_id) {
            docs.add(String.format("<document>" +
                    "<" + CPSBase.ID + ">%s</" + CPSBase.ID + ">" +
                    "<" + CPSBase.CLUST_ID + ">%s</" + CPSBase.CLUST_ID + ">" +
                    "</document>", id, cluster_id));
        }
        req.setStringDocuments(docs);
        try {
            connection.sendRequest(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String createCluster(List<Article> articles) {
        Cluster c = new Cluster();
        for(Article a : articles)
        {
            if(c.getFirst_date()==null || c.getFirst_date().compareTo(a.getPublication_date())==1)
                c.setFirst_date(a.getPublication_date());

            if(c.getLast_date()==null || c.getLast_date().compareTo(a.getPublication_date())==-1)
                c.setLast_date(a.getPublication_date());

            c.getArticle_ids().add(a.getId());
        }

        c.setId(UUID.randomUUID().toString());
        insert(c);
        setClusterForArticles(c.getId(),c.getArticle_ids());


        return c.getId();
    }
}
