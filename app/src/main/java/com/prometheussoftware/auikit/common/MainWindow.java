package com.prometheussoftware.auikit.common;

import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.viewcontrollers.ExampleViewController;

public class MainWindow extends BaseWindow {

    @Override
    protected UIViewController createRootViewController() {
        ExampleViewController vc = new ExampleViewController();
        return vc;
    }
}
