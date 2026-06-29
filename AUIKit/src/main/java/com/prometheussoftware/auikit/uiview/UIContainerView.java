package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.uiview.protocols.ViewCreation;

public class UIContainerView <V extends UIView> extends UIView {

    private V view;
    private UISingleLayerView layerView;
    private UIEdgeInsets insets = new UIEdgeInsets();

    //** @param handler Returns a UIVIew that will be added and constrainted to self. */
    public UIContainerView(UIEdgeInsets insets, ViewCreation<V> viewCreationHandler) {
        super();
        this.insets = insets;
        layerView = new UISingleLayerView<>();
        view = viewCreationHandler.view();
        layerView.setView(view);
    }

    //** @param handler Returns a UIVIew that will be added and constrainted to self. */
    public UIContainerView(ViewCreation<V> viewCreationHandler) {
        super();
        layerView = new UISingleLayerView<>();
        view = viewCreationHandler.view();
        layerView.setView(view);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        setContentViewForSuperview(layerView, this, insets, () -> {
            view.removeFromSuperview();
        });
    }

    public void setCornerRadius(int radius) {
        layerView.setCornerRadius(radius);
    }

    public void setViewBackgroundColor(UIColor color) {
        view.setBackgroundColor(color);
    }
}
