package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewController;
import com.prometheussoftware.auikit.tableview.search.BaseCellDataSource;

import java.util.ArrayList;

public class UIItemsListViewController  <T extends BaseModel & BaseCellDataSource, D extends UIItemsListDataController<T>, C extends UIItemsListContentController<T, D>> extends UITableViewController <D, C> {

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
    protected C createContentController(D dataController) {
        return (C)new UIItemsListContentController(view(), dataController);
    }


    @Override
    protected D createDataController() {
        return (D)new UIItemsListDataController();
    }

    protected Class cellClass() {
        return UITableViewCell.class;
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
}
