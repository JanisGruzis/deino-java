package com.deino.clusteriazation;

import com.deino.article_reader.Article;
import com.deino.article_reader.FeedManager;
import com.deino.article_reader.NLP;
import com.deino.article_reader.Token;
import com.deino.common.Database;

import java.util.*;

/**
 * Created by Rob the Bob on 12.04.2015..
 */
public class Main {

    public static final int keyword_limit=3;

    private static void save_article(Article article)
    {
        if(Database.isExistingArticle(article.getId())) {
            //System.out.println(article.toXML());
            //throw new RuntimeException();
            return;
        }
        ArrayList<Token> all_keywords= NLP.getTopTokens(article.getTitle() + " " + article.getDescription());
        HashMap<String,Double> top_keywords=new HashMap<>();
        int i=0;
        for(Token token : all_keywords)
        {
            if(i++>=keyword_limit)
                break;
            top_keywords.put(token.token, token.value);
        }

        article.setKeywords(top_keywords);
        Database.insert(article);
    }

    private static void insertNewArticles()
    {
        HashMap<String,Article> articles= FeedManager.getMessages();
        for(Map.Entry<String,Article> entry : articles.entrySet())
        {
            save_article(entry.getValue());
        }
    }

    private static void clusterizeArticle(Article article)
    {
        List<Article> similar=Database.getSimilarArticles(article);
        HashMap<String,List<Article>> equal=new HashMap<>();
        List<Article> articles;

        if(similar!=null) {
            for (Article other : similar) {
                if (NLP.isEqual(article.getKeywords(), other.getKeywords())) {
                    articles = equal.get(other.getCluster());
                    if (articles == null) {
                        articles = new ArrayList<>();
                        equal.put(other.getCluster(), articles);
                    }
                    articles.add(other);
                }
            }
        }

        articles=equal.get(article.getCluster());
        if(articles==null) {
            articles = new ArrayList<>();
            equal.put(article.getCluster(),articles);
        }
        articles.add(article);

        String cluster_id = Database.createCluster(articles);
        equal.remove("-1");
        equal.put(cluster_id, articles);

        Database.mergeCluster(equal.keySet());
    }

    public static void clusteizeNewArticles()
    {
        Article article;
        Integer i=0;
        while((article=Database.getUnclusterizedArticle())!=null) {

            System.out.println((++i).toString()+" "+article.getTitle());
            clusterizeArticle(article);
        }
    }

    public static void main(String[] args) {
        System.out.println((new Date()).toString());
        insertNewArticles();
        System.out.println((new Date()).toString());
        clusteizeNewArticles();
        System.out.println((new Date()).toString());
    }

}
