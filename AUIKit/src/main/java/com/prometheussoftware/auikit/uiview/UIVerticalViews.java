package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.uiview.protocols.ViewCreation;

import java.util.ArrayList;
import java.util.Map;

public class UIVerticalViews <T extends UIView> extends UIStackedViews<T> {

    public UIVerticalViews(int count, ViewCreation<T> handler) {
        super(count, handler);
    }

    public UIVerticalViews(int count, int padding, ViewCreation<T> handler) {
        super(count, padding, handler);
    }

    public UIVerticalViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, ViewCreation<T> handler) {
        super(count, interItemSpacing, horizontalMargin, verticalMargin, handler);
    }

    public UIVerticalViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, Map<Integer, Integer> sizes, ViewCreation<T> handler) {
        super(count, interItemSpacing, horizontalMargin, verticalMargin, sizes, handler);
    }

    public UIVerticalViews(int count, int padding, int interItemMargin, ViewCreation<T> handler) {
        super(count, padding, interItemMargin, handler);
    }

    public UIVerticalViews(ArrayList<ViewCreation<T>> handlers) {
        super(handlers);
    }

    @Override
    public void constraintViews(Map<Integer, Integer> sizes, int interItemSpacing, int horizontalMargin, int verticalMargin) {
        boolean isSizesEmpty = (sizes == null || sizes.isEmpty());
        constraintVertically(contentViews(), interItemSpacing, horizontalMargin, verticalMargin, isSizesEmpty);

        if (sizes != null) {
            for (Map.Entry<Integer, Integer> entry : sizes.entrySet()) {
                int index = entry.getKey();
                int height = entry.getValue();

                if (index < contentViews().size() && height > 0) {
                    constraintHeightForView(contentViews().get(index), height);
                }
            }
        }
    }

    @Override
    public void constraintViews(int padding, int interItemMargin) {
        constraintViews(interItemMargin, padding, padding);
    }
}
