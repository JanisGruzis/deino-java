package com.deino;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Inwhite on 07.04.2015..
 */
public class DelfiParser extends RSSFeedParser {
    private static final String FEED_URL = "http://www.delfi.lv/rss.php";

    public DelfiParser() {
        super(FEED_URL);
        messages = new HashMap<String, Article>();
    }

    @Override
    public boolean readFeed() {
        try {
            boolean isFeedHeader = true;
            // Set header values intial to the empty string
            String description = "";
            String title = "";
            String link = "";
            String language = "";
            String copyright = "";
            String author = "";
            String pubdate = "";
            String guid = "";

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            boolean item_started = false;
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName()
                            .getLocalPart();
                    if(item_started) {
                        switch (localPart) {
                            case CHANNEL:
                                if (isFeedHeader) {
                                    isFeedHeader = false;
                                    // TODO: can make feed class and fill it with delfi specific info (4-5 tags)
                                }
                                event = eventReader.nextEvent();
                                break;
                            case TITLE:
                                title = getCharacterData(event, eventReader);
                                break;
                            case DESCRIPTION:
                                description = getCharacterData(event, eventReader);
                                break;
                            case LINK:
                                link = getCharacterData(event, eventReader);
                                break;
                            case GUID:
                                guid = getCharacterData(event, eventReader);
                                break;
                            case LANGUAGE:
                                language = getCharacterData(event, eventReader);
                                break;
                            case AUTHOR:
                                author = getCharacterData(event, eventReader);
                                break;
                            case PUB_DATE:
                                pubdate = getCharacterData(event, eventReader);
                                break;
                            case COPYRIGHT:
                                copyright = getCharacterData(event, eventReader);
                                break;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart() == (ITEM)) {
                        item_started = true;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
                        Article art = new Article();
                        art.setDescription(description);
                        art.setURL(link);
                        art.setTitle(title);
                        addMessage(art);
                        item_started = false;

                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public HashMap<String, Article> getMessages() {
        readFeed();
        return messages;
    }

}
