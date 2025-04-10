package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.uiview.protocols.SingleIndexViewCreationHandler;
import com.prometheussoftware.auikit.uiview.protocols.UIStackedViewProtocol;

import java.util.ArrayList;
import java.util.Map;

public class UIStackedViews <T extends UIView> extends UIView implements UIStackedViewProtocol {

    private ArrayList<T> views;

    public UIStackedViews(int count, SingleIndexViewCreationHandler<T> handler) {
        this(count, 0, handler);
    }

    public UIStackedViews(int count, int padding, SingleIndexViewCreationHandler<T> handler) {
        this(count, padding, padding, handler);
    }

    public UIStackedViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, SingleIndexViewCreationHandler<T> handler) {
        this(count, interItemSpacing, horizontalMargin, verticalMargin, null, handler);
    }

    public UIStackedViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, Map<Integer, Integer> sizes, SingleIndexViewCreationHandler<T> handler) {
        super();
        initViewsWithCount(count, handler);
        constraintViews(sizes, interItemSpacing, horizontalMargin, verticalMargin);
        applyConstraints();
    }

    public UIStackedViews(int count, int padding, int interItemMargin, SingleIndexViewCreationHandler<T> handler) {
        super();
        initViewsWithCount(count, handler);
        constraintViews(padding, interItemMargin);
        applyConstraints();
    }

    private void initViewsWithCount(int count, SingleIndexViewCreationHandler<T> handler) {
        views = new ArrayList<>();

        if (handler == null) return;

        for (int i = 0; i < count; i++) {
            T view = handler.createView(i);
            if (view == null) continue;

            addSubView(view);
            this.views.add(view);
        }
    }

    public UIStackedViews(ArrayList<SingleIndexViewCreationHandler<T>> handlers) {
        super();

        this.views = new ArrayList<>();

        for (int i = 0; i < handlers.size(); i++) {
            T view = handlers.get(i).createView(0);
            if (view == null) continue;

            addSubView(view);
            this.views.add(view);
        }

        constraintViews();
    }

    public static float defaultPadding() {
        return 4.0f;
    }

    public T viewAtIndex(int index) {
        if (index >= views.size()) return null;
        return views.get(index);
    }

    public ArrayList<T> contentViews() {
        return views;
    }

    public int count() {
        return views.size();
    }

    @Override
    public void constraintViews(Map<Integer, Integer> sizes, int interItemSpacing, int horizontalMargin, int verticalMargin) {
    }

    @Override
    public void constraintViews(int interItemSpacing, int horizontalMargin, int verticalMargin) {
        constraintViews(null, interItemSpacing, horizontalMargin, verticalMargin);
    }

    @Override
    public void constraintViews(int padding, int interItemMargin) {
    }

    @Override
    public void constraintViews(int padding) {
        constraintViews(padding, padding);
    }

    @Override
    public void constraintViews() {
        constraintViews(0, 0);
    }
}
