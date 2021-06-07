package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.container.ChildViewController;
import com.prometheussoftware.auikit.container.UIHeaderFooterContainerViewController;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.uiview.UITabBar;
import com.prometheussoftware.auikit.uiview.UITransitioningContainerView;
import com.prometheussoftware.auikit.uiview.UIView;

import java.util.ArrayList;

public class UITabBarController extends UIHeaderFooterContainerViewController<UIView, UIView, UITransitioningContainerView, ChildViewController> implements UITabBarProtocol {

    static {
        BaseModel.Register(UITabBarController.class);
    }

    private Navigation.Tree<UIViewController> navigationStack = new Navigation.Tree();

    private Navigation.Node<UIViewController> selectedViewController;
    private int selectedIndex;

    private UITabBar tabBar;

    private UITabBarProtocol.Controller delegate;


    public UITabBarController() {
        super();
        init();
    }

    public void setViewControllers(ArrayList<UIViewController> viewControllers) {
        navigationStack.getNodes().clear();

        for (UIViewController vc : viewControllers) {

            Navigation.Node<UIViewController> node = new Navigation.Node<>();
            node.setNodeObject(vc);
            navigationStack.getNodes().add(node);
        }
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        tabBar.selectItemAtIndex(selectedIndex);
    }

    // region navigation

    @Override
    public void tabBarDidSelectItem(UITabBar tabBar, UITabBarItem item) {
        setSelectedViewController(item, true);
    }


    //endregion

    //region view

    private void setSelectedViewController(Navigation.Node<UIViewController> node, boolean animated) {
        if (node == null || node.getNodeObject() == null || node.equals(selectedViewController)) return;

        final Navigation.TRANSITION_ANIMATION animation =
                !animated ? Navigation.TRANSITION_ANIMATION.NONE : Navigation.TRANSITION_ANIMATION.FADE;

        node.getNodeObject().setAnimated(animated);
        contentView.setCurrentContentView(node.getNodeObject().view(), () -> {
            selectedViewController = node;
            selectedIndex = navigationStack.getNodes().indexOf(node);
            contentView.applyTransitionAnimation(false, animation, () -> { });
        });
    }

    private void setSelectedViewController(int index, boolean animated) {
        if (index < 0 || navigationStack.getNodes().size() <= index) return;
        Navigation.Node<UIViewController> node = navigationStack.getNodes().get(index);
        setSelectedViewController(node, animated);
    }

    private void setSelectedViewController(UIViewController vc, boolean animated) {
        if (vc == null) return;
        Navigation.Node<UIViewController> node = navigationStack.findNode(vc);
        setSelectedViewController(node, animated);
    }

    private void setSelectedViewController(UITabBarItem item, boolean animated) {
        if (item == null) return;
        Navigation.Node<UIViewController> node = nodeForTabBarItems(item);
        setSelectedViewController(node, animated);
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
        return 0;
    }

    @Override
    public int footerHeight() {
        return App.constants().Tab_Bar_Height();
    }

    @Override
    public void createFooterView() {
        super.createFooterView();

        tabBar = new UITabBar();
        footerView.addSubView(tabBar);
        footerView.constraintSidesForView(tabBar);
        footerView.applyConstraints();
        tabBar.setDelegate(this);
        tabBar.setItems(tabBarItems());
    }

    private ArrayList<UITabBarItem> tabBarItems() {

        ArrayList<UITabBarItem> arr = new ArrayList<>();

        for (Navigation.Node<UIViewController> node : navigationStack.getNodes()) {
            UITabBarItem item = node.getNodeObject().tabBarItem();
            if (item != null) arr.add(item);
        }
        return arr;
    }

    private UIViewController viewControllerForTabBarItems(UITabBarItem item) {

        Navigation.Node<UIViewController> node = nodeForTabBarItems(item);
        if (node == null) return node.getNodeObject();
        return null;
    }

    private Navigation.Node<UIViewController> nodeForTabBarItems(UITabBarItem item) {
        if (item == null) return null;

        for (Navigation.Node<UIViewController> node : navigationStack.getNodes()) {
            if (item.equals(node.getNodeObject().tabBarItem()))
                return node;
        }
        return null;
    }

    @Override
    protected void constraintViews() {
        super.constraintViews();
    }

    @Override public void createChildVC() {
    }

    @Override public void setContentView() {
        contentView = new UITransitioningContainerView();
    }

    @Override public int animationDuration() {
        return 200;
    }

    public UITabBar getTabBar() {
        return tabBar;
    }

    //endregion
}
