package com.prometheussoftware.auikit.tableview.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewDataController;
import com.prometheussoftware.auikit.tableview.UITableViewHolder;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.uiview.UIView;

public class SearchTableViewDataController <T extends BaseModel & SearchTableViewController.DataSource> extends UITableViewDataController {

    private final SearchTableViewController searchTableViewController;

    public SearchTableViewDataController(SearchTableViewController searchTableViewController) {
        this.searchTableViewController = searchTableViewController;
    }

    @Override
    public <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) {
        return (V) new CellViewHolder(cell);
    }

    class CellViewHolder extends UITableViewHolder.Cell <UITableViewCell.Concrete> {

        public CellViewHolder(@NonNull UIView itemView) {
            super(itemView);
        }

        @Override
        public void bindDataForRow(Object item, IndexPath indexPath, @Nullable UITableViewProtocol.Data delegate) {
            super.bindDataForRow(item, indexPath, delegate);

            if (item instanceof Pair) {
                Pair<TableObject.CellInfo, T> obj = (Pair<TableObject.CellInfo, T>)item;
                view.getTitleLabel().setText(obj.getSecond().title());
                view.setAccessoryType(obj.getFirst().selected ? UITableViewCell.ACCESSORY_TYPE.CHECKMARK : UITableViewCell.ACCESSORY_TYPE.NONE);
            }
        }
    }

    @Override
    public void didSelectRowAtIndexPath(Object item, IndexPath indexPath) {
        super.didSelectRowAtIndexPath(item, indexPath);

        if (item instanceof Pair) {
            Pair<TableObject.CellInfo, T> obj = (Pair<TableObject.CellInfo, T>)item;
            boolean isSelected = obj.getSecond().equals(searchTableViewController.getSelectedItem());
            searchTableViewController.setSelectedItem(isSelected ? null : obj.getSecond());
        }
        searchTableViewController.reloadData();
    }
}
