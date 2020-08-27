package com.prometheussoftware.auikit.common.protocols;

import android.content.Context;

import com.prometheussoftware.auikit.common.AppTheme;
import com.prometheussoftware.auikit.common.Assets;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.networking.ServerController;

public interface AppInfoProtocol {

    void initializeInstances(Context context);
    Constants initializeConstants();
    ServerController initializeServer();
    AppTheme initializeAppTheme();
    Assets initializeAssets();
}
