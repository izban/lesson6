package com.example.izban.lesson5;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class ChannelActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView lv;
    ArrayAdapter<Channel> adapter;
    //RSSContentProvider provider = new RSSContentProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        lv = (ListView)findViewById(R.id.listView);
        final Intent intent = new Intent(this, RSSActivity.class);
        adapter = new ArrayAdapter<Channel>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Channel channel = ((ArrayAdapter<Channel>)parent.getAdapter()).getItem(position);
                intent.putExtra(DatabaseHelper.CHANNELS_TITLE, channel.title);
                intent.putExtra(DatabaseHelper.CHANNELS_LINK, channel.link);
                startActivity(intent);
            }
        });
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonAddChannelClick(View view) {
        TextView text = (TextView)findViewById(R.id.editTextChannel);
        String s = text.getText().toString();
        text.setText("");

        try {
            new URL(s);
        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "invalid url", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse("content://" + RSSContentProvider.AUTHORITY + "/" + DatabaseHelper.CHANNELS_TABLE_NAME);
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.CHANNELS_TITLE, s);
        cv.put(DatabaseHelper.CHANNELS_LINK, s);
        Uri u = getContentResolver().insert(uri, cv);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri base = Uri.parse("content://" + RSSContentProvider.AUTHORITY);
        Uri uri = Uri.withAppendedPath(base, DatabaseHelper.CHANNELS_TABLE_NAME);
        Log.i("", "start: " + uri.toString());
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (adapter == null) {
            adapter = new ArrayAdapter<Channel>(this, android.R.layout.simple_list_item_1);
        }
        adapter.clear();
        while (data.moveToNext()) {
            adapter.add(DatabaseHelper.getChannel(data));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter = null;
    }

    public void onButtonClearAllClick(View view) {
        Uri uri = Uri.parse("content://" + RSSContentProvider.AUTHORITY + "/" + DatabaseHelper.CHANNELS_TABLE_NAME);
        getContentResolver().delete(uri, null, null);
        uri = Uri.parse("content://" + RSSContentProvider.AUTHORITY + "/" + DatabaseHelper.ITEMS_TABLE_NAME);
        getContentResolver().delete(uri, null, null);
    }
}
