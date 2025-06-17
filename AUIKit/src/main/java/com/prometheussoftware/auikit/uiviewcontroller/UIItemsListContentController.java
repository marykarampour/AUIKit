package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.tableview.UITableViewContentController;
import com.prometheussoftware.auikit.tableview.search.BaseCellDataSource;
import com.prometheussoftware.auikit.uiview.UIView;

public class UIItemsListContentController <T extends BaseModel & BaseCellDataSource, D extends UIItemsListDataController<T>> extends UITableViewContentController<D> {

    public UIItemsListContentController(UIView view, D dataController) {
        super(view, dataController);
    }
}
