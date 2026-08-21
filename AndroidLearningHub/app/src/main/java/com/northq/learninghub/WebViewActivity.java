package com.northq.learninghub;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        WebView webView = findViewById(R.id.helpWebView);
        ProgressBar progress = findViewById(R.id.webProgress);
        ImageButton btnBack = findViewById(R.id.webBack);
        ImageButton btnForward = findViewById(R.id.webForward);
        ImageButton btnRefresh = findViewById(R.id.webRefresh);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progress.setVisibility(View.GONE);
                btnBack.setAlpha(view.canGoBack() ? 1.0f : 0.3f);
                btnForward.setAlpha(view.canGoForward() ? 1.0f : 0.3f);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress.setProgress(newProgress);
            }
        });

        btnBack.setOnClickListener(v -> { if (webView.canGoBack()) webView.goBack(); });
        btnForward.setOnClickListener(v -> { if (webView.canGoForward()) webView.goForward(); });
        btnRefresh.setOnClickListener(v -> webView.reload());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/help.html");
    }
}
