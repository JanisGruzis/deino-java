package com.deino.article_reader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringEscapeUtils;

public class Main {



    public static void main(String[] args) {



        
//        HashMap<String,Article> result = FeedManager.getMessages();
//        for (Map.Entry<String, Article> entry : result.entrySet()) {
//            Article temp = entry.getValue();
//            System.out.println("=================================");
//
//            System.out.println(temp.getSource());
//            System.out.println(temp.getTitle());
//            System.out.println(temp.getURL());
//            System.out.println(temp.getLocation());
//            System.out.println("=================================");
//            System.out.println();
//        }


//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        try {
//			Main.saveToCsv(result, new File("articles_"+dateFormat.format(new Date())+".csv"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
    }
    public static void saveToCsv(HashMap<String, Article> articles, File f) throws Exception {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        Article a;
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        out.write("date,title,category,predefined_category,category,description,url,img_url\n");

        for (Map.Entry<String, Article> entry : articles.entrySet()) {
            a = entry.getValue();

            out.write(String.format("%s,", date_format.format(a.getPublication_date())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getTitle())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getCategory())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getPredefined_category())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getCategory())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getDescription())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getURL())));
            out.write(String.format("%s\n", StringEscapeUtils.escapeCsv(a.getImg_url())));
        }

        out.close();
    }

}
