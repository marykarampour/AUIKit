package com.prometheussoftware.auikit.uiviewcontroller;

public interface NavigationControllerProtocol {

    /** If the object is not a UIViewController its navigation controller's
     * implementation will be executed */
    void pushViewController(UIViewController viewController, boolean animated);

    /** If the object is not a UIViewController its navigation controller's
     * implementation will be executed */
    Navigation.Node<UIViewController> popViewControllerAnimated(boolean animated);

    /** If the object is not a UIViewController its navigation controller's
     * implementation will be executed */
    Navigation.Tree<UIViewController> popToViewController(UIViewController viewController, boolean animated);

    /** If the object is not a UIViewController its navigation controller's
     * implementation will be executed */
    void popToRootViewController(boolean animated);

    /** If the object is not a UIViewController its navigation controller's
     * implementation will be executed */
    void popToViewControllerAtIndex(int index, boolean animated);
}
