package com.example.izban.lesson5;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by izban on 05.01.15.
 */
public class ChannelService extends IntentService {
    public ChannelService() {
        super("ChannelService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("", "Channel service is started");
        String link = intent.getStringExtra("link");
        String action = intent.getStringExtra("action");
        if (action.equals("add")) {
            Uri uri = Uri.parse("content://" + RSSContentProvider.AUTHORITY + "/" + DatabaseHelper.CHANNELS_TABLE_NAME);
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.CHANNELS_LINK, link);
            getContentResolver().insert(uri, cv);
        } else if (action.equals("delete")) {
            Uri uri = Uri.parse("content://" + RSSContentProvider.AUTHORITY + "/" + DatabaseHelper.CHANNELS_TABLE_NAME);
            getContentResolver().delete(uri, DatabaseHelper.CHANNELS_LINK + " = \"" + link + "\"", null);
            uri = Uri.parse("content://" + RSSContentProvider.AUTHORITY + "/" + DatabaseHelper.ITEMS_TABLE_NAME);
            getContentResolver().delete(uri, DatabaseHelper.ITEMS_CHANNEL + " = \"" + link + "\"", null);
        }
        Log.i("", "Channel service is over");
    }
}
