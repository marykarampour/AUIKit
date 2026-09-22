package com.prometheussoftware.auikit.uiview;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class UIRefreshView extends UISingleView <SwipeRefreshLayout> {

    public UIRefreshView() {
        super();
    }

    @Override public void initView() {
        super.initView();
        view = new SwipeRefreshLayout(getActivity());
    }
}
