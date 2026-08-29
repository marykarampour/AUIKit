package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.uiview.UIBarButton;

import java.util.List;
import java.util.Map;

/** @brief Indicates whether the nav bar has a button.
 @note Put in the inner most view controller. */
public interface NavBarButtonTargetProtocol {

    /** @brief This is called by default when viewControllerContainingNavigationBar is set in UIViewController + ViewControllerNavigationBar.
    If other navbar setting functions need to be called, either call them explicitly, or implement this method and call them inside this.
     @code
     void setNavBarItems() {
        setMutableNavBarItems();
     }
     @endcode */
    default void setNavBarItems() {}

    /** @brief Indicates whether the nav bar has a button.
     @note Put in the inner most view controller. */
    default boolean hasButtonOfType (UIBarButton.TYPE type) { return false; }

    /** @brief If not implemented, the button will be positioned at right. */
    default UIBarButton.POSITION positionForButtonOfType (UIBarButton.TYPE type) {
        return UIBarButton.POSITION.RIGHT;
    }

    /** @brief title of the  button if type is not UIBarButton.TYPE type_IMAGE, otherwise it is the name of the image resource.
     @note Put in the inner most view controller. */
    default String titleForButtonOfType (UIBarButton.TYPE type) { return null; }

    default UIImage imageForButtonOfType (UIBarButton.TYPE type) { return null; }

    /** Passed to saveAction when savePressed. */
    default Object saveObject() { return null; }

    default boolean isEnabledButtonOfType (UIBarButton.TYPE type) { return false; }

    /** @brief Handles action performed when pressing the save button. Default is handleSavePressed in MKUMutableObjectTableViewController.
     @note Setting this property will make actionHandler nil. */
    default UITargetDelegate.TouchUp actionForButtonOfType (UIBarButton.TYPE type) { return null; }

    void addButtonOfType(UIBarButton.TYPE type, UIBarButton.POSITION position);

    UIViewController viewControllerContainingNavigationBar();

    void setViewControllerContainingNavigationBar(UIViewController viewControllerContainingNavigationBar);

    Map<UIBarButton.POSITION, List<UIBarButton>> getBarButtons();
}
