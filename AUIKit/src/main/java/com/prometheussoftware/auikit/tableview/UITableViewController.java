package com.prometheussoftware.auikit.tableview;

import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public class UITableViewController <D extends UITableViewDataController, C extends UITableViewContentController<D>> extends UIViewController implements UITableViewProtocol.UpdateDelegate {

    protected C contentController;

    public UITableViewController() {
        super();
    }

    @Override public void viewDidLoad() {
        super.viewDidLoad();
        setContentController(createContentController(createDataController()));
        contentController.getDataController().setUpdateDelegate(this);
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

    protected D dataController() {
        return contentController.getDataController();
    }

    public void reloadData() {
        contentController.reloadData();
    }

    @Override
    public void performUpdateForDidSelectSectionAtIndex(TableObject.Section item, int section) {
        performUpdateForDidSelectSectionAtIndex(item, section, true);
    }

    @Override
    public void performUpdateForDidSelectSectionAtIndex(TableObject.Section item, int section, boolean selected) {
    }

    @Override
    public void performUpdateForDidSelectRowAtIndexPath(Object item, IndexPath indexPath) {
    }

}
