package com.example.izban.lesson5;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by izban on 20.10.14.
 */
public class DownloadService extends IntentService {
    XmlPullParser parser;
    URL url;
    static Handler handler;

    public DownloadService() {
        super("DownloadService");
    }

    void download() throws IOException, XmlPullParserException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream is = connection.getInputStream();
        parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Channel channel = new Channel(intent.getStringExtra("link"));
        Log.i("", "start service");
        try {
            url = new URL(channel.toString());
            download();
            ArrayList<Item> items = new Parser(parser).parse();
            if (items.isEmpty()) {
                throw new Exception();
            }

            for (int i = 0; i < items.size(); i++) {
                items.get(i).channel = channel.toString();
                Uri uri = Uri.parse("content://" + RSSContentProvider.AUTHORITY + "/" + DatabaseHelper.ITEMS_TABLE_NAME);
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.ITEMS_LINK, items.get(i).link);
                cv.put(DatabaseHelper.ITEMS_TITLE, items.get(i).title);
                cv.put(DatabaseHelper.ITEMS_DESCRIPTION, items.get(i).description);
                cv.put(DatabaseHelper.ITEMS_CHANNEL, items.get(i).channel);
                cv.put(DatabaseHelper.ITEMS_TIME, Long.toString(items.get(i).time));
                Cursor cursor = getContentResolver().query(uri, null, DatabaseHelper.ITEMS_LINK + " = \"" + items.get(i).link + "\"", null, null);
                if (cursor.getCount() == 0) {
                    Uri u = getContentResolver().insert(uri, cv);
                    Log.i("", u.toString());
                } else {
                    Log.i("", "already was");
                }
                cursor.close();
            }
        } catch (Exception e) {
            // bad url
            handler.obtainMessage(1).sendToTarget();
        }
        Log.i("", "end service");
    }
}
