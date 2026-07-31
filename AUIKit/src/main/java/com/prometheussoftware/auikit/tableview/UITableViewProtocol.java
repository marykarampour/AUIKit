package com.prometheussoftware.auikit.tableview;

import android.view.ViewGroup;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.uiview.UILabel;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiview.protocols.UIControlProtocol;
import com.prometheussoftware.auikit.uiview.protocols.UIEditingAccessoryProtocol;

import java.util.ArrayList;
import java.util.HashMap;

public interface UITableViewProtocol {

    interface TableView <D extends Data> {
        void reloadData();
        default D getDataController() { return null; }
        default <S extends TableObject.Section> void setData(ArrayList<S> data) {}
        default void disableRecycling(int viewType) {}
        default boolean allowsMultipleSelection() { return false; }
    }

    interface UpdateDelegate {
        void performUpdateForDidSelectSectionAtIndex (TableObject.Section item, int section);
        void performUpdateForDidSelectSectionAtIndex (TableObject.Section item, int section, boolean selected);
        void performUpdateForDidSelectRowAtIndexPath (Object item, IndexPath indexPath);
    }

    interface RecyclerViewData {
        default int viewTypeForPosition(int position) { return 0; };

        default <V extends UITableViewCell> V cellForViewType(ViewGroup parent, int viewType) { return null; };

        default <V extends UIView> V headerForViewType(ViewGroup parent, int viewType) { return null; };

        default <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) { return null; };

        default <V extends UITableViewHolder, W extends UIView> V viewHolderForHeader(W header) { return null; };

        default <V extends UITableViewHolder> void bindData(V holder, int position) {};

        default <V extends UITableViewHolder> void bindData(UITableView tableView, V holder, int position) {};

        default <V extends UITableViewHolder> V viewHolderForViewType(ViewGroup parent, int viewType) { return null; };
    }

    interface TableViewData <V extends UIView & UIControlProtocol & UIEditingAccessoryProtocol> {

        default int numberOfRowsInSection(UITableView tableView, int section) {
            return 0;
        }

        default V cellForRowAtIndexPath(UITableView tableView, IndexPath indexPath) {
            return (V) new UIView();
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

        default int heightForTextFieldCellAtIndexPath (IndexPath indexPath) {
            return App.constants().TextField_Height() + 2*App.constants().Vertical_Margin();
        }

        default int heightForTextViewCellAtIndexPath (IndexPath indexPath) {
            return indexPath.row == TEXTVIEW_CELL_ROW.TITLE.intValue() ? App.constants().TextView_Title_Height() : App.constants().TextView_Medium_Height();
        }

        default UIView viewForHeaderInSection(UITableView tableView, int section) {
            return new UILabel();
        }

        default UIView viewForFooterInSection(UITableView tableView, int section) {
            return new UILabel();
        }

        default void didSelectRowAtIndexPath(UITableView tableView, IndexPath indexPath) {}

        default void didDeselectRowAtIndexPath(UITableView tableView, IndexPath indexPath) {}

        default void didSelectSectionAtIndex(UITableView tableView, int section) {}

        default void didDeselectSectionAtIndex(UITableView tableView, int section) {}

        default void tableViewCommitEditingStyleForRowAtIndexPath (UITableView tableView, UITableViewCell.EDITING_STYLE editingStyle, IndexPath indexPath) {}

        //Single table view

        default int numberOfRowsInSection(int section) {
            return 0;
        }

        default V cellForRowAtIndexPath(IndexPath indexPath) {
            return (V) new UIView();
        }

        default int numberOfSectionsInTableView() {
            return 1;
        }

        default int heightForRowAtIndexPath(Object item, IndexPath indexPath) {
            return App.constants().Default_Row_Height();
        }

        default int heightForRowAtIndexPath(IndexPath indexPath) {
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

        default void didSelectRowAtIndexPath(IndexPath indexPath) {}

        default void didSelectRowAtIndexPath(Object item, IndexPath indexPath) {}

        default void didSelectAccessoryAtIndexPath(IndexPath indexPath) {}

        default void didSelectAccessoryAtIndexPath(Object item, IndexPath indexPath) {}

        default void didDeselectRowAtIndexPath(Object item, IndexPath indexPath) {}

        default void didSelectSectionAtIndex(TableObject.Section item, int section) {}

        default void didDeselectSectionAtIndex(TableObject.Section item, int section) {}

        default void didSelectSectionAtIndex(TableObject.Section item, int section, boolean selected) {}

        default boolean tableViewCanEditRowAtIndexPath (UITableView tableView, IndexPath indexPath) { return false; }

        default boolean tableViewCanEditRowAtIndexPath (IndexPath indexPath) { return false; }

        default UITableViewCell.EDITING_STYLE tableViewEditingStyleForRowAtIndexPath (UITableView tableView, IndexPath indexPath) { return UITableViewCell.EDITING_STYLE.NONE; }

        default UITableViewCell.EDITING_STYLE tableViewEditingStyleForRowAtIndexPath (IndexPath indexPath) { return UITableViewCell.EDITING_STYLE.NONE; }

        default void tableViewCommitEditingStyleForRowAtIndexPath (UITableViewCell.EDITING_STYLE editingStyle, IndexPath indexPath) {}

        default <V extends UIView> void customizeHeaderViewForViewType(V header, int viewType) {}

        default void setUpdateDelegate(UITableViewProtocol.UpdateDelegate updateDelegate) {}

        default void setViewDelegate(UITableViewProtocol.TableView viewDelegate) {}

        default <S extends TableObject.Section> void setSections (ArrayList<S> sections) {}

        default ArrayList<TableObject.Section> getSections() { return new ArrayList<>(); }

        default int positionForIndexPath (IndexPath indexPath) { return -1; }

        default int numberOfVisibleViews() { return 0; }

        default void setMultiSelectEnabled(boolean multiSelectEnabled) {};
    }

    interface Data extends RecyclerViewData, TableViewData<UITableViewCell> {

        default UITableViewCell cellForRowAtIndexPath(UITableView tableView, IndexPath indexPath) {
            return new UITableViewCell.Concrete();
        }

        default UITableViewCell cellForRowAtIndexPath(IndexPath indexPath) {
            return new UITableViewCell.Concrete();
        }
    }

    enum TEXTVIEW_CELL_ROW {
        TITLE(0),
        TEXT(1),
        COUNT(2);

        private final int value;
        private static final HashMap<Integer, TEXTVIEW_CELL_ROW> map = new HashMap<>();

        static  {
            for (TEXTVIEW_CELL_ROW row : values()) {
                map.put(row.value, row);
            }
        }

        TEXTVIEW_CELL_ROW(int i) {
            value = i;
        }

        public int intValue() { return value; }
    }
}
