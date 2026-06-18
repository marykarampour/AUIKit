package com.prometheussoftware.auikit.uiviewcontroller.mutable;

import com.prometheussoftware.auikit.tableview.UITableViewContentController;

public class MutableTableViewContentController extends UITableViewContentController <MutableTableViewDataController> {
    public MutableTableViewContentController(MutableTableViewDataController dataController) {
        super(dataController);
    }
}
