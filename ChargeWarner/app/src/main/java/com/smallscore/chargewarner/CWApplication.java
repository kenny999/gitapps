package com.smallscore.chargewarner;

import android.app.Application;

public class CWApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Logic.onAppStarted(getApplicationContext());
    }
}