package com.prometheussoftware.auikit.tableview;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prometheussoftware.auikit.uiview.UISingleView;

public class UITableView extends UISingleView <RecyclerView> {

    private LinearLayoutManager layoutManager;
    private int orientation = RecyclerView.VERTICAL;

    public UITableView() {
        super();
        init();
    }

    public UITableView(int ori) {
        super();
        orientation = ori;
        init();
    }

    @Override public void initView() {
        super.initView();

        layoutManager = new LinearLayoutManager(getWindow());
        layoutManager.setOrientation(orientation);

        view = new RecyclerView(getWindow());
        view.setLayoutManager(layoutManager);
        view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
    }

    public LinearLayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void disableRecycling(int viewType) {
        view.getRecycledViewPool().setMaxRecycledViews(viewType, 0);
    }
}
