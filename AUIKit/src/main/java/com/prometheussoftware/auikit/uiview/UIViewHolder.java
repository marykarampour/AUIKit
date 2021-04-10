package com.prometheussoftware.auikit.uiview;

import android.util.Size;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;

public abstract class UIViewHolder <T extends UIView> extends UIView implements HolderProtocol {

    private ArrayList<T> views = new ArrayList<>();
    /** Default is 28. */
    protected Size itemSize = Dimensions.size(Dimensions.Int_28());

    public UIViewHolder() {
        super();
    }

    public void setViews(ArrayList<T> views) {

        clearViews();
        this.views = views;
        addSubViews(views);
        updateConstraints();
    }

    public void setView(T view) {

        if (ArrayUtility.firstObject(views) == view) return;

        ArrayList<T> tempButtons = (ArrayList<T>) views.clone();

        clearViews();

        if (view == null) {
            ArrayUtility.safeRemove(tempButtons, 0);
        }
        else {
            ArrayUtility.safeMove(tempButtons, 0, view);
        }

        setViews(tempButtons);
    }

    public void addView(T view) {

        if (views.contains(view)) return;

        ArrayList<T> tempButtons = (ArrayList<T>) views.clone();
        tempButtons.add(view);
        setViews(tempButtons);
    }

    private void clearViews() {
        removeSubViews(views);
        views.clear();
    }

    public ArrayList<T> getViews() {
        return views;
    }

    protected void setItemSize(Size itemSize) {
        this.itemSize = itemSize;
        if (views.size() != 0) {
            updateConstraints();
        }
    }

    public static class Row <T extends UIView> extends UIViewHolder <T> {

        public Row() {
            super();
        }

        @Override
        public void updateConstraints() {

            clearConstraints(getViews());
            constraintHorizontally(getViews(), 0, 0, 0, false, ConstraintSet.START | ConstraintSet.END);
            constraintSizeForViews(getViews(), itemSize);
            applyConstraints();
        }
    }

    public static class Column <T extends UIView> extends UIViewHolder <T> {

        public Column() {
            super();
        }

        @Override
        public void updateConstraints() {

            clearConstraints(getViews());
            constraintVertically(getViews(), 0, 0, 0, false, ConstraintSet.TOP | ConstraintSet.BOTTOM);
            constraintSizeForViews(getViews(), itemSize);
            applyConstraints();
        }
    }
}

interface HolderProtocol {
    void updateConstraints();
}