package com.example.izban.lesson5;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by izban on 03.01.15.
 */
public class RSSContentProvider extends ContentProvider {
    public static final String AUTHORITY = RSSContentProvider.class.getName();//"RSSContentProvider";

    private DatabaseHelper helper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.CHANNELS_TABLE_NAME, 0);
        sUriMatcher.addURI(AUTHORITY, DatabaseHelper.ITEMS_TABLE_NAME, 1);
    }

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case 0:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "_ID ASC";
                }
                sqLiteQueryBuilder.setTables(DatabaseHelper.CHANNELS_TABLE_NAME);
                break;
            case 1:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "time DESC";
                }
                sqLiteQueryBuilder.setTables(DatabaseHelper.ITEMS_TABLE_NAME);
                break;
            default:
                return null;
        }
        Cursor cursor = sqLiteQueryBuilder.query(helper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case 0:
                return DatabaseHelper.CHANNELS_TABLE_NAME;
            case 1:
                return DatabaseHelper.ITEMS_TABLE_NAME;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long _id;
        switch (sUriMatcher.match(uri)) {
            case 0:
                _id = helper.getWritableDatabase().insert(DatabaseHelper.CHANNELS_TABLE_NAME, null, values);
                break;
            case 1:
                _id = helper.getWritableDatabase().insert(DatabaseHelper.ITEMS_TABLE_NAME, null, values);
                break;
            default:
                return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, Long.toString(_id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int _id;
        switch (sUriMatcher.match(uri)) {
            case 0:
                _id = helper.getWritableDatabase().delete(DatabaseHelper.CHANNELS_TABLE_NAME, selection, selectionArgs);
                break;
            case 1:
                _id = helper.getWritableDatabase().delete(DatabaseHelper.ITEMS_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return _id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int _id;
        switch (sUriMatcher.match(uri)) {
            case 0:
                _id = helper.getWritableDatabase().update(DatabaseHelper.CHANNELS_TABLE_NAME, values, selection, selectionArgs);
                break;
            case 1:
                _id = helper.getWritableDatabase().update(DatabaseHelper.ITEMS_TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return _id;
    }
}
