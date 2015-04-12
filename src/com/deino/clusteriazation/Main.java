package com.deino.clusteriazation;

import com.deino.article_reader.Article;
import com.deino.article_reader.FeedManager;
import com.deino.article_reader.NLP;
import com.deino.common.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Created by Rob the Bob on 12.04.2015..
 */
public class Main {

    public static final int keyword_limit=5;

    private static void save_article(Article article)
    {
        if(Database.isExistingArticle(article.getId())) {
            System.out.println(article.toXML());
            throw new RuntimeException();
            //return;
        }
        TreeMap<String,Double> all_keywords= NLP.getTopTokens(article.getTitle() + " " + article.getDescription());
        HashMap<String,Double> top_keywords=new HashMap<>();
        int i=0;
        for(Map.Entry<String,Double> entry : all_keywords.entrySet())
        {
            if(i++>=keyword_limit)
                break;
            top_keywords.put(entry.getKey(), entry.getValue());
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
        
    }

    public static void main(String[] args) {
        insertNewArticles();
    }

}
