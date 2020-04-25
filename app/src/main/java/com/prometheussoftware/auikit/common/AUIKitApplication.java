package com.prometheussoftware.auikit.common;

import android.app.Application;

import com.prometheussoftware.auikit.uiview.UIView;

public class AUIKitApplication extends MainApplication {

    private static BaseWindow window;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInstances();
    }

    protected void initializeInstances () {
    }

    public static void setWindow(BaseWindow baseWindow) {
        window = baseWindow;
        UIView.setWindow(window);
    }

    public static BaseWindow getWindow() {
        return window;
    }
}
