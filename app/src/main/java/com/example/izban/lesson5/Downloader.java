package com.example.izban.lesson5;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
public class Downloader extends AsyncTask<URL, Void, Void> {
    Context context;
    ArrayAdapter<Item> adapter;
    XmlPullParser parser;
    ListView lv;
    boolean failed;
    URL url;

    Downloader(Context context, ListView lv) {
        this.context = context;
        this.lv = lv;
        this.adapter = new ArrayAdapter<Item>(context, android.R.layout.simple_list_item_1);
        this.failed = false;
    }

    void download() throws IOException, XmlPullParserException {
        //URL url = new URL("http://bash.im/rss/");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream is = connection.getInputStream();
        parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);
    }

    @Override
    protected Void doInBackground(URL... params) {
        this.url = params[0];
        Log.i("", "START");
        try {
            download();
            //adapter.clear();
            ArrayList<Item> items = new Parser(parser).parse();
            if (items.isEmpty()) {
                throw new Exception();
            }

            for (int i = 0; i < items.size(); i++) {
                Uri uri = Uri.parse("content://" + RSSContentProvider.AUTHORITY + "/" + DatabaseHelper.ITEMS_TABLE_NAME);
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.ITEMS_LINK, items.get(i).link);
                cv.put(DatabaseHelper.ITEMS_TITLE, items.get(i).title);
                cv.put(DatabaseHelper.ITEMS_DESCRIPTION, items.get(i).description);
                if (context.getContentResolver().query(uri, null, "link = \"" + items.get(i).link + "\"", null, null).getCount() == 0) {
                    Uri u = context.getContentResolver().insert(uri, cv);
                    Log.i("", u.toString());
                } else {
                    Log.i("", "already was");
                }
            }
        } catch (Exception e) {
            adapter.clear();
            failed = true;
            Log.i("", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (failed) {
            Toast.makeText(context, "network error or not RSS page", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("", "OK");
        }

    }
}
