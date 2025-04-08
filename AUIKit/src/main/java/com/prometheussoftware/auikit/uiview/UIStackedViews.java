package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.uiview.protocols.SingleIndexViewCreationHandler;
import com.prometheussoftware.auikit.uiview.protocols.UIStackedViewProtocol;

import java.util.ArrayList;
import java.util.Map;

public class UIStackedViews <T extends UIView> extends UIView implements UIStackedViewProtocol {

    private ArrayList<T> views;

    public UIStackedViews(int count, SingleIndexViewCreationHandler handler) {
        this(count, 0, handler);
    }

    public UIStackedViews(int count, int padding, SingleIndexViewCreationHandler handler) {
        this(count, padding, padding, handler);
    }

    public UIStackedViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, SingleIndexViewCreationHandler handler) {
        this(count, interItemSpacing, horizontalMargin, verticalMargin, null, handler);
    }

    public UIStackedViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, Map<Integer, Integer> sizes, SingleIndexViewCreationHandler handler) {
        super();
        initViewsWithCount(count, handler);
        constraintViewsWithSizes(sizes, interItemSpacing, horizontalMargin, verticalMargin);
    }

    public UIStackedViews(int count, int padding, int interItemMargin, SingleIndexViewCreationHandler handler) {
        super();
        initViewsWithCount(count, handler);
        constraintViewsWithPadding(padding, interItemMargin);
    }

    private void initViewsWithCount(int count, SingleIndexViewCreationHandler handler) {
        views = new ArrayList<>();

        if (handler == null) return;

        for (int i = 0; i < count; i++) {
            T view = (T) handler.createView(i);
            if (view == null) continue;

            addSubView(view);
            this.views.add(view);
        }
    }

    public UIStackedViews(ArrayList<SingleIndexViewCreationHandler> handlers) {
        super();

        this.views = new ArrayList<>();

        for (int i = 0; i < handlers.size(); i++) {
            T view = (T) handlers.get(i).createView(0);
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
    public void constraintViewsWithSizes(Map<Integer, Integer> sizes, int interItemSpacing, int horizontalMargin, int verticalMargin) {
    }

    @Override
    public void constraintViewsWithInterItemSpacing(int interItemSpacing, int horizontalMargin, int verticalMargin) {
        constraintViewsWithSizes(null, interItemSpacing, horizontalMargin, verticalMargin);
    }

    @Override
    public void constraintViewsWithPadding(int padding, int interItemMargin) {
    }

    @Override
    public void constraintViewsWithPadding(int padding) {
        constraintViewsWithPadding(padding, padding);
    }

    @Override
    public void constraintViews() {
        constraintViewsWithPadding(0, 0);
    }
}
