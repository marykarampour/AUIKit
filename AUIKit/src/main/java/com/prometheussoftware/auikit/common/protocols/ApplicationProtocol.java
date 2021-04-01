package com.prometheussoftware.auikit.common.protocols;

import com.prometheussoftware.auikit.common.MainAppInfo;

public interface ApplicationProtocol {
    /** Initialize and return an instance of MainAppInfo here
     * to be consumed by your custom subclass of AUIKitApplication */
    MainAppInfo appInfo();
}
