package com.example.izban.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class ChannelActivity extends Activity {
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
}
