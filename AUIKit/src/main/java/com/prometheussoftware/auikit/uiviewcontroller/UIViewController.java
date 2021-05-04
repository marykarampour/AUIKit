package com.prometheussoftware.auikit.uiviewcontroller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;

import com.prometheussoftware.auikit.callback.CompletionCallback;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.BaseWindow;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.uiview.UIButton;
import com.prometheussoftware.auikit.uiview.UINavigationBar;
import com.prometheussoftware.auikit.uiview.UITransitioningContainerView;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;

public class UIViewController <V extends UIView> extends BaseModel implements LifeCycleDelegate.ViewController, LifeCycleDelegate.View, NavigationControllerProtocol {

    static {
        BaseModel.Register(UIViewController.class);
    }

    private V view;

    /** The view controller that presented this view controller (or its farthest ancestor.) */
    private UIViewController presentingViewController;

    /** If this view controller is a child of a containing view controller
     * (e.g. a navigation controller) this is the
     * containing view controller. */
    private UIViewController parentViewController;

    private ArrayList<UIViewController> childViewControllers = new ArrayList<>();

    /** If this view controller has been pushed onto a navigation controller, return it. */
    private UINavigationController navigationController;

    /** Created on-demand so that a view controller may customize its navigation appearance. */
    private UINavigationBar navigationBar;

    private Handler animationHandler = new Handler();

    private boolean animated;

    public int animationDuration = UITransitioningContainerView.LAYOUT_LOAD_WAIT_DURATION;

    private String title;

    public UIViewController() {
        createDefaultNavigationBar();
    }

    /** Add this to your custom constructors or call it after default constructor
     *
     {@code
     new UIViewController().init();
     }
     */
    public UIViewController init() {
        setWindow(window());
        return this;
    }

    /** The completion handler, if provided, will be invoked after the presented
     controllers viewDidAppear: callback is invoked. */
    public void presentViewController(UIViewController viewControllerToPresent,
                                      Navigation.TRANSITION_ANIMATION animation,
                                      CompletionCallback completion) {

        UIViewController presentingParent = presentingParent();
        viewControllerToPresent.setPresentingViewController(presentingParent);
        animated = animation != Navigation.TRANSITION_ANIMATION.NONE;
        window().presentVisibleViewController(viewControllerToPresent, animation, completion);
    }

    /** The completion handler, if provided, will be invoked after the dismissed controller's viewDidDisappear: callback is invoked. */
    public void dismissViewController(Navigation.TRANSITION_ANIMATION animation,
                                      CompletionCallback completion) {

        animated = animation != Navigation.TRANSITION_ANIMATION.NONE;
        UIViewController presentingViewController = getPresentingViewController();
        if (presentingViewController != null) {
            window().dismissVisibleViewController(presentingViewController, animation, completion);
        }
    }

    //region life cycle

    @Override public void loadView() {
        setView((V)new UIView());
        view().setBackgroundColor(Color.WHITE);
    }

    @Override public void viewWillUnload() {
    }

    @Override public void viewDidUnload() {
        for (UIViewController child : childViewControllers) {
            child.setView(null);
        }
    }

    @Override public void viewDidLoad() {
        for (UIViewController child : childViewControllers) {
            child.viewDidLoad();
        }
    }

    @Override public void viewWillAppear(boolean animated) {
    }

    @Override public void viewDidAppear(boolean animated) {
    }

    @Override public void viewWillDisappear(boolean animated) {
    }

    @Override public void viewDidDisappear(boolean animated) {
    }

    //endregion

    //region load/unload and view life cycle

    public V getView() {
        return view;
    }

    public void setView(V view) {
        if (view == null) {
            viewWillUnload();
        }
        this.view = view;
        if (view == null) {
            viewDidUnload();
        }
        if (view != null) {
            this.view.setLifeCycleDelegate(this);
        }
    }

    /** Loads the view if null and returns the loaded view */
    public V view() {
        loadViewIfRequired();
        return view;
    }

    private void loadViewIfRequired() {
        if (view == null) {
            loadView();
            viewDidLoad();
        }
    }

    public void setWindow(BaseWindow window) {

        setChildrensWindow(window);
        if (window == null) return;
    }

    private void setChildrensWindow(BaseWindow window) {
        for (UIViewController child : childViewControllers) {
            child.setWindow(window);
        }
    }

