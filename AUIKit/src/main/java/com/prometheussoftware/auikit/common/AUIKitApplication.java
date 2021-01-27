package com.prometheussoftware.auikit.common;

import com.prometheussoftware.auikit.common.protocols.ApplicationProtocol;
import com.prometheussoftware.auikit.uiview.UIView;

public abstract class AUIKitApplication extends MainApplication implements ApplicationProtocol {

    private static BaseWindow window;

    @Override
    public void onCreate() {
        super.onCreate();
        initAppInfo();
        initializeInstances();
    }

    protected void initializeInstances () {
    }

    public static void setWindow(BaseWindow baseWindow) {
        window = baseWindow;
        UIView.setWindow(window);
    }

    public static BaseWindow getWindow() {
        return AUIKitApplication.window;
    }

    /** Initialize an instance of MainAppInfo here */
    private void initAppInfo() {
        App.initInfo(appInfo());
    }
}
