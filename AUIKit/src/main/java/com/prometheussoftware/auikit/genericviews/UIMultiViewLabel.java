package com.prometheussoftware.auikit.genericviews;

import android.util.Size;
import android.view.Gravity;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.uiview.UILabel;
import com.prometheussoftware.auikit.uiview.UIStackedViews;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiview.protocols.UIControlProtocol;
import com.prometheussoftware.auikit.uiview.protocols.ViewCreation;

public class UIMultiViewLabel <L extends UIView, R extends UIView, C extends UIView> extends UIView implements UIControlProtocol {

    private UIStackedViews.Vertical<UILabel> labels;

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
    public ALIGNMENT rightViewVerticalAlignment = UIView.ALIGNMENT.CENTER_Y;
    public ALIGNMENT leftViewVerticalAlignment = UIView.ALIGNMENT.CENTER_Y;

    static {
        Identifier.Register(UIMultiViewLabel.class);
    }

    public UIMultiViewLabel() {
        this(1);
    }

    public UIMultiViewLabel(int count) {
        super();
        this.insets = insets();
        createContentView();
        createLabels(count);
    }

    public UIMultiViewLabel(int count, L leftView, R rightView, UIEdgeInsets insets) {
        super();
        this.insets = insets;
        this.leftView = leftView;
        this.rightView = rightView;
        createContentView();
        createLabels(count);
    }

    public UIMultiViewLabel(UIEdgeInsets insets) {
        this(1, null, null, insets);
    }

    public UIMultiViewLabel(C contentView) {
        super();
        this.insets = insets();
        this.contentView = contentView;
        createLabels(1);
    }

    /** @param backView is added on top and covers the entire view */
    public void addBackView(UIView backView) {
        this.backView = backView;
        backView.setBackgroundColor(UIColor.clear());
        contentView.addSubview(backView);
        contentView.bringChildToFront(backView);
        contentView.constraintSidesForView(backView);
        contentView.applyConstraints();
    }

    /** This view is added on top of all other views
     * @param backView is added on top and covers the entire view other than possibly left and right views
     * @param coverLeft pass true to have backView cover left view
     * @param coverRight pass true to have backView cover right view */
    public void addBackView(UIView backView, boolean coverLeft, boolean coverRight) {

        if ((coverLeft || leftView == null) && (coverRight || rightView == null)) {
            addBackView(backView);
            return;
        }

        this.backView = backView;
        backView.setBackgroundColor(UIColor.clear());
        contentView.addSubview(backView);
        contentView.bringChildToFront(backView);

        if (!coverLeft && leftView != null) {
            contentView.constraintViews(backView, ConstraintSet.START, leftView, ConstraintSet.END, leftPadding);
        }
        else {
            contentView.constraintForView(ConstraintSet.START, backView, insets.left);
        }
        if (!coverRight && rightView != null) {
            contentView.constraintViews(backView, ConstraintSet.END, rightView, ConstraintSet.START, rightPadding);
        }
        else {
            contentView.constraintForView(ConstraintSet.END, backView, insets.right);
        }
        contentView.constraintForView(ConstraintSet.TOP, backView);
        contentView.constraintForView(ConstraintSet.BOTTOM, backView);
        contentView.applyConstraints();
    }

    public UIView getBackView() {
        return backView;
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
        createContentView();
        createRightView();
        createLeftView();
    }

    @Override public void loadView() {
        super.loadView();
        addSubview(contentView);
        contentView.addSubview(labels);
        contentView.addSubview(rightView);
        contentView.addSubview(leftView);
    }

    //endregion

    //region constraints

