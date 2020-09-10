package com.prometheussoftware.auikit.common;

import com.prometheussoftware.auikit.uiviewcontroller.UINavigationController;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.viewcontrollers.ExampleTableViewController;

public class MainWindow extends BaseWindow {

    @Override
    protected UIViewController createRootViewController() {

        ExampleTableViewController vc = new ExampleTableViewController();
        UINavigationController nav = new UINavigationController(vc);
        return nav;
    }
}
