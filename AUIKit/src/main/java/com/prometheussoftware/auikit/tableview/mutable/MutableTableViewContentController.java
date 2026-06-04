package com.prometheussoftware.auikit.tableview.mutable;

import com.prometheussoftware.auikit.tableview.UITableViewContentController;
import com.prometheussoftware.auikit.tableview.UITableViewDataController;

public class MutableTableViewContentController extends UITableViewContentController <MutableTableViewDataController> {
    public MutableTableViewContentController(MutableTableViewDataController dataController) {
        super(dataController);
    }
}