    @Override public void constraintLayout() {
        super.constraintLayout();

        constraintSidesForView(contentView);

        if (leftView != null && rightView != null && labels != null) {
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
        contentView.constraintForView(ConstraintSet.TOP, labels, insets.top);
        contentView.constraintForView(ConstraintSet.BOTTOM, labels, insets.bottom);
        contentView.constraintForView(ConstraintSet.END, labels, insets.right);
        contentView.constraintViews(labels, ConstraintSet.START, leftView, ConstraintSet.END, leftPadding);

        contentView.applyConstraints();
    }

    private void constraintRightLabel() {

        contentView.constraintSizeForView(rightView, rightViewSize);
        constraintVertical(rightView, rightViewVerticalAlignment);

        contentView.constraintForView(ConstraintSet.START, labels, insets.left);
        contentView.constraintForView(ConstraintSet.TOP, labels, insets.top);
        contentView.constraintForView(ConstraintSet.BOTTOM, labels, insets.bottom);
        contentView.constraintForView(ConstraintSet.END, rightView, insets.right);
        contentView.constraintViews(labels, ConstraintSet.END, rightView, ConstraintSet.START, rightPadding);

        contentView.applyConstraints();
    }

    private void constraintLeftRightLabel() {

        contentView.constraintSizeForView(rightView, rightViewSize);
        contentView.constraintSizeForView(leftView, leftViewSize);

        constraintVertical(leftView, leftViewVerticalAlignment);
        constraintVertical(rightView, rightViewVerticalAlignment);

        contentView.constraintForView(ConstraintSet.START, leftView, insets.left);
        contentView.constraintForView(ConstraintSet.TOP, labels, insets.top);
        contentView.constraintForView(ConstraintSet.BOTTOM, labels, insets.bottom);
        contentView.constraintForView(ConstraintSet.END, rightView, insets.right);

        contentView.constraintViews(labels, ConstraintSet.START, leftView, ConstraintSet.END, leftPadding);
        contentView.constraintViews(labels, ConstraintSet.END, rightView, ConstraintSet.START, rightPadding);

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
        constraintSidesForView(labels, insets);
        contentView.applyConstraints();
    }
    
    //endregion

    //region views

    public void createLabels(int count) {
        labels = new UIStackedViews.Vertical(count, new ViewCreation<UILabel>() {
            @Override
            public UILabel view() {
                UILabel label = new UILabel();
                label.setTextColor(UIColor.black(1.0f));
                label.getView().setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                return label;
            }
        });

    }

    public void createRightView() {}

    public void createLeftView() {}

    public void createContentView() {
        contentView = (C) new UIView();
    }

//    /** It will NOT set the view if view is already loaded */
    public void setLeftView(L leftView) {
//        if (!isLoaded())
        contentView.removeSubview(this.leftView);
        this.leftView = leftView;
        contentView.addSubview(leftView);
        constraintLayout();
    }

//    /** It will NOT set the view if view is already loaded */
    public void setRightView(R rightView) {
//        if (!isLoaded())
        contentView.removeSubview(this.rightView);
        this.rightView = rightView;
        contentView.addSubview(rightView);
        constraintLayout();
    }

    public R getRightView() {
        return rightView;
    }

    public L getLeftView() {
        return leftView;
    }

    public UIStackedViews.Vertical<UILabel> getLabels() {
        return labels;
    }

    public UILabel getLabel(int index) {
        UILabel label = labels.viewAtIndex(index);
        return label != null ? label : new UILabel();
    }

    public C getContentView() {
        return contentView;
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
            constraintLayout();
        }
    }

    public void setLeftViewSize(Size leftViewSize) {
        this.leftViewSize = leftViewSize;
        if (leftView != null && isLoaded()) {
            constraintLayout();
        }
    }

    public void setHeight(int height) {
        setMinHeight(height);
    }

    //endregion

    //region helpers

    protected void constraintVertical (UIView view, ALIGNMENT alignment) {
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

    public void setMultiline(int index) {
        labels.viewAtIndex(index).setNumberOfLines(Integer.MAX_VALUE);
    }

    //endregion


    @Override
    public void setUserInteractionEnabled(boolean userInteractionEnabled) {
        if (backView != null)
            backView.setUserInteractionEnabled(userInteractionEnabled);
        else
            super.setUserInteractionEnabled(userInteractionEnabled);
    }

    @Override
    public void addTarget(Object ID, UITargetDelegate target) {
        if (backView instanceof UIControlProtocol)
            ((UIControlProtocol) backView).addTarget(ID, target);
    }
}
