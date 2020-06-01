package com.prometheussoftware.auikit.tableview;

import com.prometheussoftware.auikit.uiview.UIRefreshView;
import com.prometheussoftware.auikit.uiview.UIView;

import java.util.ArrayList;

public class UITableViewContentController implements UITableViewProtocol.TableView {

    protected UITableView tableView;
    private UIRefreshView refreshView;
    private UITableViewAdapter adapter;
    protected UIView view;

    /** Call load in your no param constructor */
    public UITableViewContentController() {
        view = new UIView();
        init();
    }

    public UITableViewContentController(UIView view) {
        this.view = view;
        init();
        load();
    }

    /** Call this in your custom constructor
     * for preload initialization */
    public void init() {
    }

    public void load() {
        createViews();
        constraintViews();
    }

    /** Subclass can override to create custom table view and refresh view */
    protected void createViews() {
        createRefreshView();
        createTableView();
    }

    protected void createTableView() {
        setTableView(new UITableView());
    }

    protected void createRefreshView() {
        UIRefreshView refreshView = new UIRefreshView();
        refreshView.getView().setEnabled(false);
        setRefreshView(refreshView);
    }

    public void createAdapter(UITableViewDataController dataController) {
        setAdapter(new UITableViewAdapter(dataController));
    }

    public void setAdapter(UITableViewAdapter adapter) {
        this.adapter = adapter;
        adapter.setView(tableView);
        adapter.getDataController().setViewDelegate(this);
    }

    public UITableViewAdapter getAdapter() {
        return adapter;
    }

    public void setData(ArrayList<TableObject.Section> data) {
        adapter.getDataController().setSections(data);
    }

    //region helpers

    protected void constraintViews() {
        view.constraintSidesForView(tableView);
        view.constraintSidesForView(refreshView);
        view.applyConstraints();
    }

    public void setTableView(UITableView tableView) {
        if (tableView == null) return;

        this.tableView = tableView;
        view.addSubView(tableView);
    }

    public void setRefreshView(UIRefreshView refreshView) {
        if (refreshView == null) return;

        this.refreshView = refreshView;
        view.addSubView(refreshView);
    }

    @Override public void reloadData() {
        if (view != null) view.post(() -> adapter.notifyDataSetChanged());
    }

    public UIView getView() {
        return view;
    }

    public void disableRecycling(int viewType) {
        tableView.disableRecycling(viewType);
    }

    public UITableViewDataController getDataController() {
        return adapter.getDataController();
    }

    //endregion
}
