package com.example.pingliu.webjson;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by pingliu on 2018/3/7.
 */

public class TextActivity extends AppCompatActivity {
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 設定這個頁面XML的layout名稱
        setContentView(R.layout.activity_text);

        // 設定要顯示回上一頁的按鈕
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 取得 Intent 附帶的資料，改成文章網址存為 url
        Bundle args = this.getIntent().getExtras();
        String url = "http://disp.cc/b/" + args.getString("bi") + "-" + args.getString("ti");
        initWebView();

        // 載入網頁
        mWebView.loadUrl(url);

    }
    private void initWebView(){
        // 取得XML中的WebView
        mWebView = (WebView) findViewById(R.id.webview);

        // WebView的設定選項
        WebSettings webSettings = mWebView.getSettings();
        // Enable Javascript
        webSettings.setJavaScriptEnabled(true);
        // Enable LocalStorage
        webSettings.setDomStorageEnabled(true);

        // 要加setWebViewClient以避免點連結時跳出APP用瀏覽器開啟
        mWebView.setWebViewClient(new WebViewClient());

        // 要設定 WebChromeClient 才能支援 JS 的 Alert, Confirm
        mWebView.setWebChromeClient(new WebChromeClient());

        // API 19 以上可使用硬體加速
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        // 設定背景為黑色，避免載入網頁前顯示白色
        mWebView.setBackgroundColor(Color.BLACK);
    }
    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

