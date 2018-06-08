package com.miniapp.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.miniapp.R;
import com.miniapp.application.MiniApp;
import com.miniapp.commonUtils.CommonUtility;
import com.miniapp.home.activities.MainActivity_;
import com.miniapp.login.activities.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @App
    MiniApp miniApp;

    @AfterViews
    void init() {
        hideStatusBar();

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        miniApp.setActivity(this);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i;
                if (CommonUtility.getCurrentUserToken(SplashActivity.this)!=null) {
                    i = new Intent(SplashActivity.this, MainActivity_.class);
                } else {
                    i = new Intent(SplashActivity.this, LoginActivity_.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
