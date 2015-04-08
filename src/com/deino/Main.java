package com.deino;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        DelfiParser delfi = new DelfiParser();
        HashMap<String, Article> result = delfi.getMessages();
        for (Map.Entry<String, Article> entry : result.entrySet()) {
            Article temp = entry.getValue();
            System.out.println("=================================");
            System.out.println(temp.getDescription());
            System.out.println(temp.getImg_url());
            System.out.println("=================================");
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println("**********************************");
        System.out.println();
        System.out.println();

        DienaParser diena = new DienaParser();
        result = diena.getMessages();
        for (Map.Entry<String, Article> entry : result.entrySet()) {
            Article temp = entry.getValue();
            System.out.println("=================================");
            System.out.println(temp.getDescription());
            System.out.println(temp.getImg_url());
            System.out.println("=================================");
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println("**********************************");
        System.out.println();
        System.out.println();

        TvnetParser tvnet = new TvnetParser();
        result = tvnet.getMessages();
        for (Map.Entry<String, Article> entry : result.entrySet()) {
            Article temp = entry.getValue();
            System.out.println("=================================");
            System.out.println(temp.getDescription());
            System.out.println(temp.getImg_url());
            System.out.println("=================================");
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println("**********************************");
        System.out.println();
        System.out.println();

        ApolloParser apollo = new ApolloParser();
        result = apollo.getMessages();
        for (Map.Entry<String, Article> entry : result.entrySet()) {
            Article temp = entry.getValue();
            System.out.println("=================================");
            System.out.println(temp.getDescription());
            System.out.println(temp.getImg_url());
            System.out.println("=================================");
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println("**********************************");
        System.out.println();
        System.out.println();

        LSMParser lsm = new LSMParser();
        result = lsm.getMessages();
        for (Map.Entry<String, Article> entry : result.entrySet()) {
            Article temp = entry.getValue();
            System.out.println("=================================");
            System.out.println(temp.getDescription());
            System.out.println(temp.getImg_url());
            System.out.println("=================================");
            System.out.println();
        }
    }

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
