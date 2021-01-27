package com.prometheussoftware.auikit.uiview;

import android.view.View;

import com.prometheussoftware.auikit.utility.ConstraintUtility;
import com.prometheussoftware.auikit.utility.ViewUtility;

/** Suitable for creating subclasses of UIView with a single Android view */
public class UISingleView <V extends View> extends UIView {

    protected V view;

    public UISingleView() {
        super();
    }

    @Override public void initView() {
        super.initView();
        view = (V)new View(getActivity());
    }

    @Override
    public void loadView() {
        super.loadView();
        ViewUtility.addViewWithID(view, this);
    }

    @Override public void constraintLayout() {
        super.constraintLayout();
        ConstraintUtility.constraintSidesForView(constraintSet, view);
        constraintSet.applyTo(this);
    }

    public V getView() {
        return view;
    }
}
