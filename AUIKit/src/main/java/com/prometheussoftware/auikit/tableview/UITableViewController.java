package com.prometheussoftware.auikit.tableview;

import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public class UITableViewController <D extends UITableViewDataController, C extends UITableViewContentController<D>> extends UIViewController {

    protected C contentController;

    public UITableViewController() {
        super();
    }

    @Override public void viewDidLoad() {
        super.viewDidLoad();
        setContentController(createContentController(createDataController()));
    }

    /** Subclass must implement to return custom controller */
    protected C createContentController(D dataController) {
        return (C) new UITableViewContentController(view(), dataController);
    }

    /** Subclass must implement to return custom controller */
    protected D createDataController() {
        return (D) new UITableViewDataController();
    }

    protected void setContentController(C contentController) {
        this.contentController = contentController;
    }

    public C getContentController() {
        return contentController;
    }

    public void reloadData() {
        contentController.reloadData();
    }
}
