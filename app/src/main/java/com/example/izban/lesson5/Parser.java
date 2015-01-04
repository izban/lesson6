package com.example.izban.lesson5;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by izban on 20.10.14.
 */
public class Parser {
    XmlPullParser parser;
    Random rng = new Random(58);

    Parser(XmlPullParser parser) {
        this.parser = parser;
    }

    // sorry, I don't know what format is it
    int pos(String[] a, String s) {
        for (int i = 0; i < a.length; i++) {
            if (a[i].equals(s)) {
                return i;
            }
        }
        return 0;
    }

    Long parse(String s) {
        StringTokenizer st = new StringTokenizer(s);
        if (st.countTokens() != 6) {
            return rng.nextLong();
        }
        String[] a = new String[6];
        for (int i = 0; i < 6; i++) {
            a[i] = st.nextToken();
        }
        Long ans = 0L;
        ans += Long.parseLong(a[3]);
        String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        ans *= 12;
        ans += pos(months, a[2]);
        ans *= 31;
        ans += Long.parseLong(a[1]);
        ans *= 24;
        ans += (a[4].charAt(0) - '0') * 10 + (a[4].charAt(1) - '0');
        ans *= 60;
        ans += (a[4].charAt(3) - '0') * 10 + (a[4].charAt(4) - '0');
        ans *= 60;
        ans += (a[4].charAt(6) - '0') * 10 + (a[4].charAt(7) - '0');
        return ans;
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
            } else if (tag.equals("pubDate")) {
                parser.next();
                res.time = parse(parser.getText()); // ATTENTION, ATTENTION
                parser.next();
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
        }
        return result;
    }
}
