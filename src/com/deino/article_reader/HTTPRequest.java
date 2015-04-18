package com.deino.article_reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Inwhite on 4/11/2015.
 */
public class HTTPRequest {
    public static HashMap<String, String> getResponseHeader(String url){
        HttpURLConnection.setFollowRedirects(false);
        URLConnection conn = null;
        URL obj = null;
        try {
            obj = new URL(url);
            conn = obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, List<String>> map = conn.getHeaderFields();
        HashMap<String,String> result = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
           result.put(entry.getKey(),entry.getValue().get(0));
        }
        return result;
    }

    public static String getContent(String url){
        StringBuilder sb = new StringBuilder();
        URL oracle = null;
        try {
            oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            yc.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
               sb.append(inputLine);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
