package com.example.izban.lesson5;

import android.app.Activity;
import android.app.LoaderManager;
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
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class RSSActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{
    ListView lv;
    ArrayAdapter<Item> adapter;
    String channel;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        final Intent intent = new Intent(this, WebActivity.class);
        adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1);
        lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item it = ((ArrayAdapter<Item>)parent.getAdapter()).getItem(position);
                intent.putExtra("url", it.link);
                startActivity(intent);
            }
        });
        try {
            channel = getIntent().getStringExtra("link");
            url = new URL(channel);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "invalid channel", Toast.LENGTH_SHORT).show();
            finish();
        }
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    public void onButton1Click(View view) {
        new Downloader(this, lv, channel).execute(url);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri base = Uri.parse("content://" + RSSContentProvider.AUTHORITY);
        Uri uri = Uri.withAppendedPath(base, DatabaseHelper.ITEMS_TABLE_NAME);
        Log.i("", "start: " + uri.toString());
        String selection = DatabaseHelper.ITEMS_CHANNEL + " = \"" + channel + "\"";
        Log.i("", selection);
        //selection = null;
        return new CursorLoader(this, uri, null, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (adapter == null) {
            adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1);
        }
        adapter.clear();
        Log.i("", Integer.toString(data.getCount()));
        while (data.moveToNext()) {
            Item item = DatabaseHelper.getItem(data);
            //Log.i("", item.channel);
            adapter.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter = null;
    }
}
