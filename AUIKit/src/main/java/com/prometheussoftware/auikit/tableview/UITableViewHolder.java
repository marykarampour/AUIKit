package com.prometheussoftware.auikit.tableview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.prometheussoftware.auikit.genericviews.UICheckbox;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.uiview.UIControl;
import com.prometheussoftware.auikit.uiview.UISwitch;
import com.prometheussoftware.auikit.uiview.UIView;

public class UITableViewHolder <C extends UIView> extends RecyclerView.ViewHolder {

    protected C view;

    public static class Cell <C extends UITableViewCell> extends UITableViewHolder <C> {

        public Cell(@NonNull UIView itemView) {
            super(itemView);
            if (UITableViewCell.class.isInstance(itemView)) {
                view = (C) itemView;
            }
        }

        public void bindDataForRow(Object item, IndexPath indexPath, @Nullable UITableViewProtocol.Data delegate) {

            if (view.getAccessoryView() != null) {
                view.getAccessoryView().setVisibility(View.GONE);
                view.getAccessoryView().setTarget(b -> {
                    if (delegate != null) {
                        delegate.didSelectRowAtIndexPath(item, indexPath);
                    }
                });
            }
            view.setSelectionAction(this, s -> {
                if (delegate != null) {
                    delegate.didSelectRowAtIndexPath(item, indexPath);
                }
            });
        }
    }

    public static class Header <C extends UIView> extends UITableViewHolder <C> {

        public Header(@NonNull UIView itemView) {
            super(itemView);
            if (UIView.class.isInstance(itemView)) {
                view = (C) itemView;
            }
        }

        public void bindDataForSection(TableObject.Section item, Integer section, @Nullable UITableViewProtocol.Data delegate) { }

        public static class Switch <C extends UISwitch> extends Header <C> {

            public Switch(@NonNull UIView itemView) {
                super(itemView);
            }

            public void bindDataForSection(TableObject.Section item, Integer section, @Nullable UITableViewProtocol.Data delegate) {
                super.bindDataForSection(item, section, delegate);
                view.setDelegate((UIControl view, boolean selected) -> {
                    if (delegate != null) {
                        delegate.didSelectSectionAtIndex(item, section, selected);
                    }
                });
            }
        }

        public static class Checkbox <C extends UICheckbox> extends Header <C> {

            public Checkbox(@NonNull UIView itemView) {
                super(itemView);
            }

            public void bindDataForSection(TableObject.Section item, Integer section, @Nullable UITableViewProtocol.Data delegate) {
                super.bindDataForSection(item, section, delegate);
                view.checkView().setDelegate((UIControl view, boolean selected) -> {
                    if (delegate != null) {
                        delegate.didSelectSectionAtIndex(item, section, selected);
                    }
                });
            }
        }
    }

    public UITableViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public UITableViewHolder(@NonNull UIView v) {
        super(v);
    }

    public void bindDataForRow(Object item, IndexPath indexPath, @Nullable UITableViewProtocol.Data delegate) {}

    public void bindDataForSection(TableObject.Section item, Integer section, @Nullable UITableViewProtocol.Data delegate) {}

    protected C getView() {
        return view;
    }
}
