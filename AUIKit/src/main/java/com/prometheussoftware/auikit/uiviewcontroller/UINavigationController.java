package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.container.ChildViewController;
import com.prometheussoftware.auikit.container.UIHeaderFooterContainerViewController;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.uiview.UIBarButton;
import com.prometheussoftware.auikit.uiview.UINavigationBar;
import com.prometheussoftware.auikit.uiview.UITransitioningContainerView;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.utility.ArrayUtility;

public class UINavigationController extends UIHeaderFooterContainerViewController <UIView, UIView, UITransitioningContainerView, ChildViewController> {

    static {
        BaseModel.Register(UINavigationController.class);
    }

    private Navigation.Tree<UIViewController> navigationStack = new Navigation.Tree();

    private Navigation.Node<UIViewController> visibleViewController;

    /** This is the navigationBar of visibleViewController.
     * When a view controller is pushed, its navigationBar becomes
     * currentNavigationBar */
    private UINavigationBar currentNavigationBar;

    private UIViewController rootViewController;

    public UINavigationController(UIViewController rootViewController) {
        super();
        init();
        this.rootViewController = rootViewController;
    }

    public UIViewController getRootViewController() {
        return rootViewController;
    }

    private void setRootViewController(UIViewController rootViewController) {
        setTopViewController(rootViewController, true, false, true);
    }

    private void setTopViewController(UIViewController viewController, boolean root, boolean animated, boolean commit) {
        addChildViewController(viewController, animated);
        addNavigationNode(viewController, root);
        if (commit) {
            setVisibleViewController(false, animated);
        }
    }

    public void pushViewController(UIViewController viewController, boolean animated) {
        setTopViewController(viewController, false, animated, true);
    }

    private void pushViewController(UIViewController viewController, boolean animated, boolean commit) {
        setTopViewController(viewController, false, animated, commit);
    }

    public Navigation.Node<UIViewController> popViewControllerAnimated(boolean animated) {
        return popViewControllerAnimated(animated, null);
    }

    private Navigation.Node<UIViewController> popViewControllerAnimated(boolean animated, Navigation.Node<UIViewController> vc) {

        if (navigationStack.getNodes().size() < 2) return navigationStack.bottomNode();
        boolean commit = vc == null;
        if (commit) vc = visibleViewController;

        removeChildViewController(vc.getNodeObject());
        vc.setNodeClass(null);
        navigationStack.getNodes().remove(vc);
        addChildViewController(navigationStack.bottomNode().getNodeObject(), animated);
        if (commit) {
            setVisibleViewController(true, animated);
        }
        return navigationStack.bottomNode();
    }

    public Navigation.Tree<UIViewController> popToViewController(
            UIViewController viewController, boolean animated) {

        if (1 < navigationStack.getNodes().size()) {

            Navigation.Node<UIViewController> node = ArrayUtility.lastObject(navigationStack.getNodes());

            for (int i = navigationStack.getNodes().size() - 1; 0 <= i;) {

                if (node == null || node.getNodeObject().equals(viewController)) break;
                node = navigationStack.getNodes().get(--i);
                popViewControllerAnimated(animated && node != null, navigationStack.bottomNode());
            }
        }
        setVisibleViewController(true, animated);
        return navigationStack;
    }

    public void popToRootViewController(boolean animated) {

        if (navigationStack.getNodes().size() < 2) return;

        clearChildViewControllers();
        Navigation.Node<UIViewController> first = navigationStack.getNodes().get(0);
        navigationStack.getNodes().clear();
        setRootViewController(first.getNodeObject());
    }

    public void popToViewControllerAtIndex(int index, boolean animated) {

        if (navigationStack.getNodes().size() < 2) return;

        if (index < navigationStack.getNodes().size()) {
            Navigation.Node<UIViewController> presentingView = navigationStack.getNodes().get(index);
            if (presentingView != null) {
                popToViewController(presentingView.getNodeObject(), animated);
            }
        }
    }

    public void replaceViewControllerAtIndex(UIViewController viewController, int index, boolean animated) {
        addChildViewController(viewController, index, animated);
        replaceNavigationNode(viewController, index,false);
    }

    public void addViewControllerAtIndex(UIViewController viewController, boolean root) {
        addChildViewController(viewController);
        addNavigationNode(viewController, root);
    }

    /** Hide or show the navigation bar. If animated, it will transition vertically. */
    public void setNavigationBarHidden(boolean hidden, boolean animated) {
    }

    //region helpers

    public Navigation.Node<UIViewController> getVisibleViewController() {
        return visibleViewController;
    }

    public Navigation.Tree<UIViewController> getNavigationStack() {
        return navigationStack;
    }

    public Navigation.Tree<UIViewController> clonedNavigationStack() {
        Navigation.Tree<UIViewController> stack = BaseModel.copy(navigationStack);
        return stack;
    }

    public int indexOfNavigationNode(Navigation.Node<UIViewController> node) {
        return navigationStack.getNodes().indexOf(node);
    }

