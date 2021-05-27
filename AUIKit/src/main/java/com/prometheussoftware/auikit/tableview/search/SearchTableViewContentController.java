package com.prometheussoftware.auikit.tableview.search;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.tableview.UITableViewContentController;
import com.prometheussoftware.auikit.uiview.UISearchBar;
import com.prometheussoftware.auikit.uiview.UIView;

public class SearchTableViewContentController <T extends BaseModel & BaseCellDataSource, D extends SearchTableViewDataController<T>> extends UITableViewContentController <D> {

    private UISearchBar searchView;

    public SearchTableViewContentController(UIView view, D dataController) {
        super(view, dataController);
    }

    /** Call load in your subclass version of this constructor */
    public SearchTableViewContentController(D dataController) {
        super(dataController);
    }

    @Override
    protected void createViews() {
        super.createViews();
        createSearchView();
    }

    protected void createSearchView() {
        UISearchBar searchView = new UISearchBar();
        searchView.getView().setEnabled(false);
        searchView.setCornerRadius(searchBarHeight() / 2.0f);
        setSearchView(searchView);
    }

    public void setSearchView(UISearchBar searchView) {
        if (searchView == null) return;

        this.searchView = searchView;
        getView().addSubView(searchView);
    }

    @Override
    protected void constraintViews() {

        UIEdgeInsets insets = new UIEdgeInsets(searchBarHeight(), 0, 0, 0);

        getView().constraintHeightForView(searchView, insets.top);
        getView().constraintForView(ConstraintSet.TOP, searchView);
        getView().constraintForView(ConstraintSet.START, searchView);
        getView().constraintForView(ConstraintSet.END, searchView);
        getView().constraintSidesForView(getTableView(), insets);
        getView().constraintSidesForView(getRefreshView(), insets);
        getView().applyConstraints();
    }

    public UISearchBar getSearchView() {
        return searchView;
    }

    protected int searchBarHeight() {
        return Dimensions.Int_52();
    }

}
