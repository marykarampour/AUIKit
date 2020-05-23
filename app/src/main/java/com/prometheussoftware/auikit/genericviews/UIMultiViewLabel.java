package com.prometheussoftware.auikit.genericviews;

import android.util.Size;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.uiview.UIEnum;
import com.prometheussoftware.auikit.uiview.UILabel;
import com.prometheussoftware.auikit.uiview.UIView;

public class UIMultiViewLabel <L extends UIView, R extends UIView, C extends UIView> extends UIView {

    private UILabel titleLabel;

    /** An optional view at the back of all views
     * if added will cover the entire contentView */
    private UIView backView;

    protected C contentView;
    protected L leftView;
    protected R rightView;

    /** Insets between the subviews and edges of parent, i.e. this */
    private UIEdgeInsets insets;

    /** PAdding between right view and title or left view */
    private int rightPadding;

    /** PAdding between left view and title or right view
     * @apiNote This has priority over rightPadding */
    private int leftPadding;

    private Size rightViewSize;
    private Size leftViewSize;
    public UIEnum.ALIGNMENT rightViewVerticalAlignment = UIEnum.ALIGNMENT.CENTER_Y;
    public UIEnum.ALIGNMENT leftViewVerticalAlignment = UIEnum.ALIGNMENT.CENTER_Y;


    static {
        Identifier.Register(UIMultiViewLabel.class);
    }

    public UIMultiViewLabel() {
        super();
    }

    public UIMultiViewLabel(C contentView) {
        super();
        this.contentView = contentView;
    }

    public void addBackView() {

    }

    //region life cycle

    @Override public void init() {
        if (insets == null) insets = insets();
        if (rightPadding == 0) rightPadding = rightPadding();
        if (leftPadding == 0) leftPadding = leftPadding();
        if (rightViewSize == null) rightViewSize = rightViewSize();
        if (leftViewSize == null) leftViewSize = leftViewSize();
        super.init();
    }

    @Override public void initView() {
        super.initView();
        createTitleLabel();
        createContentView();
        createRightView();
        createLeftView();
    }

    @Override public void loadView() {
        super.loadView();
        addSubView(contentView);
        contentView.addSubView(titleLabel);
        contentView.addSubView(rightView);
        contentView.addSubView(leftView);
    }

    //endregion

    //region constraints

    @Override public void constraintLayout() {
        super.constraintLayout();

        constraintSidesForView(contentView);

        if (leftView != null && rightView != null && titleLabel != null) {
            constraintLeftRightLabel();
        }
        else if (leftView != null && rightView != null) {
            constraintLeftRight();
        }
        else if (leftView != null) {
            constraintLeftLabel();
        }
        else if (rightView != null) {
            constraintRightLabel();
        }
        else {
            constraintLabel();
        }
        applyConstraints();
    }

    private void constraintLeftLabel() {

        contentView.constraintSizeForView(leftView, leftViewSize);
        constraintVertical(leftView, leftViewVerticalAlignment);

        contentView.constraintForView(ConstraintSet.START, leftView, insets.left);
        contentView.constraintForView(ConstraintSet.TOP, titleLabel, insets.top);
        contentView.constraintForView(ConstraintSet.BOTTOM, titleLabel, insets.bottom);
        contentView.constraintForView(ConstraintSet.END, titleLabel, insets.right);
        contentView.constraintViews(titleLabel, ConstraintSet.START, leftView, ConstraintSet.END, leftPadding);

        contentView.applyConstraints();
    }

    private void constraintRightLabel() {

        contentView.constraintSizeForView(rightView, rightViewSize);
        constraintVertical(rightView, rightViewVerticalAlignment);

        contentView.constraintForView(ConstraintSet.START, titleLabel, insets.left);
        contentView.constraintForView(ConstraintSet.TOP, titleLabel, insets.top);
        contentView.constraintForView(ConstraintSet.BOTTOM, titleLabel, insets.bottom);
        contentView.constraintForView(ConstraintSet.END, rightView, insets.right);
        contentView.constraintViews(titleLabel, ConstraintSet.END, rightView, ConstraintSet.START, rightPadding);

        contentView.applyConstraints();
    }

