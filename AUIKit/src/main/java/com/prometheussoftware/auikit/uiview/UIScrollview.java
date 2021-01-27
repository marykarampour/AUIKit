package com.prometheussoftware.auikit.uiview;

import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.prometheussoftware.auikit.utility.ConstraintUtility;
import com.prometheussoftware.auikit.utility.ViewUtility;

public class UIScrollview extends UIView {

    private UIEnum.DIRECTION direction;
    private UIView contentView;
    private ViewGroup scrollView;

    public UIScrollview(UIEnum.DIRECTION direction) {
        super();
        this.direction = direction;
        init();
    }

    public UIScrollview() {
        super();
        this.direction = UIEnum.DIRECTION.VERTICAL;
        init();
    }

    @Override public void initView() {
        super.initView();

        contentView = new UIView();
        scrollView = viewWithType(direction);
    }

    private ViewGroup viewWithType(UIEnum.DIRECTION direction) {
        switch (direction) {
            case VERTICAL: {

                ScrollView view = new ScrollView(getActivity());
                view.setFillViewport(false);
                view.setScrollContainer(false);
                return view;
            }
            default: {
                HorizontalScrollView view = new HorizontalScrollView(getActivity());
                view.setFillViewport(false);
                view.setScrollContainer(false);
                return view;
            }
        }
    }

    @Override
    public void loadView() {
        super.loadView();
        ViewUtility.addViewWithID(contentView, this.scrollView);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();
        ConstraintUtility.constraintSidesForView(constraintSet, scrollView);
        applyConstraints();
    }

    public void addContentSubView(UIView view) {
        contentView.addSubView(view);
    }

    public UIView getContentView() {
        return contentView;
    }
}
