package com.example.izban.lesson5;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ChannelActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView lv;
    ArrayAdapter<Channel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        lv = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<Channel>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(adapter);
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

    public void onButtonAddChannel(View view) {
        TextView text = (TextView)findViewById(R.id.editTextChannel);
        String s = text.getText().toString();
        text.setText("");
        adapter.add(new Channel(s, s));
        Log.d("", s + ": " + Integer.toString(adapter.getCount()));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri base = Uri.parse("content://" + RSSContentProvider.AUTHORITY);
        Uri uri = Uri.withAppendedPath(base, DatabaseHelper.CHANNELS_TABLE_NAME);
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
}
