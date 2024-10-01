package com.prometheussoftware.auikit.genericviews;

import com.prometheussoftware.auikit.uiview.UISingleLayerView;
import com.prometheussoftware.auikit.uiview.UIView;

/** Class groups an array of views of the same type vertically.
 * It also has utilities for layering including round corners and border */
public class UIGroupedLayerView <V extends UIView> extends UISingleLayerView <UIGroupedView<V>> {

    @Override
    protected void createView() {
        setView(new UIGroupedView<>());
    }
}