    private void constraintLeftRightLabel() {

        contentView.constraintSizeForView(rightView, rightViewSize);
        contentView.constraintSizeForView(leftView, leftViewSize);

        constraintVertical(leftView, leftViewVerticalAlignment);
        constraintVertical(rightView, rightViewVerticalAlignment);

        contentView.constraintForView(ConstraintSet.START, leftView, insets.left);
        contentView.constraintForView(ConstraintSet.TOP, titleLabel, insets.top);
        contentView.constraintForView(ConstraintSet.BOTTOM, titleLabel, insets.bottom);
        contentView.constraintForView(ConstraintSet.END, rightView, insets.right);

        contentView.constraintViews(titleLabel, ConstraintSet.START, leftView, ConstraintSet.END, leftPadding);
        contentView.constraintViews(titleLabel, ConstraintSet.END, rightView, ConstraintSet.START, rightPadding);

        contentView.applyConstraints();
    }

    private void constraintLeftRight() {

        if (0 < leftViewSize.getHeight() && 0 < leftViewSize.getWidth()) {
            contentView.constraintSizeForView(leftView, leftViewSize);
        }
        else if ( 0 < leftViewSize.getWidth()) {
            contentView.constraintWidthForView(leftView, leftViewSize.getWidth());
        }
        else if (0 < leftViewSize.getHeight()) {
            contentView.constraintWidthForView(leftView, leftViewSize.getHeight());
        }
        else if (0 < rightViewSize.getHeight() && 0 < rightViewSize.getWidth()) {
            contentView.constraintSizeForView(rightView, rightViewSize);
        }
        else if ( 0 < rightViewSize.getWidth()) {
            contentView.constraintWidthForView(rightView, rightViewSize.getWidth());
        }
        else if (0 < rightViewSize.getHeight()) {
            contentView.constraintWidthForView(rightView, rightViewSize.getHeight());
        }

        UIView heightView = (rightViewSize.getHeight() < leftViewSize.getHeight() ? leftView : rightView);

        contentView.constraintForView(ConstraintSet.TOP, heightView, insets.bottom);
        contentView.constraintForView(ConstraintSet.BOTTOM, heightView, insets.bottom);
        contentView.constraintForView(ConstraintSet.START, leftView, insets.left);
        contentView.constraintForView(ConstraintSet.END, rightView, insets.right);

        if (heightView != leftView) {
            constraintVertical(leftView, leftViewVerticalAlignment);
        }
        if (heightView != rightView) {
            constraintVertical(rightView, rightViewVerticalAlignment);
        }

        contentView.constraintViews(rightView, ConstraintSet.START, leftView, ConstraintSet.END, 0 < leftPadding ? leftPadding : rightPadding);

        contentView.applyConstraints();
    }
    
    private void constraintLabel() {
        constraintSidesForView(titleLabel, insets);
        contentView.applyConstraints();
    }
    
    //endregion

    //region views

    public void createTitleLabel() {
        titleLabel = new UILabel();
        titleLabel.setTextColor(UIColor.black(1.0f));
    }

    public void createRightView() { }

    public void createLeftView() { }

    public void createContentView() {
        contentView = (C) new UIView();
    }

    /** It will NOT set the view if view is already loaded */
    public void setLeftView(L leftView) {
        if (!isLoaded())
        this.leftView = leftView;
    }

    /** It will NOT set the view if view is already loaded */
    public void setRightView(R rightView) {
        if (!isLoaded())
        this.rightView = rightView;
    }

    //endregion

    //region sizes

    protected UIEdgeInsets insets() {
        return new UIEdgeInsets();
    }

    protected int rightPadding() {
        return 0;
    }

    protected int leftPadding() {
        return 0;
    }

    public Size rightViewSize() {
        return new Size(0, 0);
    }

    public Size leftViewSize() {
        return new Size(0, 0);
    }

    /** This has higher priority than that being set by the class itself */
    public void setInsets(UIEdgeInsets insets) {
        this.insets = insets;
    }

    /** This has higher priority than that being set by the class itself */
    public void setRightViewSize(Size rightViewSize) {
        this.rightViewSize = rightViewSize;
        if (rightView != null && isLoaded()) {
            contentView.constraintSizeForView(rightView, rightViewSize);
            contentView.applyConstraints();
        }
    }

    public void setLeftViewSize(Size leftViewSize) {
        this.leftViewSize = leftViewSize;
        if (leftView != null && isLoaded()) {
            contentView.constraintSizeForView(leftView, leftViewSize);
            contentView.applyConstraints();
        }
    }

    //endregion

    //region helpers

    protected void constraintVertical (UIView view, UIEnum.ALIGNMENT alignment) {
        switch (alignment) {
            case TOP:
                contentView.constraintForView(ConstraintSet.TOP, view, insets.top);
                break;
            case BOTTOM:
                contentView.constraintForView(ConstraintSet.BOTTOM, view, insets.bottom);
                break;
            default:
                contentView.constraintCenterYForView(view);
                break;
        }
    }

    //endregion

}
