package com.miniapp.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.home.activities.MainActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_about_us)
public class AboutUsFragment extends Fragment {

    @App
    MiniApp miniApp;

    MainActivity mainActivity;
    @ViewById(R.id.webview)
    WebView aboutwebView;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @AfterViews
    void init() {
        mainActivity = (MainActivity) this.getActivity();
        WebSettings webSettings = aboutwebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        aboutwebView.loadUrl("http://www.productionmini.com/mini/about.php");
        aboutwebView.setWebViewClient(new WebViewClient() {
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

    public boolean canWebviewGoBack() {
        if (aboutwebView.canGoBack()) {
            aboutwebView.goBack();
            return true;
        } else {
            return false;
        }
    }
}
