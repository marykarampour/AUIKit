package com.prometheussoftware.auikit.tableview.collapsing;

import com.prometheussoftware.auikit.tableview.UITableViewContentController;
import com.prometheussoftware.auikit.uiview.UIView;

public class CollapsingTableViewContentController extends UITableViewContentController <CollapsingTableViewDataController> {

    public CollapsingTableViewContentController(UIView view, CollapsingTableViewDataController dataController) {
        super(view, dataController);
    }

    @Override
    public <T extends CollapsingTableViewDataController> void setDataController(T dataController) {
        super.setDataController(dataController);
    }
}
