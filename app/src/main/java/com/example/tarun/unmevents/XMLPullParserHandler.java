package com.example.tarun.unmevents;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by Tarun on 2/20/2016.
 */
public class XMLPullParserHandler {
    List<Event> events;
    private Event event;
    private String text;
    SimpleDateFormat c = new SimpleDateFormat("yyyy-MM-d'T'HH:mm:ss'Z'");
    Date d;
    public XMLPullParserHandler() {
        events = new ArrayList<Event>();
    }

    public List<Event> getEvents() {
        return events;
    }
    public List<Event> parse(InputStream is) {

        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();
/*            while (eventType != XmlPullParser.END_DOCUMENT) {
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String tagname = parser.getName();
                    if (tagname.equalsIgnoreCase("vevent")) {
                        event = new Event();
                    } else if (tagname.equalsIgnoreCase("uid")) {
                        String id = readid(parser);
                        event.setId(id);
                    } else {
                        skip(parser);
                    }
                }
                eventType = parser.next();
            }*/
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("vevent")) {
                            // create a new instance of employee
                            event = new Event();
                        }
                        if (tagname.equalsIgnoreCase("uid")) {
                            String id = readid(parser);
                            event.setId(id);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("vevent")) {
                            // add employee object to list
                            events.add(event);
                        } /*else if (tagname.equalsIgnoreCase("uid")) {
                            String id = readid(parser);
                            event.setId(id);
                        }*/ else if (tagname.equalsIgnoreCase("dtstamp")) {
                            try{
                                d = c.parse(text);
                                event.setCreateTime(d);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (tagname.equalsIgnoreCase("summary")) {
                            event.setSummary(text);
                        } else if (tagname.equalsIgnoreCase("description")) {
                            event.setDescription(text);
                        } else if (tagname.equalsIgnoreCase("dtstart")) {
                            try{
                                d = c.parse(text);
                                event.setStartTime(d);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else if (tagname.equalsIgnoreCase("dtend")) {
                            try{
                                d = c.parse(text);
                                event.setEndTime(d);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (tagname.equalsIgnoreCase("categories")) {
                            event.setCategory(text);
                        } else if (tagname.equalsIgnoreCase("class")) {
                            event.setEventClass(text);
                        } else if (tagname.equalsIgnoreCase("location")) {
                            event.setLocation(text);
                        } else if (tagname.equalsIgnoreCase("organizer")) {
                            event.setOrganizer(text);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        }catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return events;
    }

    private String readid(XmlPullParser parser) throws IOException, XmlPullParserException {
        String ns = null;
        String id = "";
        //parser.require(XmlPullParser.START_TAG, ns, "uid");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equals("text")) {
                    id = readText(parser);
                }
            }
        }
        return id;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        //if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        //}
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
