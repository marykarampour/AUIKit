package com.prometheussoftware.auikit.tableview;

import android.util.Size;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.genericviews.UIAccessoryView;
import com.prometheussoftware.auikit.genericviews.UIMultiViewLabel;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.uiview.UIControl;
import com.prometheussoftware.auikit.uiview.UIImageView;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.utility.ViewUtility;

public abstract class UITableViewCell <A extends UIAccessoryView, S extends UIView> extends UIMultiViewLabel <UIImageView, A, UIView> {

    private ACCESSORY_TYPE accessoryType;

    /** Use only in case of static cells */
    public IndexPath indexPath;

    protected S separator;

    private UIControl interactionLayer;

    private int separatorHeight;

    static {
        Identifier.Register(UITableViewCell.class);
    }

    /** Call init() in constructor of subclass */
    public UITableViewCell() {
        super();
        setEnabled(true);
        accessoryType = ACCESSORY_TYPE.NONE;
    }

    private void baseSetup() {
        if (separatorHeight == 0) separatorHeight = separatorHeight();
        getTitleLabel().setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    }

    @Override
    public void init() {
        setSize();
        super.init();
        baseSetup();
        constraintLayout();
        addInteractionLayer();
    }

    @Override
    public void initView() {
        super.initView();
        createSeparator();
        createInteractionLayer();
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        contentView.constraintHeightForView(separator, separatorHeight);
        contentView.constraintForView(ConstraintSet.START, separator, 0);
        contentView.constraintForView(ConstraintSet.END, separator, 0);
        contentView.constraintForView(ConstraintSet.BOTTOM, separator, 0);
        contentView.applyConstraints();
    }

    protected void createSeparator() {
        separator = (S) new UIView();
        separator.setEnabled(false);
        setSeparatorColor(App.theme().Tableview_Separator_Color());
        contentView.addSubView(separator);
    }

    private void createInteractionLayer() {
        interactionLayer = new UIControl();
        interactionLayer.setMultiTargetEnabled(false);
    }

    private void addInteractionLayer() {
        setTopView(interactionLayer, true, false);
    }

    @Override
    public void createRightView() {
        rightView = (A) UIAccessoryView.build(UIAccessoryView.TYPE.IMAGE);
        rightView.setSelectedColor(App.theme().Bright_Blue_Color());
        rightView.setDeselectedColor(App.theme().Bright_Blue_Color());
        rightView.setEnabled(true);
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

    public void setHeight(int height, UIView mainView) {

        constraintSet.clear(mainView.getId());

        boolean hasHeight = height > contentView.getPaddingBottom() + contentView.getPaddingTop();
        if (hasHeight) constraintSidesForView(mainView);
        setMinHeight(height);

        constraintHeightForView(mainView, height);
        contentView.applyConstraints();
    }

    public void setAccessorySize(Size size) {
        setRightViewSize(size);
    }

    public void setSeparatorHeight(int separatorHeight) {
        this.separatorHeight = separatorHeight;
        contentView.constraintHeightForView(separator, separatorHeight);
        contentView.applyConstraints();
    }

    protected int separatorHeight() {
        return Dimensions.Int_1();
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

        if (!ViewUtility.isChildView(contentView, view)) return;

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
        switch (accessoryType) {
            case DETAIL_DISCLOSURE_BUTTON: {
                Size size = App.constants().TableView_Accessory_Size();
                return new Size(size.getWidth()*2, size.getHeight());
            }
            default:
                return App.constants().TableView_Accessory_Size();
        }
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

    public void setAccessoryType(ACCESSORY_TYPE accessoryType) {
        this.accessoryType = accessoryType;
        switch (accessoryType) {
            case CHECKMARK: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Checkmark_Image());
                rightView.setOffImage(App.assets().Checkmark_Image());
            }
            break;
            case DETAIL_BUTTON: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Details_Image());
                rightView.setOffImage(App.assets().Details_Image());
            }
            break;
            case DISCLOSURE_INDICATOR: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Disclosure_Image());
                rightView.setOffImage(App.assets().Disclosure_Image());
            }
            break;
            case DROPDOWN_INDICATOR_EXPANDED: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Down_Chevron_Image());
                rightView.setOffImage(App.assets().Down_Chevron_Image());
            }
            break;
            case DROPDOWN_INDICATOR_COLLAPSED: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Right_Chevron_Image());
                rightView.setOffImage(App.assets().Right_Chevron_Image());
            }
            break;
            case DETAIL_DISCLOSURE_BUTTON: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Details_Disclosure_Image());
                rightView.setOffImage(App.assets().Details_Disclosure_Image());
            }
            break;
            default: {
                rightView.setGone(true);
            }
            break;
        }

        rightView.setOn(true);
        setSizeForView(rightViewSize(), rightView);
    }

    public enum ACCESSORY_TYPE {
        NONE,
        DISCLOSURE_INDICATOR,
        DROPDOWN_INDICATOR_COLLAPSED,
        DROPDOWN_INDICATOR_EXPANDED,
        DETAIL_DISCLOSURE_BUTTON,
        CHECKMARK,
        DETAIL_BUTTON
    }

    //endregion

    //region actions

    public void setSelectionAction(Object obj, UITargetDelegate.TouchUp target) {
        interactionLayer.addTouchUpTarget(obj, target);
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


    //region subclass

    /** A basic concrete subclass of UITableViewCell */
    public static class Concrete extends UITableViewCell {

        public Concrete() {
            super();
            init();
        }

        @Override
        public void initView() {
            super.initView();

            getTitleLabel().setTextColor(UIColor.black(1.0f));
            contentView.setBackgroundColor(UIColor.white(1.0f));
        }

        @Override
        protected int leftPadding() {
            return Dimensions.Int_8();
        }

        @Override
        protected int rightPadding() {
            return Dimensions.Int_8();
        }

        @Override
        protected UIEdgeInsets insets() {
            return new UIEdgeInsets(Dimensions.Int_8(), Dimensions.Int_8(), Dimensions.Int_8(), Dimensions.Int_8());
        }
    }

    //endregion

}
