package com.joesmith.devlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class About extends AppCompatActivity {

    WebView webView;
    public String fileName = "about.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

// init webView
        webView=(WebView)

                findViewById(R.id.webview);
// displaying content in WebView from html file that's stored in assets folder
        webView.getSettings().
                setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/"+fileName); }

}
