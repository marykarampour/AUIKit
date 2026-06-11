package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.uiview.protocols.ViewCreation;

import java.util.ArrayList;
import java.util.Map;

public class UIHorizontalScrollingViews <T extends UIView> extends UIView {

    private UIScrollview scrollview = new UIScrollview();
    private UIHorizontalViews views;

    public UIHorizontalScrollingViews(int count, ViewCreation<T> handler) {
        this(count, 0, handler);
    }

    public UIHorizontalScrollingViews(int count, int padding, ViewCreation<T> handler) {
        this(count, padding, padding, handler);
    }

    public UIHorizontalScrollingViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, ViewCreation<T> handler) {
        this(count, interItemSpacing, horizontalMargin, verticalMargin, null, handler);
    }

    public UIHorizontalScrollingViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, Map<Integer, Integer> sizes, ViewCreation<T> handler) {
        super();
        this.views = new UIHorizontalViews(count, interItemSpacing, horizontalMargin, verticalMargin, sizes, handler);
    }

    public UIHorizontalScrollingViews(int count, int padding, int interItemMargin, ViewCreation<T> handler) {
        super();
        this.views = new UIHorizontalViews(count, padding, interItemMargin, handler);
    }

    public UIHorizontalScrollingViews(ArrayList<ViewCreation<T>> handlers) {
        super();
        this.views = new UIHorizontalViews(handlers);
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubview(scrollview);
        scrollview.getContentView().addSubview(views);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();
        setContentViewForSuperview(views, scrollview.getContentView(), new UIEdgeInsets(), () -> {});
        constraintSidesForView(scrollview);
        applyConstraints();
    }
}
