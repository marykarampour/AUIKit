package com.prometheussoftware.auikit.uiview;

import android.util.Size;
import android.view.Gravity;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;

public class UINavigationBar extends UIView {

    private static final int HORIZONTAL_MARGIN = Dimensions.Int_8();
    private static final int VERTICAL_MARGIN = Dimensions.Int_4();

    private UILabel titleLabel;
    private UIImageView shadow;
    private int titleViewHeight = Dimensions.Int_48();
    private int statusBarHeight = App.constants().Status_Bar_Height();

    private TitleViewHolder titleViewHolder;
    private BarButtonHolder leftItemsViewHolder;
    private BarButtonHolder rightItemsViewHolder;

    public UINavigationBar() {
        super();
        init();
    }

    private void setupHolders() {

        titleLabel = new UILabel();
        titleLabel.setGravity(Gravity.CENTER);
        titleLabel.setFont(App.theme().Nav_Bar_Font());
        titleLabel.setTextColor(App.theme().Nav_Bar_Tint_Color());
        titleViewHolder.setView(titleLabel);
    }

    @Override
    public void initView() {
        super.initView();

        titleViewHolder = new TitleViewHolder();
        leftItemsViewHolder = new BarButtonHolder();
        rightItemsViewHolder = new BarButtonHolder();

        shadow = new UIImageView();
        shadow.setScaleType(ImageView.ScaleType.FIT_XY);

        setBackgroundColor(App.theme().Nav_Bar_Background_Color());
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubView(titleViewHolder);
        addSubView(leftItemsViewHolder);
        addSubView(rightItemsViewHolder);
        addSubView(shadow);
        setupHolders();
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        constraintHeightForView(shadow, App.constants().Nav_Bar_Shadow_Size());
        constraintWidthForView(leftItemsViewHolder, ConstraintSet.WRAP_CONTENT);
        constraintWidthForView(rightItemsViewHolder, ConstraintSet.WRAP_CONTENT);

        constraintSidesForView(titleViewHolder, new UIEdgeInsets(statusBarHeight, 0, VERTICAL_MARGIN, 0));
        constraintForView(ConstraintSet.START, shadow);
        constraintForView(ConstraintSet.END, shadow);
        constraintForView(ConstraintSet.BOTTOM, shadow);
        constraintForView(ConstraintSet.BOTTOM, leftItemsViewHolder, VERTICAL_MARGIN);
        constraintForView(ConstraintSet.BOTTOM, rightItemsViewHolder, VERTICAL_MARGIN);
        constraintForView(ConstraintSet.TOP, leftItemsViewHolder, statusBarHeight);
        constraintForView(ConstraintSet.TOP, rightItemsViewHolder, statusBarHeight);
        constraintForView(ConstraintSet.START, leftItemsViewHolder, HORIZONTAL_MARGIN);
        constraintForView(ConstraintSet.END, rightItemsViewHolder, HORIZONTAL_MARGIN);
        applyConstraints();
    }

    //region items

    public static UIBarButton barButtonItem(UIImage image) {
        UIBarButton button = new UIBarButton();
        button.setImage(image);
        button.setTintColor(App.theme().Nav_Bar_Tint_Color());
        return button;
    }

    /** Adds a view at the position defined by UIEnum.ALIGNMENT.
     * @param view The view to be added. Pass null to add a button at left or right, or label in center. */
    public void addItem (UIView view, UIEnum.ALIGNMENT position) {
        switch (position) {
            case LEFT: {
                if (view == null) {
                    addItem(new UIButton(), position);
                    return;
                }
                else if (view instanceof UIButton) {
                    leftItemsViewHolder.addView(view);
                }
            }
            case RIGHT: {
                if (view == null) {
                    addItem(new UIButton(), position);
                    return;
                }
                else if (view instanceof UIButton) {
                    rightItemsViewHolder.addView(view);
                }
            }
            default: {
                if (view == null) {
                    addItem(new UILabel(), position);
                    return;
                }
                else {
                    titleViewHolder.setView(view);

                }
            }
        }
    }

