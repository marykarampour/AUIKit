package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.uiview.protocols.SingleIndexViewCreationHandler;

import java.util.ArrayList;
import java.util.Map;

public class UIVerticalViews extends UIStackedViews {

    public UIVerticalViews(int count, SingleIndexViewCreationHandler handler) {
        super(count, handler);
    }

    public UIVerticalViews(int count, int padding, SingleIndexViewCreationHandler handler) {
        super(count, padding, handler);
    }

    public UIVerticalViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, SingleIndexViewCreationHandler handler) {
        super(count, interItemSpacing, horizontalMargin, verticalMargin, handler);
    }

    public UIVerticalViews(int count, int interItemSpacing, int horizontalMargin, int verticalMargin, Map<Integer, Integer> sizes, SingleIndexViewCreationHandler handler) {
        super(count, interItemSpacing, horizontalMargin, verticalMargin, sizes, handler);
    }

    public UIVerticalViews(int count, int padding, int interItemMargin, SingleIndexViewCreationHandler handler) {
        super(count, padding, interItemMargin, handler);
    }

    public UIVerticalViews(ArrayList<SingleIndexViewCreationHandler> handlers) {
        super(handlers);
    }

    @Override
    public void constraintViewsWithSizes(Map<Integer, Integer> sizes, int interItemSpacing, int horizontalMargin, int verticalMargin) {
        constraintVertically(contentViews(), interItemSpacing, horizontalMargin, verticalMargin, sizes.isEmpty());

       for (Map.Entry<Integer, Integer> entry : sizes.entrySet()) {
           int index = entry.getKey();
           int height = entry.getValue();

           if (index < contentViews().size() && height > 0) {
               constraintHeightForView(contentViews().get(index), height);
           }
       }
    }

    @Override
    public void constraintViewsWithPadding(int padding, int interItemMargin) {
        constraintViewsWithInterItemSpacing(interItemMargin, padding, padding);
    }

    public UIView viewAtIndex(int index) {
        return super.viewAtIndex(index);
    }
}
