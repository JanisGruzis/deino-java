package com.deino.article_reader;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


import com.deino.common.Database;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.xml.crypto.Data;

public class Main {



    public static void main(String[] args) {

//        Cluster cl =  new Cluster();
//        cl.setId("second");
//        cl.setFirst_date(new Date());
//        cl.setLast_date(new Date());
//        cl.addArticleId("22");
//        cl.addArticleId("33");
//        cl.setCategory_id("SECOND CATEGORY HURR DURR");
//        Article art = new Article();
//        art.setURL("11");
//        art.setCluster("first");
//        Database.insert(art);
//        art = new Article();
//        art.setURL("22");
//        art.setCluster("second");
//        Database.insert(art);
//        art = new Article();
//        art.setURL("33");
//        art.setCluster("second");
//        Database.insert(art);

//        Database.insert(cl);
//       Database.mergeCluster("first","second");

        HashMap<String,Article> result = FeedManager.getMessages();
        for (Map.Entry<String, Article> entry : result.entrySet()) {
            Article temp = entry.getValue();
            System.out.println("=================================");

//            System.out.println(temp.getSource());
//            System.out.println(temp.getTitle());
//            System.out.println(temp.getCategory());
            System.out.println(temp.getText());
//            System.out.println(temp.getLocation());
            System.out.println("=================================");
            System.out.println();
        }


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
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getPredefineCategory())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getCategory())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getPredefineCategory())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getDescription())));
            out.write(String.format("%s,", StringEscapeUtils.escapeCsv(a.getURL())));
            out.write(String.format("%s\n", StringEscapeUtils.escapeCsv(a.getImg_url())));
        }

        out.close();
    }

}
