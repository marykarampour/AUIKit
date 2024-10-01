package com.prometheussoftware.auikit.common;

import android.content.Context;

import com.prometheussoftware.auikit.networking.ServerController;

public class App {

    private static Constants constantsInstance;

    private static ServerController serverInstance;

    private static AppTheme appThemeInstance;

    private static Assets assetsInstance;

    private static MainAppInfo info;

    public static Constants constants() {
        return constantsInstance;
    }

    public static ServerController server() {
        return serverInstance;
    }

    public static AppTheme theme() {
        return appThemeInstance;
    }

    public static Assets assets() {
        return assetsInstance;
    }

    /** Call this in or after setWindow of your subclass of BaseActivity */
    public static void initializeInstances(Context context) {
        info.initializeInstances(context);
    }

    /** This should be called first in Application class */
    public static void initInfo(MainAppInfo main) {
        info = main;
        constantsInstance = info.initializeConstants();
        assetsInstance = info.initializeAssets();
        appThemeInstance = info.initializeAppTheme();

        serverInstance = info.initializeServer();
        if (serverInstance != null) serverInstance.initialize();
    }
}
