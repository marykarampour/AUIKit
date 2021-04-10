package com.prometheussoftware.auikit.tableview.search;

import com.prometheussoftware.auikit.tableview.UITableViewController;

import java.util.ArrayList;

public class SearchTableViewController extends UITableViewController {

    private ArrayList<DataSource> items;
    private ArrayList<DataSource> resultItems;

    public void setItems(ArrayList<DataSource> items) {
        this.items = items;
    }

    public interface DataSource {
        String title();
    }
}
