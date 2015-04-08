package com.deino;

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
    HashMap<String, RSSFeedParser> feeds;

    public FeedManager() {
        feeds = new HashMap<>();
        feeds.put(DELFI, new DelfiParser());
        feeds.put(APOLLO, new ApolloParser());
        feeds.put(DIENA, new DienaParser());
        feeds.put(LSM, new LSMParser());
        feeds.put(TVNET, new TvnetParser());
    }

    public HashMap<String, Article> getMessages() {
        HashMap<String, Article> messages = new HashMap<>();
        for (Map.Entry<String, RSSFeedParser> entry : feeds.entrySet()) {
            RSSFeedParser f = entry.getValue();

            messages.putAll(f.getMessages());
        }
        return messages;
    }


}
