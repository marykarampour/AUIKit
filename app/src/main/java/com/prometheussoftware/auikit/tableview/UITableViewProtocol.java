package com.prometheussoftware.auikit.tableview;

import android.view.ViewGroup;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.uiview.UILabel;
import com.prometheussoftware.auikit.uiview.UIView;

public interface UITableViewProtocol {

    interface TableView {
        void reloadData();
    }

    interface Content {
        void performUpdateForDidSelectSectionAtIndex (TableObject.Section item, int section);
        void performUpdateForDidSelectSectionAtIndex (TableObject.Section item, int section, boolean selected);
    }

    interface Data {

        default int numberOfRowsInSection(UITableView tableView, int section) {
            return 0;
        }

        default UITableViewCell cellForRowAtIndexPath(UITableView tableView, IndexPath indexPath) {
            return new UITableViewCell();
        }

        default int numberOfSectionsInTableView(UITableView tableView) {
            return 1;
        }

        default int heightForRowAtIndexPath(UITableView tableView, IndexPath indexPath) {
            return App.constants().Default_Row_Height();
        }

        default int heightForHeaderInSection(UITableView tableView, int section) {
            return 0;
        }

        default int heightForFooterInSection(UITableView tableView, int section) {
            return 0;
        }

        default UIView viewForHeaderInSection(UITableView tableView, int section) {
            return new UILabel();
        }

        default UIView viewForFooterInSection(UITableView tableView, int section) {
            return new UILabel();
        }

        default void didSelectRowAtIndexPath(UITableView tableView, IndexPath indexPath) {
        }

        default void didDeselectRowAtIndexPath(UITableView tableView, IndexPath indexPath) {
        }

        default void didSelectSectionAtIndex(UITableView tableView, int section) {
        }

        default void didDeselectSectionAtIndex(UITableView tableView, int section) {
        }

        //Single table view
        default int numberOfRowsInSection(int section) {
            return 0;
        }

        default UITableViewCell cellForRowAtIndexPath(IndexPath indexPath) {
            return new UITableViewCell();
        }

        default int numberOfSectionsInTableView() {
            return 1;
        }

        default int heightForRowAtIndexPath(Object item, IndexPath indexPath) {
            return App.constants().Default_Row_Height();
        }

        default int heightForHeaderInSection(int section) {
            return 0;
        }

        default int heightForFooterInSection(int section) {
            return App.constants().TableView_Section_Header_Height();
        }

        default UIView viewForHeaderInSection(int section) {
            return new UILabel();
        }

        default UIView viewForFooterInSection(int section) {
            return new UILabel();
        }

        default void didSelectRowAtIndexPath(Object item, IndexPath indexPath) {
        }

        default void didDeselectRowAtIndexPath(Object item, IndexPath indexPath) {
        }

        default void didSelectSectionAtIndex(TableObject.Section item, int section) {
        }

        default void didDeselectSectionAtIndex(TableObject.Section item, int section) {
        }

        default void didSelectSectionAtIndex(TableObject.Section item, int section, boolean selected) {
        }

        <V extends UIView> void customizeHeaderViewForViewType(V header, int viewType);

        //recycler view

        int viewTypeForPosition(int position);

        <V extends UITableViewCell> V cellForViewType(ViewGroup parent, int viewType);

        <V extends UIView> V headerForViewType(ViewGroup parent, int viewType);

        <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell);

        <V extends UITableViewHolder, W extends UIView> V viewHolderForHeader(W header);

        <V extends UITableViewHolder> void bindData(V holder, int position);

        <V extends UITableViewHolder> void bindData(UITableView tableView, V holder, int position);

        <V extends UITableViewHolder> V viewHolderForViewType(ViewGroup parent, int viewType);
    }
}
