package com.prometheussoftware.auikit.uiview;

import android.view.View;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.utility.ConstraintUtility;
import com.prometheussoftware.auikit.utility.ViewUtility;

/** Suitable for creating subclasses of UIView with a single Android view */
public class UISingleView <V extends View> extends UIView {

    protected V view;

    public UISingleView() {
        super();
        init();
    }

    public UISingleView(V view) {
        super();
        setView(view);
    }

    @Override
    public void initView() {
        super.initView();
        createView();
    }

    protected void createView() {
        setView((V)new View(getActivity()));
    }

    @Override
    public void loadView() {
        super.loadView();
        ViewUtility.addViewWithID(view, this);
    }

    @Override public void constraintLayout() {
        super.constraintLayout();
        constraintSet.clear(view.getId());
        ConstraintUtility.constraintSidesForView(constraintSet, view, insets());
        constraintSet.applyTo(this);
    }

    public void setView(V view) {
        this.view = view;
        loadView();
        constraintLayout();
    }

    public V getView() {
        return view;
    }

    protected UIEdgeInsets insets() {
        return new UIEdgeInsets();
    }
}
