package com.linked_sys.maktabi.activities;


import android.os.Bundle;
import android.os.Handler;

import com.linked_sys.maktabi.R;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int SPLASH_TIME_OUT = 0;
        if (session.isLoggedIn()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getUserData();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    session.checkLogin();
                    finish();
                }
            }, 1000);
        }
    }
    @Override
    protected int getLayoutID() {
        return R.layout.splash_activity;
    }
}
