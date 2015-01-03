package com.example.izban.lesson5;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by izban on 20.10.14.
 */
public class Parser {
    XmlPullParser parser;

    Parser(XmlPullParser parser) {
        this.parser = parser;
    }

    Item parseItem() throws IOException, XmlPullParserException {
        Item res = new Item();
        while (parser.next() != XmlPullParser.END_TAG || !parser.getName().equals("item")) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals("link")) {
                parser.next();
                res.link = parser.getText();
                parser.next();
            } else if (tag.equals("title")) {
                parser.next();
                res.title = parser.getText();
                parser.next();
            } else if (tag.equals("description")) {
                while (parser.next() != XmlPullParser.END_TAG || !parser.getName().equals("description")) {
                    if (parser.getEventType() == XmlPullParser.TEXT) {
                        res.description += parser.getText();
                    }
                }
            }
        }
        return res;
    }

    ArrayList<Item> parse() throws IOException, XmlPullParserException {
        ArrayList<Item> result = new ArrayList<Item>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("item")) {
                result.add(parseItem());
            }
            /*if (result.size() == 10) {
                break;
            }*/
        }
        return result;
    }
}
