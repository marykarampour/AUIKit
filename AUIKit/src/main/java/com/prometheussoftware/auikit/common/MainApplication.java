package com.prometheussoftware.auikit.common;

import android.app.Application;
import android.content.Context;

public abstract class MainApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
