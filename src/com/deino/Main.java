package com.deino;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.text.DateFormatter;

public class Main {
	
	public static void saveToCsv(HashMap<String,Article> articles, File f) throws Exception
	{
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
		Article a;
		SimpleDateFormat date_format=new SimpleDateFormat("yyyy/MM/dd");
		
		out.write("date,title,category,predefined_category,category,description,url,img_url\n");
		
		for (Map.Entry<String, Article> entry : articles.entrySet())
		{
			a=entry.getValue();
			
			out.write(String.format("%s,",date_format.format(a.getPublication_date())));
			out.write(String.format("%s,",StringEscapeUtils.escapeCsv(a.getTitle())));
			out.write(String.format("%s,",StringEscapeUtils.escapeCsv(a.getCategory())));
			out.write(String.format("%s,",StringEscapeUtils.escapeCsv(a.getPredefined_category())));
			out.write(String.format("%s,",StringEscapeUtils.escapeCsv(a.getCategory())));
			out.write(String.format("%s,",StringEscapeUtils.escapeCsv(a.getDescription())));
			out.write(String.format("%s,",StringEscapeUtils.escapeCsv(a.getURL())));
			out.write(String.format("%s\n",StringEscapeUtils.escapeCsv(a.getImg_url())));
		}
		
		out.close();
	}

    public static void main(String[] args) {

        FeedManager fm = new FeedManager();
        HashMap<String,Article> result = fm.getMessages();
        for (Map.Entry<String, Article> entry : result.entrySet()) {
            Article temp = entry.getValue();
            System.out.println("=================================");
            System.out.println(temp.getSource());
            System.out.println(temp.getTitle());
//            System.out.println(temp.getDescription());
            System.out.println(temp.getPredefined_category());
            System.out.println(temp.getCategory());
            System.out.println(temp.getImg_url());
            System.out.println("=================================");
            System.out.println();
        }
        
        try {
			Main.saveToCsv(result, new File("articles.csv"));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


}
