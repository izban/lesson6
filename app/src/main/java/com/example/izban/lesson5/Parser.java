package com.example.izban.lesson5;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by izban on 20.10.14.
 */
public class Parser extends DefaultHandler {
    String channel;
    StringBuilder curText;
    Item curItem = new Item();
    ArrayList<Item> items = new ArrayList<Item>();
    Random rng = new Random(58);

    Parser(String channel) {
        this.channel = channel;
    }

    static ArrayList<Item> parse(Channel channel) {
        Log.i("", "starting parse " + channel);
        Parser parser = new Parser(channel.link);
        try {
            URL url = new URL(channel.link);
            InputStream inputStream = url.openStream();
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setContentHandler(parser);
            reader.parse(new InputSource(inputStream));
            Log.i("", "parsed ok");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("", String.format("parsed %d items", parser.items.size()));
        return parser.items;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Log.i("", "I am alive");
        super.startElement(uri, localName, qName, attributes);

        curText = new StringBuilder();

        if (qName.equals("item")) {
            curItem = new Item();
            curItem.channel = channel;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Log.i("", "~I am alive");
        super.endElement(uri, localName, qName);
        Log.i("", "~~I am alive");

        if (qName.equals("item")) {
            items.add(curItem);
        } else if (qName.equals("title")) {
            curItem.title = curText.toString();
        } else if (qName.equals("link")) {
            curItem.link = curText.toString();
        } else if (qName.equals("description")) {
            curItem.description = curText.toString();
        } else if (qName.equals("pubDate")) {
            curItem.time = parseDate(curText.toString());
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        Log.i("", "I am living");
        super.characters(ch, start, length);

        curText.append(ch, start, length);
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

    Long parseDate(String s) {
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
}
