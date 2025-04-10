package com.prometheussoftware.auikit.uiview;

import android.view.Gravity;
import android.widget.SearchView;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.uiview.protocols.UISearchDelegate;

public class UISearchBar extends UISingleView <UISearchBar.UISearchLayer> {

    private UISearchDelegate delegate;

    public UISearchBar() {
        super();
        init();
    }

    @Override
    public void initView() {
        super.initView();
        view = new UISearchLayer();
        view.setBorderWidth(borderWidth());
    }

    @Override
    public UISearchLayer getView() {
        return super.getView();
    }

    public void clear() {
        view.getView().getView().setQuery("", false);
    }

    public void setDelegate(UISearchDelegate delegate) {
        this.delegate = delegate;

        if (delegate == null) return;

        view.getView().getView().setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (delegate != null) return delegate.searchBarDidSubmitText(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (delegate != null) return delegate.searchBarDidChangeText(newText);
                return false;
            }
        });

        view.getView().getView().setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                if (delegate != null) return delegate.searchBarDidSelectSuggestion(position);
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                if (delegate != null) return delegate.searchBarDidPressSuggestion(position);
                return false;
            }
        });

        view.getView().getView().setOnCloseListener( () -> {
            if (delegate != null) return delegate.searchBarCloseButtonPressed();
            return false;
        });
    }

    @Override
    protected UIEdgeInsets insets() {
        return new UIEdgeInsets(Dimensions.Int_4(), Dimensions.Int_8(), Dimensions.Int_4(), Dimensions.Int_8());
    }

    protected int borderWidth() {
        return Dimensions.Int_2();
    }

    public void setSearchBackgroundColor(UIColor color) {
        view.getView().setBackgroundColor(color);
    }

    public void setSearchBorderColor(UIColor color) {
        view.setBorderColor(color);
    }

    public void setCornerRadius(float radius) {
        view.setCornerRadius(radius - 2*borderWidth());
    }

    //region layer

    public static class UISearchView extends UISingleView <SearchView> {

        public UISearchView() {
            super();
            init();
        }

        @Override public void initView() {
            super.initView();
            view = new SearchView(getActivity());
            view.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            view.setBackground(null);
            view.setFocusable(false);
            view.setFocusableInTouchMode(true);
        }
    }

    public static class UISearchLayer extends UISingleLayerView <UISearchView> {

        public UISearchLayer() {
            super();
            init();
        }

        @Override
        protected void createView() {
            setView(new UISearchView());
        }
    }

    //endregion
}
