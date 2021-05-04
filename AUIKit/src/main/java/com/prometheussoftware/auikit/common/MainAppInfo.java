package com.prometheussoftware.auikit.common;

import com.prometheussoftware.auikit.common.protocols.AppInfoProtocol;

public abstract class MainAppInfo implements AppInfoProtocol {

    @Override
    public Assets initializeAssets() {
        return new Assets();
    }

    @Override
    public Constants initializeConstants() {
        return null;
    }

    @Override
    public AppTheme initializeAppTheme() {
        return new AppTheme();
    }
}
