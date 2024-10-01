package com.prometheussoftware.auikit.tableview;

import com.prometheussoftware.auikit.uiview.UIRefreshView;
import com.prometheussoftware.auikit.uiview.UIView;

import java.util.ArrayList;

public class UITableViewContentController <D extends UITableViewDataController> implements UITableViewProtocol.TableView {

    private UITableView tableView;
    private UIRefreshView refreshView;
    private UITableViewAdapter adapter;
    private D dataController;
    private UIView view;

    /** Call load in your subclass version of this constructor */
    public UITableViewContentController(D dataController) {
        view = new UIView();
        init();
        setDataController(dataController);
    }

    public UITableViewContentController(UIView view, D dataController) {
        this.view = view;
        init();
        setDataController(dataController);
        load();
    }

    /** Call this in your custom constructor
     * for preload initialization */
    protected void init() {
        createViews();
    }

    public void load() {
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

    private void createAdapter() {
        setAdapter(new UITableViewAdapter(dataController));
    }

    protected void setAdapter(UITableViewAdapter adapter) {
        this.adapter = adapter;
        adapter.setView(tableView);
        adapter.getDataController().setViewDelegate(this);
    }

    public UITableViewAdapter getAdapter() {
        return adapter;
    }

    public <S extends TableObject.Section> void setData(ArrayList<S> data) {
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

    public UIRefreshView getRefreshView() {
        return refreshView;
    }

    @Override public void reloadData() {
        if (view != null) view.post(() -> adapter.notifyDataSetChanged());
    }

    public UIView getView() {
        return view;
    }

    public UITableView getTableView() {
        return tableView;
    }

    public void disableRecycling(int viewType) {
        tableView.disableRecycling(viewType);
    }

    public D getDataController() {
        return (D) dataController;
    }

    public <T extends D> void setDataController(T dataController) {
        this.dataController = dataController;
        createAdapter();
    }

    //endregion
}
