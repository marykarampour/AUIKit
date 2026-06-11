package com.prometheussoftware.auikit.tableview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.uiview.UIRefreshView;
import com.prometheussoftware.auikit.uiview.UIView;

import java.util.ArrayList;

public class UITableViewContentController <D extends UITableViewProtocol.Data> implements UITableViewProtocol.TableView<D> {

    private UITableView tableView;
    private UIRefreshView refreshView;
    private Adapter adapter;
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
        setAdapter(new Adapter());
    }

    protected void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        tableView.getView().setAdapter(adapter);
    }

    @Override
    public <S extends TableObject.Section> void setData(ArrayList<S> data) {
        dataController.setSections(data);
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
        view.addSubview(tableView);
    }

    public void setRefreshView(UIRefreshView refreshView) {
        if (refreshView == null) return;

        this.refreshView = refreshView;
        view.addSubview(refreshView);
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

    @Override
    public void disableRecycling(int viewType) {
        tableView.disableRecycling(viewType);
    }

    @Override
    public D getDataController() {
        return dataController;
    }

    public <T extends D> void setDataController(T dataController) {
        this.dataController = dataController;
        dataController.setViewDelegate(this);
        createAdapter();
    }

    public void requestfocusForViewAtPosition(int position) {
        RecyclerView.ViewHolder viewHolder = tableView.getView().findViewHolderForLayoutPosition(position);
        viewHolder.itemView.requestFocus();
    }

    public void requestfocusForRowAtIndexPath(IndexPath indexPath) {
        int position = dataController.positionForIndexPath(indexPath);
        requestfocusForViewAtPosition(position);
    }

    //endregion

    //region adapter

    public void notifyItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
        adapter.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    public final void notifyItemRangeChanged(int positionStart, int itemCount) {
        adapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    public final void notifyItemRangeInserted(int positionStart, int itemCount) {
        adapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
        adapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    public int getItemCount() {
        return adapter.getItemCount();
    }

    public int getItemViewType(int position) {
        return adapter.getItemViewType(position);
    }

    private final class Adapter extends RecyclerView.Adapter {

        @NonNull
        @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return dataController.viewHolderForViewType(parent, viewType);
        }

        @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if (!UITableViewHolder.class.isInstance(holder)) return;

            UITableViewHolder obj = (UITableViewHolder)holder;
            dataController.bindData(obj, position);
        }

        @Override public int getItemCount() {
            return dataController.numberOfVisibleViews();
        }

        @Override public int getItemViewType(int position) {
            return dataController.viewTypeForPosition(position);
        }
    }

    //endregion
}
