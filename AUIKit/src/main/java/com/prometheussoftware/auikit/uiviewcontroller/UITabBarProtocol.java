package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.uiview.UITabBar;

public interface UITabBarProtocol {

    /** called when a new view is selected by the user (but not programatically) */
    void tabBarDidSelectItem (UITabBar tabBar, UITabBarItem item);


    interface Item {
        /** Automatically created lazily with the
         * view controller's title if it's not set explicitly. */
        UITabBarItem tabBarItem();
        /** If the view controller has a tab bar controller
         * as its ancestor, return it. Returns nil otherwise. */
        UITabBarController tabBarController();
    }

    interface Controller {
    }
}
