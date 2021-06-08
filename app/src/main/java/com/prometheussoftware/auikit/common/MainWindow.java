package com.prometheussoftware.auikit.common;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.uiviewcontroller.UINavigationController;
import com.prometheussoftware.auikit.uiviewcontroller.UITabBarController;
import com.prometheussoftware.auikit.uiviewcontroller.UITabBarItem;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.viewcontrollers.ExampleTableViewController;
import com.prometheussoftware.auikit.viewcontrollers.ExampleViewController;

import java.util.ArrayList;

public class MainWindow extends BaseWindow {

    @Override
    protected UIViewController createRootViewController() {

        ArrayList<UIViewController> VCs = new ArrayList<>();

        ExampleTableViewController vc = new ExampleTableViewController();
        UINavigationController nav = new UINavigationController(vc);
        nav.setTabBarItem(UITabBarItem.build("Nav VC", App.assets().Details_Image(), App.assets().Details_Image()));
        VCs.add(nav);

        ExampleViewController ex = new ExampleViewController(UIColor.blue(1.0f));
        ex.setTabBarItem(UITabBarItem.build("Example VC", App.assets().X_Mark_Round_Image(), App.assets().Checkmark_Image()));
        VCs.add(ex);

        ExampleViewController ex0 = new ExampleViewController(UIColor.green(1.0f));
        ex0.setTabBarItem(UITabBarItem.build("Example VC 0", App.assets().Up_Chevron_Image(), App.assets().Down_Chevron_Image()));
        VCs.add(ex0);

        UITabBarController tabBarController = new UITabBarController();
        tabBarController.setViewControllers(VCs);

        return tabBarController;
    }
}