    @Override
    public void viewWillBeLoaded() {

        if (animated) {
            animationHandler.postDelayed(() -> {
                viewWillAppear(true);
                animationHandler.postDelayed(() -> {
                    viewDidAppear(true);
                }, animationDuration);
            }, 2*animationDuration);
        }
        else {
            viewWillAppear(false);
        }
    }

    @Override
    public void viewIsLoaded() {
        if (!animated) {
            viewDidAppear(false);
        }
    }

    @Override
    public void viewWillBeUnloaded() {

        if (animated) {
            animationHandler.postDelayed(() -> {
                viewWillDisappear(true);
                animationHandler.postDelayed(() -> {
                    viewDidDisappear(true);
                }, 2*animationDuration);
            }, 2*animationDuration);
        }
        else {
            viewWillDisappear(false);
        }
    }

    @Override
    public void viewIsUnloaded() {
        if (!animated) {
            viewDidDisappear(false);
        }
    }

    //endregion

    //region orientation

    public void setAnimated(boolean animated) {
        this.animated = animated;
        for (UIViewController vc : childViewControllers) {
            vc.setAnimated(animated);
        }
    }

    public boolean shouldAutorotate() {
        return false;
    }

    public int supportedInterfaceOrientations() {
        return BaseWindow.ORIENTATION.PORTRAIT.intValue() | BaseWindow.ORIENTATION.LANDSCAPE.intValue();
    }

    public BaseWindow.ORIENTATION preferredInterfaceOrientationForPresentation() {
        return BaseWindow.ORIENTATION.PORTRAIT;
    }

    //endregion


    //region container

    /** If the child controller has a different parent controller, it will first be removed
     * from its current parent by calling removeFromParentViewController.
     * If this method is overridden then the super implementation must be called. */
    protected void addChildViewController (UIViewController childController, boolean animated) {

        if (childController == null) return;

        childController.removeFromParentViewController();
        childController.setParentViewController(this);
        childViewControllers.add(childController);
    }

    protected void addChildViewController (UIViewController childController) {
        addChildViewController(childController, false);
    }

    /** If the child controller has a different parent controller, it will first be removed
     * from its current parent by calling removeFromParentViewController.
     * If this method is overridden then the super implementation must be called. */
    protected void addChildViewController (UIViewController childController, int index, boolean animated) {

        if (childController == null) return;

        childController.removeFromParentViewController();
        childController.setParentViewController(this);
        ArrayUtility.safeReplace(childViewControllers, index, childController);
    }

    protected void addChildViewController (UIViewController childController, int index) {
        addChildViewController(childController, index, false);
    }

    protected void removeChildViewController (UIViewController childController) {

        if (childController == null) return;

        childController.setParentViewController(null);
        childController.setWindow(null);
        childViewControllers.remove(childController);
    }

    protected void removeChildViewController (int index) {

        UIViewController childController = ArrayUtility.safeGet(childViewControllers, index);
        if (childController == null) return;

        childController.setParentViewController(null);
        childController.setWindow(null);
        childViewControllers.remove(childController);
    }

    protected void clearChildViewControllers () {

        for (UIViewController vc : childViewControllers) {
            clearChildViewController(vc);
        }
        childViewControllers.clear();
    }

    protected void clearChildViewControllersExcept (UIViewController child, boolean animated) {

        for (UIViewController vc : childViewControllers) {
            if (vc != child) {
                vc.setAnimated(animated);
                clearChildViewController(vc);
            }
        }
        childViewControllers.clear();
    }

    private void clearChildViewController (UIViewController vc) {

        if (vc == null) return;
        vc.setParentViewController(null);
        vc.setWindow(null);
        UIView vi = getView();
        if (vi != null) {
            vi.removeView(vc.getView());
        }
    }

    /** Removes the the receiver from its parent's children controllers array.
     * If this method is overridden then the super implementation must be called. */
    protected void removeFromParentViewController() {
        if (parentViewController != null) parentViewController.childViewControllers.remove(this);
    }

    public void setPresentingViewController(UIViewController presentingViewController) {
        this.presentingViewController = presentingViewController;
    }

    private UIViewController presentingParent() {

        UIViewController parent = this;
        while (parent != null) {
            UIViewController vc = parent.parentViewController;
            if (vc == null) {
                return parent;
            }
            parent = vc;
        }
        return parent;
    }

    //endregion

    //region navigation

