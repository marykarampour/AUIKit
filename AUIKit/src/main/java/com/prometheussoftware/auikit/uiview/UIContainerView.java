package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.uiview.protocols.ViewCreation;

public class UIContainerView <V extends UIView> extends UIView {

    private V view;
    private UIEdgeInsets insets = new UIEdgeInsets();
    private ViewCreation<V> viewCreationHandler;

    //** @param handler Returns a UIVIew that will be added and constrainted to self. */
    public UIContainerView(UIEdgeInsets insets, ViewCreation<V> viewCreationHandler) {
        super();
        this.insets = insets;
        view = viewCreationHandler.view();
    }

    //** @param handler Returns a UIVIew that will be added and constrainted to self. */
    public UIContainerView(ViewCreation<V> viewCreationHandler) {
        super();
        view = viewCreationHandler.view();
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        setContentViewForSuperview(view, this, insets, () -> {
            view.removeFromSuperview();
        });
    }
}
