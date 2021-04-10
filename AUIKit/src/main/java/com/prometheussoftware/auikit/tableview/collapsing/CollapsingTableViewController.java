package com.prometheussoftware.auikit.tableview.collapsing;

import com.prometheussoftware.auikit.tableview.UITableViewController;

public class CollapsingTableViewController extends UITableViewController<CollapsingTableViewContentController> {

    @Override protected void createContentController() {
        setContentController(new CollapsingTableViewContentController(view()));
    }

    @Override public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        contentController.reloadData();
    }
}