    public void setNavigationController(UINavigationController navigationController) {
        this.navigationController = navigationController;
        for (UIViewController vc : childViewControllers) {
            vc.setNavigationController(navigationController);
        }
    }

    /** Returns the navigation controller which this view is pushed into.
     * If that's null, and presenting parent is navigation controller,
     * returns presenting parent.
     * If that's null returns presenting parent's navigation controller.
     * Otherwise returns this if it is a navigation controller.
     * If everything fails, it returns null. */
    public UINavigationController getNavigationController() {

        if (navigationController != null) {
            return navigationController;
        }

        UIViewController presentingParent = presentingParent();
        if (presentingParent != null) {

            if (presentingParent instanceof UINavigationController)
                return (UINavigationController) presentingParent;
            else {
                UINavigationController nav = presentingParent.getNavigationController();
                if (nav != null) return nav;
            }
        }
        else if (this instanceof UINavigationController) {
            return (UINavigationController) this;
        }
        return null;
    }

    @Override public void pushViewController(UIViewController viewController, boolean animated) {
        if (navigationController != null) {
            navigationController.pushViewController(viewController, animated);
        }
    }

    @Override public Navigation.Node<UIViewController> popViewControllerAnimated(boolean animated) {
        if (navigationController != null) {
            navigationController.popViewControllerAnimated(animated);
        }
        return null;
    }

    @Override public Navigation.Tree<UIViewController> popToViewController(UIViewController viewController, boolean animated) {
        if (navigationController != null) {
            navigationController.popToViewController(viewController, animated);
        }
        return null;
    }

    @Override public void popToRootViewController(boolean animated) {
        if (navigationController != null) {
            navigationController.popToRootViewController(animated);
        }
    }

    @Override public void popToViewControllerAtIndex(int index, boolean animated) {
        if (navigationController != null) {
            navigationController.popToViewControllerAtIndex(index, animated);
        }
    }

    private void setParentViewController(UIViewController parentViewController) {
        this.parentViewController = parentViewController;
    }

    //endregion

    //region parents and container helpers

    public UIViewController getPresentingViewController() {

        if (presentingViewController != null) {
            return presentingViewController;
        }

        UIViewController presentingParent = presentingParent();
        if (presentingParent != null && presentingParent != this) {
            return presentingParent.getPresentingViewController();
        }
        return null;
    }

    public UIViewController getParentViewController() {
        return parentViewController;
    }

    public ArrayList<UIViewController> getChildViewControllers() {
        return childViewControllers;
    }

    /** Call in or after viewWillAppear, getNavigationController will return null if called too soon */
    public void setTitle(String title) {
        this.title = title;
        navigationBar.setTitle(title);
    }

    /** Call in or after viewWillAppear, getNavigationController will return null if called too soon */
    public void setRightNavItemHidden(boolean hidden) {
        navigationBar.rightBarButtonItem().setHidden(hidden);
    }

    /** Call in or after viewWillAppear, getNavigationController will return null if called too soon */
    public void setLeftNavItemHidden(boolean hidden) {
        navigationBar.leftBarButtonItem().setHidden(hidden);
    }

    /** Call in or after viewWillAppear, getNavigationController will return null if called too soon */
    public UIButton getRightNavItem() {
        return navigationBar.rightBarButtonItem();
    }

    /** Call in or after viewWillAppear, getNavigationController will return null if called too soon */
    public UIButton getLeftNavItem() {
        return navigationBar.leftBarButtonItem();
    }

    public String getTitle() {
        return title;
    }

    private void createDefaultNavigationBar() {
        navigationBar = new UINavigationBar();
        navigationBar.setShadowTintColor(App.theme().Nav_Bar_Background_Color());
    }

    public UINavigationBar getNavigationBar() {
        return navigationBar;
    }

    //endregion

    //region window helpers

    public BaseWindow window() {
        return UIView.getWindow();
    }

    public void runOnUiThread(Runnable run) {
        if (window() != null)
            window().runOnUiThread(run);
    }

    public void endEditing() {
        window().dismissKeyboard();
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if (window() != null) {
            window().startActivityForResult(intent, requestCode);
        }
    }

    /** Call this in or after loadView to set a ResultDelegate object */
    public void setResultDelegate(BaseWindow.ResultDelegate delegate) {
        if (delegate != null && window() != null) {
            window().resultDelegate = delegate;
        }
    }

    //endregion
}
