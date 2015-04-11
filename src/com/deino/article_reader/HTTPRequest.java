package com.deino.article_reader;


import java.io.IOException;
import java.net.HttpURLConnection;
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
    
}
