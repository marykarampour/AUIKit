package com.prometheussoftware.auikit.tableview.search;

import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewController;
import com.prometheussoftware.auikit.uiview.protocols.UISearchDelegate;
import com.prometheussoftware.auikit.utility.ArrayUtility;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class SearchTableViewController <T extends BaseModel & BaseCellDataSource, D extends SearchTableViewDataController<T>> extends UITableViewController <D, SearchTableViewContentController<T, D>> implements UISearchDelegate {

    private ArrayList<T> items;
    private ArrayList<T> searchItems;
    private T selectedItem;

    public void setSearchItems(ArrayList<T> searchItems) {
        this.searchItems = searchItems;
        updateData();
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
        setSelectedItem(preSelected());
        setSearchItems(items);
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public ArrayList<T> getSearchItems() {
        return searchItems;
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem = selectedItem;
    }

    public SearchTableViewController() {
        super();
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        loadData();
    }

    @Override
    public void viewDidAppear(boolean animated) {
        super.viewDidAppear(animated);
        getContentController().getSearchView().clear();
    }

    @Override
    public void viewDidDisappear(boolean animated) {
        super.viewDidDisappear(animated);
        getContentController().getSearchView().clear();
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

        TableObject.RowData rowData = rowData(getSelectedItem());
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

    private TableObject.RowData rowData (T selectedItem) {

        TableObject.RowData data = new TableObject.RowData(getSearchItems(), cellClass());
        ArrayList<T> selected = ArrayUtility.arrayOf(selectedItem);
        data.setSelected(selected);
        return data;
    }

    protected abstract Class cellClass();

    @Override
    protected SearchTableViewContentController createContentController(SearchTableViewDataController dataController) {

        SearchTableViewContentController controller = new SearchTableViewContentController(view(), dataController);
        controller.getSearchView().setDelegate(this);
        return controller;
    }

    @Override
    public void performUpdateForDidSelectRowAtIndexPath(Object item, IndexPath indexPath) {
        super.performUpdateForDidSelectRowAtIndexPath(item, indexPath);

        if (item instanceof Pair) {
            Pair<TableObject.CellInfo, T> obj = (Pair<TableObject.CellInfo, T>)item;
            boolean isSelected = obj.getSecond().equals(getSelectedItem());
            setSelectedItem(isSelected ? null : obj.getSecond());
        }
        reloadData();
    }

    protected T preSelected() {
        return null;
    }
    
    //region search delegate and filtering

    @Override
    public boolean searchBarDidSubmitText(String query) {
        filterSearchItems(query);
        return true;
    }

    @Override
    public boolean searchBarDidChangeText(String newText) {
        filterSearchItems(newText);
        return true;
    }

    @Override
    public boolean searchBarCloseButtonPressed() {
        filterSearchItems("");
        return true;
    }

    private void filterSearchItems(String query) {
        if (StringUtility.isEmpty(query)) {
            setSearchItems(getItems());
        }
        else {
            ArrayList<T> list = new ArrayList<>();
            list.addAll(getItems().stream().filter(item -> item.title().toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList()));
            setSearchItems(list);
        }
    }

    //endregion

}
