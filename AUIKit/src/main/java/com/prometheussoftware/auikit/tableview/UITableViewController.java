package com.prometheussoftware.auikit.tableview;

import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public class UITableViewController <C extends UITableViewContentController> extends UIViewController {

    protected C contentController;

    public UITableViewController() {
        super();
    }

    @Override public void viewDidLoad() {
        super.viewDidLoad();
        createContentController();
    }

    protected void createContentController() {
        setContentController((C) new UITableViewContentController(view()));
    }

    /** Subclass must implement to return custom controller */
    protected UITableViewDataController createDataController() {
        return new UITableViewDataController();
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
