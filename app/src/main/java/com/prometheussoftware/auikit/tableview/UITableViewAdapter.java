package com.prometheussoftware.auikit.tableview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public final class UITableViewAdapter extends RecyclerView.Adapter {

    private UITableViewDataController dataController;
    private UITableView view;

    public UITableViewAdapter(UITableViewDataController dataController) {
        setDataController(dataController);
    }

    public void setDataController(UITableViewDataController dataController) {
        this.dataController = dataController;
    }

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

    public void setView(UITableView view) {
        this.view = view;
        view.getView().setAdapter(this);
    }

    public UITableView getView() {
        return view;
    }

    public UITableViewDataController getDataController() {
        return dataController;
    }
}
