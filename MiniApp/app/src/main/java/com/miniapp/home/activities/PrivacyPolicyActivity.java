package com.miniapp.home.activities;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.home.fragments.AboutUsFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_privacy_policy)
public class PrivacyPolicyActivity extends AppCompatActivity {

    @App
    MiniApp miniApp;
    @ViewById(R.id.webview)
    WebView policywebView;

    @AfterViews
    void init() {
        miniApp.setActivity(this);
        WebSettings webSettings = policywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        policywebView.loadUrl("http://www.productionmini.com/mini/privacy.php");
        policywebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//               miniApp.showProgress(mainActivity);
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
//                miniApp.hideProgress();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (policywebView.canGoBack()) {
            policywebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
