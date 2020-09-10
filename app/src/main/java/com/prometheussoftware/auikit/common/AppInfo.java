package com.prometheussoftware.auikit.common;

import android.content.Context;

import com.prometheussoftware.auikit.networking.ServerController;

public class AppInfo extends MainAppInfo {

    @Override
    public void initializeInstances(Context context) { }

    @Override
    public Constants initializeConstants() {
        return new EXConstants();
    }

    @Override
    public ServerController initializeServer() {
        return null;
    }

    @Override
    public AppTheme initializeAppTheme() {
        return new EXAppTheme();
    }

}
