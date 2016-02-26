package com.egnese.eggwallet.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.egnese.eggwallet.R;
import com.egnese.eggwallet.ui.TypefaceSpan;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by adityaagrawal on 24/12/15.
 */
public class LoadURLActivity extends ActionBarActivity {
    @InjectView(R.id.progressLoading)
    MaterialProgressBar progressLoading;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.webView)
    WebView webView;
    private String url = "", title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_policies);
        populate();
    }

    private void populate() {
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getInt("action") == 1) {
            title = "Terms of Service";
            url = "http://www.google.com";
        } else if (bundle.getInt("action") == 2) {
            title = "Privacy Policy";
            url = "http://www.facebook.com";
        } else if (bundle.getInt("action") == 3) {
            title = "Content Policy";
            url = "http://www.yahoo.com";
        }

        SpannableString s = new SpannableString(title);
        s.setSpan(new TypefaceSpan(this, "LatoLatin-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((android.widget.TextView) toolbar.findViewById(R.id.toolbarTitle)).setText(s);
       /* toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_cancel_mtrl_am_alpha_dark);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        getSupportActionBar().setTitle("");
        webView.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(progress == 100)
                    progressLoading.setVisibility(View.GONE);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("file:///android_asset/error_403.html");
                progressLoading.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(url);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start_application_dark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_cancel) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}