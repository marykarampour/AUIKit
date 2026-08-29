package com.prometheussoftware.auikit.uiviewcontroller;

import java.util.List;

public interface NavBarButtonContainerProtocol {

    List<NavBarButtonTargetProtocol> navBarTargets();
    /** @brief It sets self as the owner of the navbar. Call this starting from the inner most VC, each time this is called it is reset.
    The desirable effect is to have the ouer most container VC own the navbar. Call in outer most container. */
    void addNavBarTarget (NavBarButtonTargetProtocol object);
    /** @brief Call in outer most container. It calls setNavBarItems. */
    void setNavBarItemsOfTarget (NavBarButtonTargetProtocol object);
    /** @brief Call in outer most container. It calls setNavBarItems. */
    void setNavBarItemsOfTargetAtIndex (int index);
    /** @brief Call in outer most container. */

    /** @brief Calls addNavBarTarget and setNavBarItemsOfTarget on self. Call this to set navbar items. */
    void setAsNavBarTarget();

    /** @brief Clears the navbar and calls setAsNavBarTarget. */
    void resetBarButtons();

    /** @brief Clears the navbar and calls setBarButtonsOfTarget. */
    void resetBarButtonsOfTarget (NavBarButtonTargetProtocol object);

    /** @brief Calls addNavBarTarget and setNavBarItemsOfTarget on object. Call this to set navbar items. */
    void setBarButtonsOfTarget (NavBarButtonTargetProtocol object);

    /** @brief Clears the navbar and calls setAsNavBarTarget on all targets. */
    void resetBarButtonsOfTargets();

    /** @brief Clalls setBarButtonsOfTarget on all targets. */
    void setBarButtonsOfTargets();

    /** @brief Calls setAsNavBarTarget of viewControllerContainingNavigationBar on self as target. */
    void setBarButtonsOfViewControllerContainingNavigationBar();

    /** @brief Clears the navbar and calls setAsNavBarTarget of viewControllerContainingNavigationBar on self as target. */
    void resetBarButtonsOfViewControllerContainingNavigationBar();
}
