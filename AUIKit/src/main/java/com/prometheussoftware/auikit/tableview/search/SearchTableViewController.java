package com.prometheussoftware.auikit.tableview.search;

import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewController;
import com.prometheussoftware.auikit.uiview.protocols.UISearchDelegate;
import com.prometheussoftware.auikit.utility.ArrayUtility;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SearchTableViewController <T extends BaseModel & SearchTableViewController.DataSource> extends UITableViewController <SearchTableViewDataController, SearchTableViewContentController> implements UISearchDelegate {

    private ArrayList<T> items;
    private ArrayList<T> searchItems;
    private T selectedItem;

    public SearchTableViewController() {
        super();
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        loadData();
    }

    public void setItems(ArrayList<T> items) {
        this.items = items;
        setSelectedItem(preSelected());
        setSearchItems(items);
    }

    public ArrayList<T> getItems() {
        return items;
    }

    private void setSearchItems(ArrayList<T> searchItems) {
        this.searchItems = searchItems;
        updateData();
    }

    protected T preSelected() {
        return null;
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem = selectedItem;
    }

    protected void loadData() {

        ArrayList<TableObject.Section> sections = new ArrayList();
        TableObject.Section section = new TableObject.Section();

        section.expandedHeight = 0;
        section.setCollapsible(false);
        sections.add(section);

        contentController.getDataController().setMultiSelectEnabled(false);
        contentController.setData(sections);
    }

    public void updateData() {

        TableObject.RowData rowData = rowData(selectedItem);
        ArrayList<TableObject.Section> sections = BaseModel.cloneArray(contentController.getDataController().getSections());

        if (0 < sections.size()) {
            sections.get(0).rows = rowData;
            if (0 < rowData.getItemsInfo().array.size()) {
                contentController.disableRecycling(rowData.getItemsInfo().array.get(0).getFirst().getIdentifier());
            }
        }
        contentController.setData(sections);
    }

    private TableObject.RowData rowData (T selectedItem) {

        TableObject.RowData data = new TableObject.RowData(searchItems, UITableViewCell.Concrete.class);
        ArrayList<T> selected = ArrayUtility.arrayOf(selectedItem);
        data.setSelected(selected);
        return data;
    }

    @Override
    protected SearchTableViewDataController createDataController() {
        return new SearchTableViewDataController(this);
    }

    @Override
    protected SearchTableViewContentController createContentController(SearchTableViewDataController dataController) {

        SearchTableViewContentController controller = new SearchTableViewContentController(view(), dataController);
        controller.getSearchView().setDelegate(this);
        return controller;
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
            setSearchItems(items);
        }
        else {
            ArrayList<T> list = new ArrayList<>();
            list.addAll(items.stream().filter(item -> item.title().toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList()));
            setSearchItems(list);
        }
    }

    //endregion

    public interface DataSource {
        String title();
    }
}
