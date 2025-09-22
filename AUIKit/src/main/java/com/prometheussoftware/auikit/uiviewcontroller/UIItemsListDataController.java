package com.prometheussoftware.auikit.uiviewcontroller;

import android.text.SpannableStringBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewDataController;
import com.prometheussoftware.auikit.tableview.UITableViewHolder;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.tableview.search.BaseCellDataSource;
import com.prometheussoftware.auikit.uiview.UIView;

public class UIItemsListDataController <T extends BaseModel & BaseCellDataSource> extends UITableViewDataController {

    public UIItemsListDataController() {
        super();
    }

    @Override
    public <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) {
        return (V) new UIItemsListDataController.CellViewHolder(cell);
    }

    public static class CellViewHolder extends UITableViewHolder.Cell <UITableViewCell> {

        public CellViewHolder(@NonNull UIView itemView) {
            super(itemView);
        }

        @Override
        protected UITableViewCell getView() {
            return super.getView();
        }

        @Override
        public void bindDataForRow(Object item, IndexPath indexPath, @Nullable UITableViewProtocol.Data delegate) {
            super.bindDataForRow(item, indexPath, delegate);

            if (item instanceof Pair) {
                Pair<TableObject.CellInfo, BaseCellDataSource> obj = (Pair<TableObject.CellInfo, BaseCellDataSource>)item;
                SpannableStringBuilder title = obj.getSecond().attributedTitle();
                view.getTitleLabel().setText(title != null ? title : obj.getSecond().plainTitle());
                view.setHeight(0 < obj.getFirst().minHeight ? obj.getFirst().minHeight : delegate.heightForRowAtIndexPath(item, indexPath));
            }
        }
    }

    @Override
    public int heightForRowAtIndexPath(Object item, IndexPath indexPath) {
        return Dimensions.Int_56();
    }
}
