package com.prometheussoftware.auikit.uiview;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import com.prometheussoftware.auikit.utility.ViewUtility;

public class UIScrollview extends UIView {

    private DIRECTION direction;
    private UIView contentView;
    private ViewGroup scrollView;

    public UIScrollview(DIRECTION direction) {
        super();
        this.direction = direction;
        init();
    }

    public UIScrollview() {
        this(UIView.DIRECTION.VERTICAL);
    }

    @Override
    public void initView() {
        super.initView();

        contentView = new UIView();
        scrollView = viewWithType(direction);
    }

    private ViewGroup viewWithType(DIRECTION direction) {
        switch (direction) {
            case VERTICAL: {
                ScrollView view = new ScrollView(getActivity());
                view.setFillViewport(true);
                view.setScrollContainer(false);
                return view;
            }
            default: {
                HorizontalScrollView view = new HorizontalScrollView(getActivity());
                view.setFillViewport(true);
                view.setScrollContainer(false);
                return view;
            }
        }
    }

    @Override
    public void loadView() {
        super.loadView();
        ViewUtility.addViewWithID(scrollView, this);
        ViewUtility.addViewWithID(contentView, scrollView);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        contentView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
    }

    public void addContentSubview(UIView view) {
        contentView.addSubview(view);
    }

    public UIView getContentView() {
        return contentView;
    }
}
