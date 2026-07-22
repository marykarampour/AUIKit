package com.prometheussoftware.auikit.tableview;

import android.text.SpannableStringBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.callback.ViewControllerCallback;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;
import com.prometheussoftware.auikit.uiviewcontroller.ItemsListProtocol;

import java.util.ArrayList;

public class UIItemsListViewController<T extends BaseModel & ViewContentProtocol.Placeholder, C extends BaseTableViewCell> extends UITableViewController implements ItemsListProtocol.VC, ItemsListProtocol.ListVC {

    private ArrayList<T> items = new ArrayList<>();

    public boolean addsNewElementsToStart;

    public UIItemsListViewController() {
        super();
        init().view();
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
        updateData();
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public void addItem (T item) {
        if (items == null) items = new ArrayList<>();
        if (items.contains(item)) return;
        addAndUpdate(item);
    }

    public void removeItem (T item) {
        if (items == null) return;
        items.remove(item);
        updateData();
    }

    public void setItem (T item) {
        if (items == null) items = new ArrayList<>();
        if (items.contains(item)) return;
        items.clear();
        addAndUpdate(item);
    }

    private void addAndUpdate (T item) {
        if (addsNewElementsToStart)
            items.add(0, item);
        else
            items.add(item);
        updateData();
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        loadData();
    }

    protected void loadData() {

        ArrayList<TableObject.Section> sections = new ArrayList();
        TableObject.Section section = new TableObject.Section();

        section.expandedHeight = 0;
        section.setCollapsible(false);
        sections.add(section);

        dataController().setMultiSelectEnabled(false);
        contentController.setData(sections);
    }

    public void updateData() {

        TableObject.RowData rowData = new TableObject.RowData(items, cellClass());
        ArrayList<TableObject.Section> sections = BaseModel.cloneArray(dataController().getSections());

        if (0 < sections.size()) {
            sections.get(0).rows = rowData;
            if (0 < rowData.itemsArray().size()) {
                ArrayList<Pair<TableObject.CellInfo, Object>> array = rowData.itemsArray();
                contentController.disableRecycling(array.get(0).getFirst().getIdentifier());
            }
        }
        contentController.setData(sections);
    }

    @Override
    protected DataController createDataController() {
        return new DataController();
    }

    protected Class cellClass() {
        return BaseTableViewCell.class;
    }

    @Override
    public void performUpdateForDidSelectRowAtIndexPath(Object item, IndexPath indexPath) {
        super.performUpdateForDidSelectRowAtIndexPath(item, indexPath);

        if (item instanceof Pair) {
            Pair<TableObject.CellInfo, T> obj = (Pair<TableObject.CellInfo, T>)item;
            didSelectItemAtIndexPath(obj.getSecond(), indexPath);
        }
        reloadData();
    }

    protected void didSelectItemAtIndexPath(T item, IndexPath indexPath) { }

    @Override
    public ViewControllerCallback transitioningViewControllerForItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        return createPresentingSelectionVCForItemAtIndexPath(item, indexPath);
    }

    public class DataController <T extends BaseModel & ViewContentProtocol.Placeholder> extends UITableViewDataController {

        @Override
        public <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) {
            return (V) new CellViewHolder(cell);
        }

        public class CellViewHolder extends UITableViewHolder.Cell <BaseTableViewCell> {

            public CellViewHolder(@NonNull UIView itemView) {
                super(itemView);
            }

            @Override
            protected BaseTableViewCell getView() {
                return super.getView();
            }

            @Override
            public void bindDataForRow(Object item, IndexPath indexPath, @Nullable UITableViewProtocol.Data delegate) {
                super.bindDataForRow(item, indexPath, delegate);

                if (item instanceof Pair) {
                    Pair<TableObject.CellInfo, ViewContentProtocol.Placeholder> obj = (Pair<TableObject.CellInfo, ViewContentProtocol.Placeholder>)item;
                    SpannableStringBuilder title = obj.getSecond().attributedTitle();
                    view.getTitleLabel().setText(title != null ? title : obj.getSecond().title());
                    view.setHeight(0 < obj.getFirst().minHeight ? obj.getFirst().minHeight : delegate.heightForRowAtIndexPath(item, indexPath));
                }
            }
        }

        @Override
        public int heightForRowAtIndexPath(Object item, IndexPath indexPath) {
            return Dimensions.Int_56();
        }
    }
}