    public void setNavigationStack(Navigation.Tree<UIViewController> navigationStack, boolean animated) {

        for (int i=0; i < navigationStack.getNodes().size(); i++) {

            Navigation.Node<UIViewController> node = navigationStack.getNodes().get(i);
            Navigation.Node<UIViewController> existingNode = ArrayUtility.safeGet(this.navigationStack.getNodes(), i);
            UIViewController vc = node.getNodeObject();

            if (vc != null) {
                if (existingNode == null) {
                    if (i == navigationStack.getNodes().size() - 1) {
                        pushViewController(vc, false, false);
                    }
                    else {
                        addViewControllerAtIndex(vc, i==0);
                    }
                }
                else if (existingNode.getNodeClass() != node.getNodeClass()) {
                    replaceViewControllerAtIndex(vc, i, false);
                }
            }
        }

        int count = this.navigationStack.getNodes().size();
        boolean isDismiss = count < navigationStack.getNodes().size();
        boolean isSame = count == navigationStack.getNodes().size();

        for (int i = navigationStack.getNodes().size(); i < count; i++) {
            popViewControllerAnimated(false, this.navigationStack.bottomNode());
        }
        setVisibleViewController(isDismiss, animated && !isSame);
    }

    private void addNavigationNode(UIViewController viewController, boolean isRoot) {
        if (viewController == null) return;

        Navigation.Node<UIViewController> node = Navigation.Tree.node(viewController, isRoot);
        navigationStack.getNodes().add(node);
    }

    private void replaceNavigationNode(UIViewController viewController, int index, boolean isRoot) {
        if (viewController == null) return;

        Navigation.Node<UIViewController> node = Navigation.Tree.node(viewController, isRoot);
        ArrayUtility.safeReplace(navigationStack.getNodes(), index, node);
    }

    //endregion

    //region view

    private void setVisibleViewController(boolean isDismiss, boolean animated) {

        final Navigation.TRANSITION_ANIMATION animation =
                !animated ? Navigation.TRANSITION_ANIMATION.NONE :
                        (isDismiss ? Navigation.TRANSITION_ANIMATION.LEFTRIGHT : Navigation.TRANSITION_ANIMATION.RIGHTLEFT);

        Navigation.Node<UIViewController> bottom = navigationStack.bottomNode();
        setCurrentNavigationBar(bottom.getNodeObject().getNavigationBar());

        if (bottom != null) {
            bottom.getNodeObject().setAnimated(animated);
            contentView.setCurrentContentView(bottom.getNodeObject().view(), () -> {

                visibleViewController = bottom;
                visibleViewController.getNodeObject().setNavigationController(this);
                contentView.applyTransitionAnimation(isDismiss, animation, () -> { });
                currentNavigationBar.setTitle(visibleViewController.getNodeObject().getTitle());
            });
        }
    }

    public boolean backBarButtonItemIsHidden() {
        return navigationStack.hasASingleNode() || navigationStack.hasNoNode();
    }

    public void updateHeaderView() {
        boolean hidden = navigationStack.getNodes().size() < 2;
        setHeaderExpanded(!hidden, true, null);
    }

    protected void dismiss() {
        dismissViewController(Navigation.TRANSITION_ANIMATION.LEFTRIGHT, null);
    }

    @Override public int headerHeight() {
        return App.constants().Nav_Bar_Height() + App.constants().Status_Bar_Height();
    }

    public void setCurrentNavigationBar(UINavigationBar currentNavigationBar) {
        this.currentNavigationBar = currentNavigationBar;

        headerView.removeAllViews();
        headerView.addSubView(currentNavigationBar);
        headerView.constraintSidesForView(currentNavigationBar);
        headerView.applyConstraints();

        if (1 < navigationStack.getNodes().size()) {

            UIBarButton leftButton = UINavigationBar.barButtonItem(App.assets().Left_Chevron_Image());
            currentNavigationBar.setLeftBarButtonItem(leftButton);
            currentNavigationBar.leftBarButtonItem().addTouchUpTarget(this, v -> {
                if (navigationStack.getNodes().size() < 2) {
                    dismiss();
                }
                else {
                    popViewControllerAnimated(true);
                }
            });
        }
    }

    @Override public void createChildVC() {
    }

    @Override public void setContentView() {
        contentView = new UITransitioningContainerView();
    }

    @Override public int animationDuration() {
        return 200;
    }

    //endregion

    //region UIViewController

    @Override protected void addChildViewController(UIViewController childController, boolean animated) {
        clearChildViewControllersExcept(childViewController, animated);
        super.addChildViewController(childController, animated);
    }

    @Override public void viewDidLoad() {
        super.viewDidLoad();
        if (rootViewController.getParentViewController() == null) {
            setRootViewController(rootViewController);
        }
    }

    //endregion

    //region navigation

    public Navigation.Node<UIViewController> getTopViewController() {
        return visibleViewController;
    }

    //endregion
}