    //endregion

    //region fields

    public Size iconSize() {
        return App.constants().Nav_Bar_Icon_Size();
    }

    public void setLeftItemSize (Size size) {
        leftItemsViewHolder.setItemSize(size);
    }

    public void setRightItemSize (Size size) {
        rightItemsViewHolder.setItemSize(size);
    }

    public void setTitleViewHeight(int titleViewHeight) {
        this.titleViewHeight = titleViewHeight;
        updateTitleViewHeight();
    }

    public void updateTitleViewHeight() {
        titleViewHolder.updateViewHeight();
    }

    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    public void setShadowTintColor(UIColor shadowTintColor) {
        if (shadowTintColor == null || shadowTintColor.get() == 0) {
            shadow.setImage(null);
        }
        else {
            shadow.setTintColor(shadowTintColor);
            shadow.setImage(App.assets().Shadow_Gradient_Image());
        }
    }

    //endregion

    //region controls

    public UIView getTitleView() {
        return titleViewHolder.getView();
    }

    public ArrayList<UIBarButton> getLeftBarButtonItems() {
        return leftItemsViewHolder.getViews();
    }

    public ArrayList<UIBarButton> getRightBarButtonItems() {
        return rightItemsViewHolder.getViews();
    }

    public void setLeftBarButtonItems(ArrayList<UIBarButton> leftBarButtonItems) {
        leftItemsViewHolder.setViews(leftBarButtonItems);
    }

    public void setRightBarButtonItems(ArrayList<UIBarButton> rightBarButtonItems) {
        rightItemsViewHolder.setViews(rightBarButtonItems);
    }

    /** The contents of this property always refer to the first bar button item in the leftBarButtonItems array.
     * Assigning a new value to this property replaces the first item in the leftBarButtonItems array with the new value.
     * Setting this property to null removes the first item in the array.
     * If the bar button item is already in the array, it is moved from its current location to the front of the array.
     * */
    public void setLeftBarButtonItem(UIBarButton item) {
        leftItemsViewHolder.setView(item);
    }

    /** The contents of this property always refer to the first bar button item in the rightBarButtonItems array.
     * Assigning a new value to this property replaces the first item in the rightBarButtonItems array with the new value.
     * Setting this property to null removes the first item in the array.
     * If the bar button item is already in the array, it is moved from its current location to the front of the array.
     * */
    public void setRightBarButtonItem(UIBarButton item) {
        rightItemsViewHolder.setView(item);
    }

    public UIBarButton leftBarButtonItem() {
        return ArrayUtility.firstObject(getLeftBarButtonItems());
    }

    public UIBarButton rightBarButtonItem() {
        return ArrayUtility.firstObject(getRightBarButtonItems());
    }

    //TODO: maybe create a class for this back button to check if it is back button
    public void setBackBarButtonItemHidden(boolean hidden) {
        UIBarButton button = leftBarButtonItem();
        if (button != null) {
            button.setHidden(hidden);
        }
    }

    //endregion

    //region private classes

    class TitleViewHolder extends UIView {

        private UIView view;

        public void setView(UIView view) {

            removeSubView(this.view);
            this.view = view;
            addSubView(view);
            updateConstraints();
        }

        @Override
        public void constraintLayout() {
            super.constraintLayout();
            if (view != null) {
                updateConstraints();
            }
        }

        private void updateConstraints() {

            clearConstraints(view);
            constraintForView(ConstraintSet.START, view);
            constraintForView(ConstraintSet.END, view);
            constraintForView(ConstraintSet.BOTTOM, view);
            updateViewHeight();
        }

        protected void updateViewHeight() {
            if (isLoaded()) {
                constraintHeightForView(view, titleViewHeight);
                applyConstraints();
            }
        }

        public UIView getView() {
            return view;
        }
    }

    class BarButtonHolder extends UIViewHolder.Row <UIBarButton> {
    }

    //endregion
}
