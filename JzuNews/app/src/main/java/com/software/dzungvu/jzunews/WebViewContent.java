package com.software.dzungvu.jzunews;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class WebViewContent extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private String link;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_content);

        Bundle getData = getIntent().getExtras();
        if (getData == null) {
            link = null;
        } else {
            link = getData.getString("URLNEWS");
        }


        webView = findViewById(R.id.wvNews);
        progressBar = findViewById(R.id.progressbar);

        webView.setWebViewClient(new myWebClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(link);

        Toolbar toolbar = findViewById(R.id.detail_page_tool_bar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuBack_detail:
                this.finish();
                return true;
            case R.id.mnuSetting_detail:
                Toast.makeText(WebViewContent.this, "Go to setting", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void ShareOnFB(View view) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(link))
                .build();
        ShareDialog.show(this, content);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(link);
            return true;
        }


    }
}
