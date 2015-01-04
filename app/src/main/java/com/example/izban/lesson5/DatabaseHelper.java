package com.example.izban.lesson5;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by izban on 03.01.15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String NAME = "data";
    public static final int ver = 1;

    public static final String CHANNELS_TABLE_NAME = "channels";
    public static final String CHANNELS_ID = "_ID";
    public static final String CHANNELS_TITLE = "title";
    public static final String CHANNELS_LINK = "link";
    public static final String CHANNELS_CREATE =
            "CREATE TABLE " + CHANNELS_TABLE_NAME + " (" +
            CHANNELS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CHANNELS_TITLE + " TEXT, " +
            CHANNELS_LINK + " TEXT)";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context) {
        super(context, CHANNELS_TABLE_NAME, null, ver);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CHANNELS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CHANNELS_TABLE_NAME);
        onCreate(db);
    }

    static public Channel getChannel(Cursor cursor) {
        return new Channel(cursor.getString(cursor.getColumnIndex(CHANNELS_TITLE)),
                           cursor.getString(cursor.getColumnIndex(CHANNELS_LINK)));
    }
}
