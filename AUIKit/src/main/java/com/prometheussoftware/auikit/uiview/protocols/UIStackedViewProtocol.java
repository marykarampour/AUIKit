package com.prometheussoftware.auikit.uiview.protocols;

import java.util.Map;

public interface UIStackedViewProtocol {

    /**
     * @param sizes The key is the index of the view and the value is the width or height, whichever applies.
     *              Only used if the corresponding value isn't zero.
     */
    void constraintViewsWithSizes(Map<Integer, Integer> sizes, int interItemSpacing, int horizontalMargin, int verticalMargin);

    /** Views are sized equally here. */
    void constraintViewsWithInterItemSpacing(int interItemSpacing, int horizontalMargin, int verticalMargin);

    void constraintViewsWithPadding(int padding, int interItemMargin);

    void constraintViewsWithPadding(int padding);

    void constraintViews();
}
