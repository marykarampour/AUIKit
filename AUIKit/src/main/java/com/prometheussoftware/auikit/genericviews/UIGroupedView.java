package com.prometheussoftware.auikit.genericviews;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nonnull;

/** Class groups an array of views of the same type vertically */
public class UIGroupedView <V extends UIView> extends UIView {

    private ArrayList<V> views = new ArrayList<>();
    private UIView contentView;
    private UIEdgeInsets insets = new UIEdgeInsets();
    private int interItemMargin;
    private int itemHeight;
    private DataDelegate<V> dataDelegate;

    public UIGroupedView() {
        super();
    }

    public UIGroupedView(@Nonnull ArrayList<V> views) {
        super();
        setViews(views);
    }

    public UIGroupedView(@Nonnull ArrayList<V> views, UIEdgeInsets insets, int interItemMargin) {
        super();
        this.interItemMargin = interItemMargin;
        this.insets = insets;
        setViews(views);
    }

    public void setViews(@Nonnull ArrayList<V> views) {
        this.views = views;
        init();
    }

    public void setViews(V ...args) {

        this.views.clear();
        Collections.addAll(views, args);
        init();
        if (dataDelegate != null) dataDelegate.groupDidSetViews(this, views);
    }

    @Override
    public void initView() {
        super.initView();

        contentView = new UIView();
    }

    @Override
    public void loadView() {
        super.loadView();

        addSubView(contentView);

        for (V view: views) {
            contentView.addSubView(view);
        }
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        constraintSidesForView(contentView, insets);

        if (0 < itemHeight && 0 < views.size()) {
            contentView.constraintHeightForView(views.get(0), itemHeight);
        }
        contentView.constraintVerticallyAllSides(views, interItemMargin, true);
        contentView.applyConstraints();
        applyConstraints();
    }

    public void setInsets(UIEdgeInsets insets) {
        this.insets = insets;
    }

    public void setInterItemMargin(int interItemMargin) {
        this.interItemMargin = interItemMargin;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int estimatedHeight() {
        return views.size() * itemHeight + (views.size() - 1) * interItemMargin;
    }

    public V viewAtIndex(int index) {
        return ArrayUtility.safeGet(views, index);
    }

    public void setDataDelegate(DataDelegate<V> dataDelegate) {
        this.dataDelegate = dataDelegate;
    }

    public interface DataDelegate <V> {
        void groupDidSetViews (UIGroupedView groupedView, ArrayList<V> views);
    }
}
