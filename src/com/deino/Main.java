package com.deino;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        FeedManager fm = new FeedManager();
        HashMap<String,Article> result = fm.getMessages();
        for (Map.Entry<String, Article> entry : result.entrySet()) {
            Article temp = entry.getValue();
            System.out.println("=================================");

            System.out.println(temp.getSource());
            System.out.println(temp.getTitle());
            System.out.println(temp.getDescription());
////            System.out.println(temp.getDescription());
//            System.out.println(temp.getPredefined_category());
//            System.out.println(temp.getCategory());
//            System.out.println(temp.getImg_url());
            System.out.println("=================================");
            System.out.println();
        }
    }


}
