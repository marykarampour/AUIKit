package com.prometheussoftware.auikit.tableview.collapsing;

import com.prometheussoftware.auikit.tableview.UITableViewController;

public class CollapsingTableViewController extends UITableViewController<CollapsingTableViewContentController> {

    @Override protected void createContentController() {
        setContentController(new CollapsingTableViewContentController(view()));
    }
}

