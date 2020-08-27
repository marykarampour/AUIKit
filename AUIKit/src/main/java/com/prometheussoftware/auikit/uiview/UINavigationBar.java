package com.prometheussoftware.auikit.uiview;

import android.util.Size;
import android.view.Gravity;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;

public class UINavigationBar extends UIView {

    private ArrayList<UIButton> leftBarButtonItems = new ArrayList<>();
    private ArrayList<UIButton> rightBarButtonItems = new ArrayList<>();
    private UILabel titleLabel;
    private UIView titleView;
    private UIImageView shadow;

    public UINavigationBar() {
        super();
        init();
    }

    @Override
    public void initView() {
        super.initView();

        titleLabel = new UILabel();
        titleLabel.setGravity(Gravity.CENTER);
        titleLabel.setFont(App.theme().Nav_Bar_Font());
        titleLabel.setTextColor(App.theme().Nav_Bar_Tint_Color());

        shadow = new UIImageView();
        shadow.setScaleType(ImageView.ScaleType.FIT_XY);

        setBackgroundColor(App.theme().Nav_Bar_Background_Color());
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubView(titleLabel);
        addSubView(shadow);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        constraintHeightForView(titleLabel, Dimensions.Int_44());
        constraintHeightForView(shadow, App.constants().Nav_Bar_Shadow_Size());

        constraintCenterXForView(titleLabel);
        constraintCenterYForView(titleLabel);

        constraintForView(ConstraintSet.START, shadow);
        constraintForView(ConstraintSet.END, shadow);
        constraintForView(ConstraintSet.BOTTOM, shadow);

        applyConstraints();
    }

    //region items

    /** Adds a view at the position defined by ConstraintSet.position */
    public void addItem (UIView view, int position) {

        if (position == ConstraintSet.START) {
            if (view == null) {
                addItem(new UIButton(), position);
                return;
            }
            else if (view instanceof UIButton) {
                addSubView(view);

                constraintSizeForView(view, App.constants().Nav_Bar_Icon_Size());
                constraintCenterXForView(view);

                if (leftBarButtonItems.size() == 0) {

                    constraintForView(ConstraintSet.START, view);
                    constraintViews(titleLabel, ConstraintSet.START, view, ConstraintSet.END);
                }
                else {
                    constraintViews(view, ConstraintSet.START, ArrayUtility.lastObject(leftBarButtonItems), ConstraintSet.END);
                    constraintViews(titleLabel, ConstraintSet.START, view, ConstraintSet.END);
                }

                leftBarButtonItems.add((UIButton)view);
            }
        }
        else if (position == ConstraintSet.END) {
            if (view == null) {
                addItem(new UIButton(), position);
                return;
            }
            else if (view instanceof UIButton) {

                addSubView(view);

                constraintSizeForView(view, App.constants().Nav_Bar_Icon_Size());
                constraintCenterXForView(view);

                if (rightBarButtonItems.size() == 0) {

                    constraintForView(ConstraintSet.END, view);
                    constraintViews(titleLabel, ConstraintSet.END, view, ConstraintSet.START);
                }
                else {
                    constraintViews(view, ConstraintSet.END, ArrayUtility.lastObject(rightBarButtonItems), ConstraintSet.START);
                    constraintViews(titleLabel, ConstraintSet.END, view, ConstraintSet.START);
                }

                rightBarButtonItems.add((UIButton)view);
            }
        }
        else {
            if (view == null) {
                addItem(new UILabel(), position);
                return;
            }
            else {
                titleView = view;
                addSubView(view);

                constraintCenterXForView(view);
                constraintCenterYForView(view);

                constraintHeightForView(view, ConstraintSet.WRAP_CONTENT);
                constraintWidthForView(view, ConstraintSet.WRAP_CONTENT);
            }
        }

        applyConstraints();
    }

    //endregion

    //region fields

    public Size iconSize() {
        return App.constants().Nav_Bar_Icon_Size();
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
        return titleView;
    }

    public ArrayList<UIButton> getLeftBarButtonItems() {
        return leftBarButtonItems;
    }

    public void setLeftBarButtonItems(ArrayList<UIButton> leftBarButtonItems) {
        this.leftBarButtonItems = leftBarButtonItems;
    }

    public ArrayList<UIButton> getRightBarButtonItems() {
        return rightBarButtonItems;
    }

    public void setRightBarButtonItems(ArrayList<UIButton> rightBarButtonItems) {
        this.rightBarButtonItems = rightBarButtonItems;
    }

    public UIButton leftBarButtonItem() {
        return ArrayUtility.firstObject(leftBarButtonItems);
    }

    public void setLeftBarButtonItem(UIButton item) {
        ArrayUtility.safeReplace(leftBarButtonItems, 0, item);
    }

    public UIButton rightBarButtonItem() {
        return ArrayUtility.firstObject(rightBarButtonItems);
    }

    public void setRightBarButtonItem(UIButton item) {
        ArrayUtility.safeReplace(rightBarButtonItems, 0, item);
    }

    //endregion
}
