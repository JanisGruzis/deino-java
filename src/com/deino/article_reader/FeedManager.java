package com.deino.article_reader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inwhite on 08.04.2015..
 */
public class FeedManager {
    public static final String DELFI = "Delfi";
    public static final String APOLLO = "Apollo";
    public static final String DIENA = "Diena";
    public static final String LSM = "LSM";
    public static final String TVNET = "TVNET";
    public static final Map<String, RSSFeedParser> feeds;

    static {
        HashMap<String, RSSFeedParser> tmp = new HashMap<>();
//        tmp.put(DELFI, new DelfiParser());
        tmp.put(APOLLO, new ApolloParser());
//        tmp.put(DIENA, new DienaParser());
//        tmp.put(LSM, new LSMParser());
//        tmp.put(TVNET, new TvnetParser());
        feeds = Collections.unmodifiableMap(tmp);
    }

    public static HashMap<String, Article> getMessages() {
        HashMap<String, Article> messages = new HashMap<>();
        for (Map.Entry<String, RSSFeedParser> entry : feeds.entrySet()) {
            System.out.println("Reading " + entry.getKey() + "...");
            RSSFeedParser f = entry.getValue();

            messages.putAll(f.getMessages());
        }
        return messages;
    }
}