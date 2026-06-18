package com.prometheussoftware.auikit.uiview;

import android.graphics.Rect;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.uiview.protocols.ViewCreation;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;
import java.util.Map;

public class UIHorizontalScrollingViews <T extends UIView> extends UIView {

    private UIScrollview scrollview;
    private UIHorizontalViews views;
    private int estimatedContentWidth;

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
        estimatedContentWidth = ArrayUtility.sum(new ArrayList<>(sizes.values()));
        this.views = new UIHorizontalViews(count, interItemSpacing, horizontalMargin, verticalMargin, sizes, handler);
        init();
    }

    public UIHorizontalScrollingViews(int count, int padding, int interItemMargin, ViewCreation<T> handler) {
        super();
        this.views = new UIHorizontalViews(count, padding, interItemMargin, handler);
        init();
    }

    public UIHorizontalScrollingViews(ArrayList<ViewCreation<T>> handlers) {
        super();
        this.views = new UIHorizontalViews(handlers);
        init();
    }

    @Override
    public void setFrame(Rect frame) {
        Rect rect = new Rect(frame);
        if (frame.width() == 0)
            rect.right = rect.left + estimatedContentWidth;
        super.setFrame(rect);

        scrollview.getContentView().constraintWidthForView(views, rect.width());
        scrollview.getContentView().constraintHeightForView(views, rect.height());
        scrollview.getContentView().applyConstraints();
    }

    @Override
    public void initView() {
        super.initView();
        scrollview = new UIScrollview(DIRECTION.HORIZONTAL);
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
