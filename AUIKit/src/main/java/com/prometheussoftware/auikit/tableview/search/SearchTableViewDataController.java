package com.prometheussoftware.auikit.tableview.search;

import android.text.SpannableStringBuilder;

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

public class SearchTableViewDataController <T extends BaseModel & BaseCellDataSource> extends UITableViewDataController {

    public SearchTableViewDataController() {
        super();
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
                SpannableStringBuilder title = obj.getSecond().attributedTitle();
                view.getTitleLabel().setText(title != null ? title : obj.getSecond().plainTitle());
                view.setAccessoryType(obj.getFirst().selected ? UITableViewCell.ACCESSORY_TYPE.CHECKMARK : UITableViewCell.ACCESSORY_TYPE.NONE);
            }
        }
    }
}
