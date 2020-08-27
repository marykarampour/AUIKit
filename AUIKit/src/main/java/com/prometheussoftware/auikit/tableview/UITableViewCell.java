package com.prometheussoftware.auikit.tableview;

import android.graphics.Color;
import android.util.Size;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.genericviews.UIAccessoryView;
import com.prometheussoftware.auikit.genericviews.UIMultiViewLabel;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.uiview.UIButton;
import com.prometheussoftware.auikit.uiview.UIControl;
import com.prometheussoftware.auikit.uiview.UIImageView;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.utility.ViewUtility;

public class UITableViewCell <A extends UIAccessoryView, S extends UIView> extends UIMultiViewLabel <UIImageView, A, UIView> {

    /** Use only in case of static cells */
    public IndexPath indexPath;

    protected S separator;

    private UIButton interactionLayer;

    static {
        Identifier.Register(UITableViewCell.class);
    }

    /** Call init() in constructor of subclass */
    public UITableViewCell() {
        super();
        setEnabled(true);
    }

    @Override
    public void init() {
        setSize();
        super.init();
        constraintLayout();
    }

    @Override
    public void initView() {
        super.initView();

        interactionLayer = new UIButton();
        setTopView(interactionLayer, false, false);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        contentView.constraintForView(ConstraintSet.START, separator, 0);
        contentView.constraintForView(ConstraintSet.END, separator, 0);
        contentView.constraintForView(ConstraintSet.BOTTOM, separator, 0);
        contentView.applyConstraints();
    }

    protected void createSeparator() {
        separator = (S) new UIView();
        separator.setEnabled(false);
        setSeparatorColor(UIColor.white(1.0f));
    }

    @Override
    public void createRightView() {
        rightView = (A) UIAccessoryView.build(UIAccessoryView.TYPE.IMAGE);
    }

    @Override
    public void createLeftView() {
        leftView = new UIImageView();
        leftView.view().setAdjustViewBounds(true);
        leftView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    //region sizes

    @Override
    public Size estimatedSize() {
        return new Size(Constants.Screen_Size().getWidth(), App.constants().Default_Row_Height());
    }

    public void setAccessorySize(int size) {
        contentView.constraintHeightForView(separator, size);
        contentView.applyConstraints();
    }

    public void setHeight(int height, UIView mainView) {

        constraintSet.clear(mainView.getId());

        boolean hasHeight = height > contentView.getPaddingBottom() + contentView.getPaddingTop();
        if (hasHeight) constraintSidesForView(mainView);
        setMinHeight(height);

        constraintHeightForView(mainView, height);
        contentView.applyConstraints();
    }

    public void setHeight(int height) {
        setMinHeight(height);
    }

    public void setSeparatorHeight(int height) {
        contentView.constraintHeightForView(separator, height);
        contentView.applyConstraints();
    }

    public void setSizeForView(Size size, UIView view) {
        setSizeForView(size, view, new UIEdgeInsets());
    }

    public void setSizeForView(Size size, UIView view, UIEdgeInsets insets) {
        setSizeForView(size, view, insets, -1);
    }

    public void setSeparatorColor(UIColor separatorColor) {
        separator.setBackgroundColor(separatorColor);
    }

    public void setSizeForView(Size size, UIView view, int verticalAlignment) {
        setSizeForView(size, view, new UIEdgeInsets(), verticalAlignment);
    }

    public void setSizeForView(Size size, UIView view, UIEdgeInsets insets, int verticalAlignment) {

        if (!ViewUtility.isChildView(this, view)) return;

        boolean hasHeight = this.getHeight() == 0 || (this.getHeight() > 0 && (size.getHeight() + insets.top + insets.bottom) < this.getHeight());
        if (hasHeight) {
            contentView.constraintSizeForView(view, size);
            switch (verticalAlignment) {
                case ConstraintSet.TOP: {
                    contentView.constraintForView(verticalAlignment, view, insets.top);
                    break;
                }
                case ConstraintSet.BOTTOM: {
                    contentView.constraintForView(verticalAlignment, view, insets.bottom);
                    break;
                }
                default: {
                    contentView.constraintCenterYForView(view);
                    break;
                }
            }
        }
        else {
            contentView.constraintForView(ConstraintSet.TOP, view, insets.top);
            contentView.constraintForView(ConstraintSet.BOTTOM, view, insets.bottom);
            contentView.getConstraintSet().setDimensionRatio(view.getId(), "1:1");
        }
        contentView.applyConstraints();
    }

    //region helpers

    @Override
    public Size rightViewSize() {
        return App.constants().TableView_Accessory_Size();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isEnabled();
    }

    //endregion

    //region views

    public A getAccessoryView() {
        return rightView;
    }

    public UIImageView getImageView() {
        return leftView;
    }

    public S getSeparator() {
        return separator;
    }

    public UIView getContentView() {
        return contentView;
    }

    //endregion

    //region actions

    public void setSelectionAction(UIControl.TargetDelegate target) {
        interactionLayer.addTarget(this, target);
    }

    //endregion

    /** onAttachedToWindow and onDetachedFromWindow are called
     * multiple times in recycler view */
    @Override public void viewDidLoad() {
        loaded = true;
    }

    /** onAttachedToWindow and onDetachedFromWindow are called
     * multiple times in recycler view */
    @Override public void unLoadView() {
        loaded = false;
    }

}
