package com.paix.invenocoverage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webviewActivity extends Activity {
    WebView myWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        init();
    }

    private void init() {
        myWebView = (WebView)findViewById(R.id.webView);
        myWebView.loadUrl("http://192.168.9.126:8088/code_coverage/help.aspx");
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                try {
                    view.stopLoading();
                } catch (Exception e) {

                }
                try {
                    view.clearView();
                } catch (Exception e) {

                }
//                view.loadUrl("about:blank");
                view.loadUrl("file:///android_asset/error.html");
            }

        });
    }


}
