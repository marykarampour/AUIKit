package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.uiview.protocols.SingleIndexViewCreationHandler;

import java.util.ArrayList;
import java.util.Map;

public class UIHorizontalViews <T extends UIView> extends UIStackedViews<T> {
    public UIHorizontalViews(int count, SingleIndexViewCreationHandler<T> handler) {
        super(count, handler);
    }

    public UIHorizontalViews(int count, int padding, SingleIndexViewCreationHandler<T> handler) {
        super(count, padding, handler);
    }

    public UIHorizontalViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, SingleIndexViewCreationHandler<T> handler) {
        super(count, interItemSpacing, horizontalMargin, verticalMargin, handler);
    }

    public UIHorizontalViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, Map<Integer, Integer> sizes, SingleIndexViewCreationHandler<T> handler) {
        super(count, interItemSpacing, horizontalMargin, verticalMargin, sizes, handler);
    }

    public UIHorizontalViews(int count, int padding, int interItemMargin, SingleIndexViewCreationHandler<T> handler) {
        super(count, padding, interItemMargin, handler);
    }

    public UIHorizontalViews(ArrayList<SingleIndexViewCreationHandler<T>> singleIndexViewCreationHandlers) {
        super(singleIndexViewCreationHandlers);
    }

    @Override
    public void constraintViews(Map<Integer, Integer> sizes, int interItemSpacing, int horizontalMargin, int verticalMargin) {
        boolean isSizesEmpty = (sizes == null || sizes.isEmpty());
        constraintHorizontally(contentViews(), interItemSpacing, horizontalMargin, verticalMargin, isSizesEmpty);

        if (sizes != null) {
            for (Map.Entry<Integer, Integer> entry : sizes.entrySet()) {
                int index = entry.getKey();
                int width = entry.getValue();

                if (index < contentViews().size() && width > 0) {
                    constraintWidthForView(contentViews().get(index), width);
                }
            }
        }
    }

    @Override
    public void constraintViews(int padding, int interItemMargin) {
        constraintViews(interItemMargin, padding, padding);
    }
}
