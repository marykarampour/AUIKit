package com.prometheussoftware.auikit.tableview.collapsing;

import com.prometheussoftware.auikit.tableview.UITableViewController;

public class CollapsingTableViewController extends UITableViewController <CollapsingTableViewDataController, CollapsingTableViewContentController> {

    @Override
    protected CollapsingTableViewContentController createContentController(CollapsingTableViewDataController dataController) {
        return new CollapsingTableViewContentController(view(), dataController);
    }

    @Override
    protected CollapsingTableViewDataController createDataController() {
        return new CollapsingTableViewDataController();
    }

    @Override public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        contentController.reloadData();
    }
}

