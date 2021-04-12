package com.prometheussoftware.auikit.tableview.search;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.tableview.UITableViewContentController;
import com.prometheussoftware.auikit.uiview.UISearchBar;
import com.prometheussoftware.auikit.uiview.UIView;

public class SearchTableViewContentController extends UITableViewContentController {

    private UISearchBar searchView;

    public SearchTableViewContentController(UIView view) {
        super(view);
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
        view.addSubView(searchView);
    }

    @Override
    protected void constraintViews() {

        UIEdgeInsets insets = new UIEdgeInsets(searchBarHeight(), 0, 0, 0);

        view.constraintHeightForView(searchView, insets.top);
        view.constraintForView(ConstraintSet.TOP, searchView);
        view.constraintForView(ConstraintSet.START, searchView);
        view.constraintForView(ConstraintSet.END, searchView);
        view.constraintSidesForView(tableView, insets);
        view.constraintSidesForView(getRefreshView(), insets);
        view.applyConstraints();
    }

    public UISearchBar getSearchView() {
        return searchView;
    }

    protected int searchBarHeight() {
        return Dimensions.Int_52();
    }
}
