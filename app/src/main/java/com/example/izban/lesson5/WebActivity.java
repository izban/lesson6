package com.example.izban.lesson5;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class WebActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebView webView = (WebView)findViewById(R.id.webView);
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}
